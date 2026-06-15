Descrição do Projeto Prático   

O projeto prático consiste em implementar um software completo para Web utilizando no front-end as tecnologias HTML, CSS e JS e no back-end o ecossistema Java EE. O projeto deverá ser desenvolvido individualmente.   

Os requisitos funcionais e não funcionais comum a todas as aplicações (sistema web) são:   

    Autenticar usuário: O sistema deverá solicitar a autenticação de usuário por meio de e-mail e senha. Somente usuários autenticados poderão ter acesso à página principal da aplicação.   

    Manter usuário: O sistema deverá permitir o cadastro de usuários que poderão acessar os recursos da aplicação e executar funções conforme seu perfil de usuário.   

    Manter perfil de usuário: O sistema deverá permitir o cadastro de perfil de usuários com configurações específicas de acesso a cada recurso funcional disponível na aplicação ou possuir papéis específicos para diferentes usuários, com relação de recursos funcionais bem definidos.   

    Exibir opções de navegação de recursos: O sistema deverá apresentar um mecanismo de navegação para todas as interfaces de usuário de modo que permita retornar ao passo anterior ou a interface principal da aplicação. Uma sugestão para implementação desse mecanismo de navegação é a utilização de âncoras (links) disponíveis em um menu da aplicação.   

    Dois casos de uso específico do domínio do seu problema: Você deverá definir pelo menos dois requisitos funcionais para sua aplicação e desenvolvê-los integralmente. Não serão aceitos como casos de uso cadastros de dados auxiliares ou cadastros triviais (Ex: cadastro de tipo, cadastro de status, cadastro de categoria, etc...). Caso sua aplicação possua cadastro de usuário a partir da interface de autenticação (login), é importante que defina pelo menos um caso de uso com necessidade de permissões de acesso distintas. Ex: caso seja um e-commerce, a função compra pode ser utilizada por qualquer usuário autenticado, porém o cadastro de produtos só pode ser acessado por um usuário com perfil administrador.   

    Rastreabilidade e Auditoria: O sistema deve manter TODAS as ações executadas por todos os usuários a fim de permitir auditoria, logo, um registro de log de uso do sistema deverá ser mantido contendo minimamente: a) a ação executada, b) o usuário executor (caso autenticado) e c) data e hora da ação executada.   

Requisitos Não Funcionais:   

    Linguagem Java EE: O sistema deverá obrigatoriamente ser implementado em linguagem Java. É desejável que seja utilizada a versão 11 ou superior.   

    Modelo MVC: O sistema deverá obrigatoriamente utilizar o modelo de desenvolvimento em camadas MVC independente do framework utilizado.   

    JAX-RS: O sistema deverá obrigatoriamente utilizar a especificação JAX-RS para a implementação dos endpoints das requisições REST.   

    Quarkus: É desejável que o sistema utilize o framework Quarkus.   

    Utilização dos padrões DAO e Entity: Para cada entidade do seu sistema, deverá ser criada uma classe na camada de modelo que represente a estrutura de dados da entidade (Entity) e o respectivo objeto de acesso a dados (DAO) para manter e recuperar os dados da respectiva entidade.   

    Utilização do padrão BO: TODAS as regras de negócios deverão ser implementadas utilizando objetos Business Object (BO). Isso inclui também as validações de dados a serem persistidos.   

    Comunicação entre back-end e front-end exclusivamente por DTO: Por motivos de segurança, uma entidade nunca deve ser transitada na estrutura em que é persistida para o front-end. Logo, para cada interface de usuário (UI), os DTOs necessários para o funcionamento da respectiva UI deverão ser projetados para trafegar somente os dados necessários.   

Avaliação   

A avaliação do projeto prático será realizada subdividida em três avaliações:   

    Front-End (FE) - 2 pontos;   

    Back-end (BE) - 2 pontos;   

    Aplicação dos fundamentos (AF) - 2 pontos;   

    Fluxo completo dos casos de uso (FC) - 4 pontos; comunicação entre front-end, back-end e persistência   

    Apresentação e Arguição (AA) - 10 pontos   

        Apresentação coletiva   

        Pelo menos 3 perguntas por aluno com resposta individual   

Critérios de avaliação:   

    Corretude   

    Completude   

    Clareza   

Cálculo da nota final individual:   

    NF = (FE + BE + AF + FC) * (AA / 10)   

    Aprovação: NF >= 6.0   

Sobre a avaliação:   

    A data e horário limite para TÉRMINO entrega e apresentação será o penúltimo dia de aula às 22h.   

    Não serão aceitos trabalhos após essa data e horário limite.   

    Não serão aceitos apresentações após essa data e horário limite.   

    Não haverá possibilidade de reapresentação.   

    Ausência na avaliação Apresentação e Arguição implicará na reprovação automática (AA = 0).   

    Os códigos fontes do trabalho deverão ser entregues via repositório de código (Github, Gitlab, etc...)
