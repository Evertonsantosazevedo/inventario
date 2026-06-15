# Pendências do Projeto Inventário

Projeto: **Sistema de Gerenciamento de Inventário**  
Stack: Quarkus · PostgreSQL · JWT · Qute Templates

---

## 1. Segurança — Migração para JWT via Cookie

Atualmente o token JWT é retornado no corpo da resposta (`LoginResponseDTO`) e o cliente o envia via header `Authorization: Bearer`. A mudança prevista é passar a usar **cookie HttpOnly**, o que melhora a segurança contra ataques XSS.

### O que precisa ser alterado

**`AuthController`** — endpoint `POST /auth/login`
- Após gerar o token em `UsuarioBO.realizarLogin()`, em vez de retornar o token no JSON, deve criar um `NewCookie` com as flags `HttpOnly`, `Secure` e `SameSite=Strict` e adicioná-lo ao `Response`.
- Exemplo de resposta esperada:
  ```
  Set-Cookie: jwt=<token>; HttpOnly; Secure; SameSite=Strict; Path=/
  ```

**`UsuarioBO`** — método `realizarLogin()`
- Pode continuar gerando o token JWT normalmente.
- Considerar retornar apenas o token (String) em vez do `LoginResponseDTO` completo, deixando a montagem do cookie para o controller.

**`LoginResponseDTO`**
- Remover o campo `token` do DTO, já que ele não deve mais ser exposto no corpo da resposta.
- Manter apenas `nome` e `perfil` para o frontend saber quem está logado.

**`application.properties`**
- Habilitar a leitura do JWT a partir do cookie:
  ```properties
  mp.jwt.token.header=Cookie
  mp.jwt.token.cookie=jwt
  ```

**Endpoint de logout** — ainda não existe
- Criar `POST /auth/logout` que invalida o cookie retornando um `Set-Cookie` com `Max-Age=0`.

**`MovimentacaoController`** — remoção do `@Inject JsonWebToken jwt`
- Após a mudança, o JWT continua sendo injetável normalmente pelo Quarkus; verificar se a extração do claim `"id"` em `UsuarioBO.desativarUsuario()` e em `MovimentacaoController.saidaProdutos()` continua funcionando.

---
    
## 2. Rastreabilidade — Log de Auditoria via Filter

A entidade `AuditoriaLogEntity` já existe mas não é usada em nenhum ponto do código. A implementação será feita através de um **`ContainerRequestFilter`** do Jakarta REST, centralizando o registro sem poluir as classes de negócio.

### O que precisa ser criado

**`AuditoriaFilter`** — nova classe em `br.edu.ifg.luziania.filter`
- Implementar `jakarta.ws.rs.container.ContainerRequestFilter`.
- Anotar com `@Provider` e `@Authenticated` (ou `@RolesAllowed`) para interceptar apenas rotas autenticadas.
- Extrair do `JsonWebToken` o claim `"id"` para identificar o usuário que fez a requisição.
- Montar a string de `acao` com método HTTP + path (ex.: `"POST /produtos"`).
- Persistir um `AuditoriaLogEntity` com usuário, ação e `LocalDateTime.now()`.

**`AuditoriaLogDAO`** — nova classe em `br.edu.ifg.luziania.model.dao`
- Método `salvar(AuditoriaLogEntity log)` anotado com `@Transactional`.
- Injetado dentro do `AuditoriaFilter`.

**Rotas a excluir da auditoria** (não requerem autenticação)
- `GET /auth/login` — tela de login
- `POST /auth/login` — autenticação
- `GET /auth/dashboard` — tela inicial (avaliar se deve ser protegida)

---

## 3. Histórico de Movimentações — Entrada sem Registro

`ProdutoBO.registrarEntrada()` atualiza a quantidade do produto mas **não grava na tabela `movimentacoes`**. A saída já faz isso corretamente.

### O que precisa ser alterado

**`ProdutoBO`** — método `registrarEntrada()`
- Injetar `MovimentacaoDAO` e `UsuarioDAO`.
- Após atualizar a quantidade, criar e persistir um `MovimentacaoEntity` com `TipoMovimentacao.ENTRADA`.
- O `idUsuario` deve vir do JWT (via `@Inject JsonWebToken jwt`), da mesma forma que já é feito no `MovimentacaoController`.

**`ProdutoController`** — endpoint `POST /produtos/{id}/entrada`
- Injetar `JsonWebToken` e passar o `idUsuario` para o BO, ou deixar o BO extrair diretamente do JWT.

---

## 4. Funcionalidades de Usuário Incompletas

### 4.1 Reativar usuário

Existe `PATCH /usuarios/{id}/desativar` mas não há o caminho inverso.

**O que criar:**
- Endpoint `PATCH /usuarios/{id}/ativar` em `UsuarioController`.
- Método `ativarUsuario(Long id)` em `UsuarioBO` (espelho de `desativarUsuario`, com `setAtivo(true)`).

### 4.2 Troca de senha

O `PUT /usuarios/{id}` edita nome, email e perfil, mas **não altera a senha**.

**O que alterar:**
- Adicionar campo opcional `senha` em `UsuarioEdicaoDTO`.
- Em `UsuarioBO.editarUsuario()`, verificar se o campo senha foi enviado e, se sim, aplicar `BcryptUtil.bcryptHash()` antes de persistir.

---
    
## 5. Exclusão de Produto

Não há endpoint para remover um produto.

### O que criar

**`ProdutoDAO`** — método `deletar(Long id)` anotado com `@Transactional`.

**`ProdutoBO`** — método `deletarProduto(Long id)`:
- Verificar se o produto existe; lançar 404 caso contrário.
- Verificar se há movimentações vinculadas antes de deletar (evitar violação de FK).

**`ProdutoController`** — endpoint `DELETE /produtos/{id}` com `@RolesAllowed("ADMINISTRADOR")`.

---

## 6. Validação de E-mail

Na `UsuarioEntity`, a anotação `@Pattern` para validar formato de e-mail está **comentada**.

**O que fazer:**
- Descomentar a anotação ou, preferencialmente, usar a anotação padrão `@Email` do Jakarta Validation (`jakarta.validation.constraints.Email`) — mais simples e igualmente eficaz.
- Aplicar também no `CadastroRequestDTO` e `UsuarioEdicaoDTO`, que são os pontos de entrada da API.

---

## 7. Proteção das Rotas de Template HTML

O endpoint `GET /usuarios/gerenciar` retorna o HTML da tela de gerenciamento sem nenhuma restrição de acesso.

**O que alterar:**
- Adicionar `@RolesAllowed("ADMINISTRADOR")` (ou ao menos `@Authenticated`) no método `telaGerenciarUsuarios()` do `UsuarioController`.
- Revisar `GET /auth/dashboard` pelo mesmo motivo.

---

## 8. Telas HTML Incompletas

Atualmente existem templates apenas para `login`, `dashboard` e `gerenciarUsuarios`. O frontend está incompleto.

**Templates a criar:**

| Tela | Caminho sugerido |
|---|---|
| Listagem e cadastro de produtos | `templates/ProdutoController/produtos.html` |
| Listagem de movimentações | `templates/MovimentacaoController/movimentacoes.html` |
| Relatório / log de auditoria | `templates/AuthController/auditoria.html` |

Cada template precisa do JS correspondente em `META-INF/resources/js/`.

---

## 9. Testes Automatizados

Os únicos testes existentes são os gerados pelo scaffolding do Quarkus (`GreetingResourceTest`) e não cobrem nada do sistema real.

**O que criar:**

- `AuthControllerTest` — fluxo de login com credenciais válidas e inválidas; verificar cookie na resposta após a migração.
- `ProdutoControllerTest` — cadastro, listagem, edição, entrada de estoque e exclusão.
- `MovimentacaoControllerTest` — saída com estoque suficiente e insuficiente; verificar registro no histórico.
- `UsuarioControllerTest` — cadastro, desativar, reativar, edição e troca de senha.
- `AuditoriaFilterTest` — verificar se o log é gravado ao chamar rotas protegidas.

---

## 10. Dados Iniciais (`import.sql`)

O arquivo `import.sql` está vazio (apenas comentários). Sem um usuário administrador pré-cadastrado, não é possível usar a API em dev/test sem criação manual.

**O que adicionar:**
- Um `INSERT` com um usuário `ADMINISTRADOR` com senha bcrypt conhecida para uso em desenvolvimento.
- Produtos de exemplo para facilitar testes manuais de movimentação.

---
    
## Resumo das Pendências

| # | Item | Prioridade |
|---|---|---|
| 1 | Migrar autenticação para JWT via cookie HttpOnly | 🔴 Alta |
| 2 | Implementar `AuditoriaFilter` e `AuditoriaLogDAO` | 🔴 Alta |
| 3 | Registrar entrada de estoque no histórico de movimentações | 🔴 Alta |
| 4 | Endpoint de reativar usuário | 🟡 Média |
| 5 | Troca de senha na edição de usuário | 🟡 Média |
| 6 | Endpoint de exclusão de produto | 🟡 Média |
| 7 | Proteger rotas HTML com `@RolesAllowed` | 🟡 Média |
| 8 | Reativar validação de formato de e-mail | 🟡 Média |
| 9 | Criar telas HTML para produtos, movimentações e auditoria | 🟠 Baixa |
| 10 | Escrever testes automatizados reais | 🟠 Baixa |
| 11 | Popular `import.sql` com dados iniciais de desenvolvimento | 🟠 Baixa |
