# Sistema de Gerenciamento de Inventário

Este projeto é um software completo para gerenciamento de estoque, desenvolvido como projeto prático para a disciplina de Programação para Web. A aplicação utiliza tecnologias modernas no ecossistema Java (Quarkus) e uma interface web responsiva e intuitiva.

## 🚀 Tecnologias Utilizadas

### Back-end
*   **Java 21**
*   **Quarkus**: Framework Java nativo para nuvem.
*   **JAX-RS**: Para criação de endpoints REST.
*   **Hibernate ORM (JPA)**: Camada de persistência relacional utilizando `EntityManager`.
*   **SmallRye JWT**: Segurança e autenticação baseada em tokens.
*   **PostgreSQL**: Banco de dados relacional.

### Front-end
*   **HTML5 & CSS3**: Interface moderna com tema escuro e visual premium.
*   **JavaScript (Vanilla)**: Lógica de interface, manipulação do DOM e consumo de APIs assíncronas via `fetch` e `async/await`.
*   **Qute**: Mecanismo de templates do Quarkus para renderização de páginas no servidor.

## 🏛️ Arquitetura e Padrões
O projeto segue rigorosamente o modelo **MVC** (Model-View-Controller) e utiliza os seguintes padrões:
*   **DAO (Data Access Object)**: Isolamento do acesso aos dados e consultas JPQL estruturadas.
*   **Entity**: Representação fiel das tabelas do banco de dados utilizando anotações JPA.
*   **BO (Business Object)**: Centralização de TODAS as regras de negócio e validações (ex: validação de saldo insuficiente em saídas).
*   **DTO (Data Transfer Object)**: Comunicação exclusiva entre front-end e back-end utilizando Java Records para garantir segurança e esconder estruturas do banco de dados.

## 🔐 Funcionalidades Principais

1.  **Autenticação e Segurança**: Login seguro via JWT armazenado em Cookie HTTP-Only (`HttpOnly` e `Strict`), garantindo proteção contra XSS. Controle de acesso baseado em perfis (RBAC).
2.  **Gerenciamento de Usuários (ADM)**: Cadastro, edição e ativação/desativação de operadores do sistema.
3.  **Catálogo de Produtos**: Controle centralizado de itens, marcas e valores, com ordenação dinâmica por colunas e paginação.
4.  **Movimentações de Estoque**: Registro de entradas e saídas de produtos com atualização em tempo real do saldo e validação impeditiva de estoque insuficiente.
5.  **Auditoria Automatizada e Visualização**: Registro automático de todas as ações de usuários via filtro JAX-RS (`AuditoriaFilter`) e tela dedicada de Logs de Auditoria para administradores com paginação e ordenação.

## 👥 Perfis de Acesso
*   **ADMINISTRADOR**: Acesso total ao sistema, incluindo gestão de usuários, relatórios de auditoria e controle estrutural de produtos (criar, editar, excluir).
*   **OPERADOR**: Focado na operação diária, com acesso ao catálogo de produtos (apenas visualização e entrada) e registro de entradas/saídas no histórico de movimentações.

## 🛠️ Como Executar

### Pré-requisitos
*   Java 21 instalado.
*   Banco de Dados PostgreSQL configurado (conforme `application.properties`).

### Execução em modo de desenvolvimento
```shell script
./mvnw quarkus:dev
```
A aplicação estará disponível em `http://localhost:8080`.

> [!TIP]
> **Dev Services**: Se você possuir o Docker rodando em sua máquina e remover/comentar as configurações explícitas do datasource no arquivo `application.properties`, o Quarkus iniciará automaticamente um container PostgreSQL de desenvolvimento, aplicando as migrações e o script `import.sql` sem a necessidade de instalação manual de banco de dados.

### Compilação e Empacotamento
```shell script
./mvnw package
```
O arquivo gerado será `target/quarkus-app/quarkus-run.jar`.

---
**Desenvolvido como projeto prático individual.**

