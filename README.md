# 🐾 PetShop Manager — Grupo 7

Projeto interdisciplinar que visa desenvolver um sistema de gerenciamento para pet shops, com funcionalidades que abrangem desde o cadastro de clientes e pets até a realização de agendamentos e vendas. A aplicação é dividida em duas camadas principais: uma API REST desenvolvida em Spring Boot e uma interface desktop feita com JavaFX.

---

## 📌 Seções do Projeto

### 1. Introdução

A gestão de pet shops envolve múltiplas operações como controle de clientes, agendamentos, vendas de produtos e serviços. Muitos estabelecimentos ainda realizam essas tarefas de forma manual, o que gera retrabalho e perda de dados. Este projeto visa digitalizar esse processo por meio de um sistema de fácil uso e integração entre frontend e backend.

### 2. Objetivo

#### 🎯 Objetivo Geral

Desenvolver uma aplicação para gerenciamento de pet shops que integre frontend desktop com backend via API REST, utilizando banco de dados MongoDB.

#### 🎯 Objetivos Específicos

- Permitir o cadastro de clientes, pets, produtos, vendas e agendamentos.
- Construir uma API REST estruturada com Spring Boot e MongoDB.
- Criar uma interface JavaFX conectada à API.
- Organizar o projeto com boas práticas de documentação, testes e modelagem UML.

### 3. Ferramentas Utilizadas

- **Java 17**
- **JavaFX** + SceneBuilder
- **Spring Boot**
- **MongoDB (local/Atlas)**
- **ORMLite** (camada desktop)
- **Maven**
- **Git/GitHub**
- **Draw.io / StarUML** (modelagem UML)
- **Postman / Insomnia** (testes de API)
- **JUnit** (testes unitários)

---

## 👥 Divisão de Tarefas

```markdown
| Integrante  | Responsabilidades                                                                 |
|-------------|-----------------------------------------------------------------------------------|
| Ana Liz     | Documentação, planejamento, README, organização do repositório                    |
| Pedro       | Modelagem UML (classes, componentes, sequência)                                   |
| Mário       | Aplicação desktop (JavaFX), entidades e views básicas, ORMLite, JavaDocs          |
| Allan       | API REST com Spring Boot, conexão com MongoDB, endpoints iniciais (Pet e Cliente) |
| Paulo       | Testes unitários e integração, documentação de testes                             |
```

---

## ⚙️ Instalação

1. **Clone o repositório:**

2. **Configure o MongoDB:**

3. **Importe o projeto:**

---

## ▶️ Execução

### 🔹 Backend (API REST)

1. Acesse a pasta `api/`.
2. Execute a classe `ApiApplication.java`.
3. A API estará disponível em `http://localhost:8080`.

### 🔹 Frontend (JavaFX)

1. Acesse a pasta `front/`.
2. Execute a classe `App.java`.
3. As telas JavaFX se comunicarão com a API REST via `ApiService`.
4. Apenas para testar, pode utilizar o comando `mvn exec:java -Dexec.mainClass="com.grupo7.petshop.model.TestORMLite"`

---

## 🧪 Testes

### API



### Unitários



### Executar testes:



---

## 📁 Estrutura de Diretórios

```
grupo7/
├── api/                       # API REST (Spring Boot + MongoDB)
│   └── src/main/java/...      # Controllers, Models, Repositories
│   └── src/test/java/...      # Testes
├── src/                       # Frontend Desktop (JavaFX)
│   └── main/java/...          # Views, Controllers, Services
├── docs/
│   ├── planejamento.md        # Planejamento do projeto
│   └── modelagem/             # Diagramas UML (classes, sequência, componentes)
├── README.md                  # Este arquivo
└── relatorio_pessoal.md       # Relatório de participação individual
```

---

## 📡 Documentação da API

Para detalhes técnicos, endpoints e exemplos de uso, consulte a [documentação da API](api/README.md).

---

## 📌 Status Atual (21/07)

- [x] Planejamento documentado 
- [x] Estrutura da API iniciada 
- [x] Views e entidades iniciais no JavaFX 
- [ ] Diagramas UML em andamento 
- [x] Testes unitários em rascunho 

---

## 📚 Referências

- [Documentação Spring Boot](https://spring.io/projects/spring-boot)
- [Documentação JavaFX](https://openjfx.io/)
- [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
- [ORMLite](https://ormlite.com/)
- [Draw.io](https://app.diagrams.net/)
- [JUnit](https://junit.org/junit5/)
