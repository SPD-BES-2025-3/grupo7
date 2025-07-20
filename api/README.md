# API RESTful para Petshop - Spring Boot e MongoDB

Esta é uma API RESTful desenvolvida com Spring Boot e MongoDB para gerenciamento de petshop.

## 🏪 Funcionalidades do Petshop

### **Gestão de Clientes**
- Cadastro completo de clientes com validações
- Busca por nome, email, CPF e telefone
- Controle de status ativo/inativo
- Relacionamento com pets

### **Gestão de Pets**
- Cadastro de pets vinculados aos clientes
- Informações completas (espécie, raça, peso, etc.)
- Busca por nome, raça e espécie
- Controle de status ativo/inativo

### **Gestão de Produtos**
- Cadastro de produtos com categorias
- Controle de estoque automático
- Busca por nome, categoria, marca
- Alertas de estoque baixo
- Gestão de preços

### **Sistema de Agendamentos**
- Agendamento de serviços (banho, tosa, consulta)
- Controle de status (agendado, confirmado, realizado, cancelado)
- Verificação de conflitos de horário
- Relacionamento com clientes, pets e funcionários
- Filtros por período, status e serviço

### **Sistema de Vendas**
- Venda de produtos com múltiplos itens
- Cálculo automático de totais
- Controle de estoque na finalização
- Múltiplas formas de pagamento
- Relatórios de vendas por período

## 🛠️ Tecnologias Utilizadas

- **Spring Boot 3.2.0**
- **Spring Data MongoDB**
- **MongoDB**
- **Java 17**
- **Maven**
- **Bean Validation**
- **Spring Boot Actuator**

## 📁 Estrutura do Projeto

```
src/main/java/com/grupo7/api/
├── ApiApplication.java              # Classe principal
├── config/
│   └── MongoConfig.java            # Configuração MongoDB
├── controller/
│   ├── ClienteController.java      # Gestão de clientes
│   ├── PetController.java          # Gestão de pets
│   ├── ProdutoController.java      # Gestão de produtos
│   ├── AgendamentoController.java  # Gestão de agendamentos
│   ├── VendaController.java        # Gestão de vendas
│   ├── UsuarioController.java      # Gestão de usuários
│   └── HealthController.java       # Saúde da API
├── exception/
│   └── GlobalExceptionHandler.java # Tratamento de erros
├── model/
│   ├── BaseEntity.java             # Classe base
│   ├── Cliente.java               # Entidade Cliente
│   ├── Pet.java                   # Entidade Pet
│   ├── Produto.java               # Entidade Produto
│   ├── Agendamento.java           # Entidade Agendamento
│   ├── Venda.java                 # Entidade Venda
│   └── Usuario.java               # Entidade Usuario
├── repository/
│   ├── ClienteRepository.java     # Repositório Cliente
│   ├── PetRepository.java         # Repositório Pet
│   ├── ProdutoRepository.java     # Repositório Produto
│   ├── AgendamentoRepository.java # Repositório Agendamento
│   ├── VendaRepository.java       # Repositório Venda
│   └── UsuarioRepository.java     # Repositório Usuario
└── service/
    ├── ClienteService.java        # Serviço Cliente
    ├── PetService.java            # Serviço Pet
    ├── ProdutoService.java        # Serviço Produto
    ├── AgendamentoService.java    # Serviço Agendamento
    ├── VendaService.java          # Serviço Venda
    └── UsuarioService.java        # Serviço Usuario
```

## 🚀 Configuração

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+
- MongoDB 4.4+

### Executando a Aplicação
```bash
cd api
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080/api`

## 📋 Endpoints Principais

### **Clientes** (`/api/clientes`)
- `GET /` - Listar todos os clientes
- `GET /{id}` - Buscar cliente por ID
- `GET /email/{email}` - Buscar por email
- `GET /cpf/{cpf}` - Buscar por CPF
- `GET /ativos` - Listar clientes ativos
- `GET /busca/geral?termo={termo}` - Busca geral
- `POST /` - Criar cliente
- `PUT /{id}` - Atualizar cliente
- `DELETE /{id}` - Deletar cliente
- `PATCH /{id}/status?ativo={boolean}` - Alterar status

### **Pets** (`/api/pets`)
- `GET /` - Listar todos os pets
- `GET /{id}` - Buscar pet por ID
- `GET /cliente/{clienteId}` - Pets de um cliente
- `GET /especie/{especie}` - Pets por espécie
- `GET /raca/{raca}` - Pets por raça
- `GET /ativos` - Listar pets ativos
- `GET /busca/geral?termo={termo}` - Busca geral
- `POST /` - Criar pet
- `PUT /{id}` - Atualizar pet
- `DELETE /{id}` - Deletar pet
- `PATCH /{id}/peso?peso={peso}` - Atualizar peso

### **Produtos** (`/api/produtos`)
- `GET /` - Listar todos os produtos
- `GET /{id}` - Buscar produto por ID
- `GET /codigo/{codigo}` - Buscar por código
- `GET /ativos` - Listar produtos ativos
- `GET /categoria/{categoria}` - Produtos por categoria
- `GET /estoque-baixo?limite={limite}` - Produtos com estoque baixo
- `GET /preco?precoMin={min}&precoMax={max}` - Produtos por faixa de preço
- `GET /busca/geral?termo={termo}` - Busca geral
- `POST /` - Criar produto
- `PUT /{id}` - Atualizar produto
- `DELETE /{id}` - Deletar produto
- `PATCH /{id}/estoque?quantidade={qtd}` - Atualizar estoque
- `PATCH /{id}/preco?preco={preco}` - Atualizar preço

### **Agendamentos** (`/api/agendamentos`)
- `GET /` - Listar todos os agendamentos
- `GET /{id}` - Buscar agendamento por ID
- `GET /cliente/{clienteId}` - Agendamentos de um cliente
- `GET /pet/{petId}` - Agendamentos de um pet
- `GET /hoje` - Agendamentos de hoje
- `GET /semana` - Agendamentos da semana
- `GET /pendentes` - Agendamentos pendentes
- `GET /confirmados` - Agendamentos confirmados
- `GET /realizados` - Agendamentos realizados
- `GET /cancelados` - Agendamentos cancelados
- `GET /servico/{servico}` - Agendamentos por serviço
- `POST /` - Criar agendamento
- `PUT /{id}` - Atualizar agendamento
- `DELETE /{id}` - Deletar agendamento
- `PATCH /{id}/confirmar` - Confirmar agendamento
- `PATCH /{id}/realizar` - Realizar agendamento
- `PATCH /{id}/cancelar` - Cancelar agendamento

### **Vendas** (`/api/vendas`)
- `GET /` - Listar todas as vendas
- `GET /{id}` - Buscar venda por ID
- `GET /cliente/{clienteId}` - Vendas de um cliente
- `GET /hoje` - Vendas de hoje
- `GET /semana` - Vendas da semana
- `GET /mes` - Vendas do mês
- `GET /pendentes` - Vendas pendentes
- `GET /pagas` - Vendas pagas
- `GET /total/hoje` - Total de vendas hoje
- `GET /total/semana` - Total de vendas da semana
- `GET /total/mes` - Total de vendas do mês
- `POST /` - Criar venda
- `PUT /{id}` - Atualizar venda
- `DELETE /{id}` - Deletar venda
- `PATCH /{id}/finalizar?formaPagamento={forma}` - Finalizar venda
- `PATCH /{id}/cancelar` - Cancelar venda

## 💡 Exemplos de Uso

### Criar um Cliente
```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "telefone": "(11) 99999-9999",
    "cpf": "123.456.789-00",
    "endereco": "Rua das Flores, 123"
  }'
```

### Criar um Pet
```bash
curl -X POST http://localhost:8080/api/pets \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Rex",
    "especie": "Cachorro",
    "raca": "Golden Retriever",
    "dataNascimento": "2020-01-15",
    "clienteId": "cliente_id_aqui"
  }'
```

### Criar um Agendamento
```bash
curl -X POST http://localhost:8080/api/agendamentos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": "cliente_id_aqui",
    "petId": "pet_id_aqui",
    "servico": "Banho e Tosa",
    "dataHora": "2024-01-15T14:00:00",
    "observacoes": "Pet muito agitado"
  }'
```

### Criar uma Venda
```bash
curl -X POST http://localhost:8080/api/vendas \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": "cliente_id_aqui",
    "itens": [
      {
        "produtoId": "produto_id_aqui",
        "quantidade": 2,
        "precoUnitario": 25.50
      }
    ]
  }'
```

## 🔧 Funcionalidades Especiais

### **Controle de Estoque**
- Atualização automática do estoque na finalização de vendas
- Alertas de produtos com estoque baixo
- Validação de disponibilidade antes da venda

### **Gestão de Agendamentos**
- Verificação de conflitos de horário
- Controle de status (agendado → confirmado → realizado)
- Filtros por período e serviço

### **Relatórios de Vendas**
- Total de vendas por período (hoje, semana, mês)
- Vendas por cliente
- Vendas por forma de pagamento

### **Validações**
- Validação de CPF e email únicos
- Validação de códigos de produto únicos
- Validação de datas futuras para agendamentos
- Validação de estoque suficiente

## 📊 Monitoramento

Endpoints do Spring Boot Actuator:
- `GET /api/actuator/health` - Saúde da aplicação
- `GET /api/actuator/info` - Informações da aplicação
- `GET /api/actuator/metrics` - Métricas da aplicação

## 🎯 Próximos Passos

- Implementar autenticação e autorização
- Adicionar relatórios mais detalhados
- Implementar notificações
- Adicionar upload de imagens
- Implementar dashboard administrativo 