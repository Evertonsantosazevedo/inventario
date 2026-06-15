# Sistema de Gerenciamento de Inventário

Este projeto é um software completo para gerenciamento de estoque, desenvolvido como projeto prático para a disciplina de Programação para Web. A aplicação utiliza tecnologias modernas no ecossistema Java (Quarkus) e uma interface web responsiva e intuitiva.

## 🚀 Tecnologias Utilizadas

### Back-end
*   **Java 11+**
*   **Quarkus**: Framework Java nativo para nuvem.
*   **JAX-RS**: Para criação de endpoints REST.
*   **Hibernate ORM com Panache**: Camada de persistência simplificada.
*   **SmallRye JWT**: Segurança e autenticação baseada em tokens.
*   **PostgreSQL**: Banco de dados relacional.

### Front-end
*   **HTML5 & CSS3**: Interface moderna com tema escuro.
*   **JavaScript (Vanilla)**: Lógica de interface, consumo de APIs e manipulação do DOM.
*   **Qute**: Mecanismo de templates do Quarkus para renderização de páginas.

## 🏛️ Arquitetura e Padrões
O projeto segue rigorosamente o modelo **MVC** (Model-View-Controller) e utiliza os seguintes padrões:
*   **DAO (Data Access Object)**: Isolamento do acesso aos dados.
*   **Entity**: Representação fiel das tabelas do banco de dados.
*   **BO (Business Object)**: Centralização de TODAS as regras de negócio e validações.
*   **DTO (Data Transfer Object)**: Comunicação exclusiva entre front-end e back-end para garantir segurança e performance.

## 🔐 Funcionalidades Principais

1.  **Autenticação e Segurança**: Login seguro via JWT. Sessões protegidas e controle de acesso baseado em perfis (RBAC).
2.  **Gerenciamento de Usuários (ADM)**: Cadastro, edição e ativação/desativação de operadores do sistema.
3.  **Catálogo de Produtos**: Controle centralizado de itens, marcas e valores, com busca e ordenação.
4.  **Movimentações de Estoque**: Registro de entradas e saídas com atualização em tempo real do saldo e validação de estoque insuficiente.
5.  **Auditoria Automatizada**: Registro sistêmico de todas as ações realizadas pelos usuários (Quem, O que e Quando).

## 👥 Perfis de Acesso
*   **ADMINISTRADOR**: Acesso total ao sistema, incluindo gestão de usuários, auditoria e controle estrutural de produtos.
*   **OPERADOR**: Focado na operação diária, com acesso ao catálogo de produtos e registro de entradas/saídas de estoque.

## 🛠️ Como Executar

### Pré-requisitos
*   Java 11 ou superior instalado.
*   Banco de Dados PostgreSQL configurado (conforme `application.properties`).

### Execução em modo de desenvolvimento
```shell script
./mvnw quarkus:dev
```
A aplicação estará disponível em `http://localhost:8080`.

### Compilação e Empacotamento
```shell script
./mvnw package
```
O arquivo gerado será `target/quarkus-app/quarkus-run.jar`.

---
**Desenvolvido como projeto prático individual.**
