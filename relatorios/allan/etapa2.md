# Relatório Etapa 2

## Integração da API com o MongoDB

Nesta etapa do projeto, foi realizada a integração da API com o banco de dados MongoDB, adotando uma abordagem orientada a documentos para o armazenamento das informações.

### Configuração

A configuração do acesso ao MongoDB foi feita utilizando o Spring Data MongoDB. Foram definidos os parâmetros de conexão no arquivo `application.yml`, incluindo host, porta, nome do banco de dados, usuário e senha, além do banco de autenticação quando necessário.

### Repositórios e ODM

Foram criados repositórios específicos para cada entidade do sistema, todos estendendo a interface `MongoRepository` do Spring Data. Isso permitiu a utilização de métodos prontos para operações CRUD e a definição de consultas customizadas utilizando a anotação `@Query`.

### Entidades

As entidades do sistema foram modeladas como documentos, utilizando anotações do Spring Data MongoDB para mapear os campos e coleções. Cada entidade possui um identificador único e os relacionamentos foram representados de acordo com as necessidades do domínio.

### Boas práticas

- Separação clara entre camadas (controller, service, repository)
- Utilização de DTOs e validação de dados
- Configuração de índices automáticos para melhorar a performance das consultas
- Definição de variáveis de ambiente para facilitar a configuração em diferentes ambientes

A integração com o MongoDB proporcionou maior flexibilidade no armazenamento dos dados e facilitou a evolução do modelo de dados da aplicação.
