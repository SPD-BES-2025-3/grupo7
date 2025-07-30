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
2. Executar da seguinte forma:
`mvn clean compile`
`mvn exec:java -Dexec.mainClass="com.grupo7.petshop.App"`
---

## ✅ Execução de Testes

Este projeto utiliza *Maven* para gerenciamento de dependências e execução dos testes automatizados (JUnit).

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- (Linux/macOS): permissões de execução no script run-tests.sh

### 🔧 Executar todos os testes (modo automático)

Navegue até o diretório api e execute o script de testes:

```bash
cd api
chmod +x run-tests.sh  # apenas uma vez, se necessário
./run-tests.sh
```


---

## 📁 Estrutura de Diretórios

grupo7/
├── api/          # Backend Spring Boot
├── front/        # Aplicação JavaFX com SQLite
├── sqlite/       # Microserviço com MongoDB e Redis
├── docs/         # Documentação e diagramas
├── relatorios/   # Relatórios individuais

---

## 📡 Documentação da API

Para detalhes técnicos, endpoints e exemplos de uso, consulte a [documentação da API](api/README.md).

---

## 📌 Status (21/07)

- [x] Planejamento documentado 
- [x] Estrutura da API iniciada 
- [x] Views e entidades iniciais no JavaFX 
- [ ] Diagramas UML em andamento 
- [x] Testes unitários em rascunho 

---

## 📈 Avanços Pós-Entrega Inicial

Desde a entrega anterior, realizamos os seguintes progressos:

- Finalização das views JavaFX com navegação e controles completos
- Integração estável entre frontend local e backend remoto via HTTP
- Estrutura de eventos com Redis para propagação entre microsserviços
- Containerização com Docker (backend + MongoDB + Redis)
- Repositórios e serviços completos com cobertura para todas as entidades
- Documentação ampliada: diagramas de classe, componentes e sequência

---

## 📚 Referências

- [Documentação Spring Boot](https://spring.io/projects/spring-boot)
- [Documentação JavaFX](https://openjfx.io/)
- [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
- [ORMLite](https://ormlite.com/)
- [Draw.io](https://app.diagrams.net/)
- [JUnit](https://junit.org/junit5/)
