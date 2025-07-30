# Guia de Testes - API PetShop

Este documento descreve como executar os testes unitários e de integração da API PetShop.

## 📋 Visão Geral

A API possui uma cobertura completa de testes implementada com:
- **JUnit 5** para testes unitários
- **Mockito** para mock de dependências
- **Spring Boot Test** para testes de integração
- **TestRestTemplate** para testes de API
- **JaCoCo** para relatórios de cobertura

## 🏗️ Estrutura dos Testes

### Testes Unitários (Mockados)

#### 1. Testes de Modelo (Entidades)
- ✅ `AgendamentoTest.java`
- ✅ `ClienteTest.java`
- ✅ `PetTest.java`
- ✅ `ProdutoTest.java`
- ✅ `UsuarioTest.java`
- ✅ `VendaTest.java`

#### 2. Testes de Repositório
- ✅ `AgendamentoRepositoryTest.java`
- ✅ `ClienteRepositoryTest.java`
- ✅ `PetRepositoryTest.java`
- ✅ `ProdutoRepositoryTest.java`
- ✅ `UsuarioRepositoryTest.java`
- ✅ `VendaRepositoryTest.java`

#### 3. Testes de Serviço
- ✅ `AgendamentoServiceTest.java`
- ✅ `ClienteServiceTest.java`
- ✅ `PetServiceTest.java`
- ✅ `ProdutoServiceTest.java`
- ✅ `UsuarioServiceTest.java`
- ✅ `VendaServiceTest.java`

#### 4. Testes de Controlador
- ✅ `AgendamentoControllerTest.java`
- ✅ `ClienteControllerTest.java`
- ✅ `PetControllerTest.java`
- ✅ `ProdutoControllerTest.java`
- ✅ `UsuarioControllerTest.java`
- ✅ `VendaControllerTest.java`

### Testes de Integração

#### 5. Testes de Integração (TestRestTemplate)
- ✅ `AgendamentoIntegrationTest.java`
- ✅ `ClienteIntegrationTest.java`
- ✅ `PetIntegrationTest.java`
- ✅ `ProdutoIntegrationTest.java`
- ✅ `UsuarioIntegrationTest.java`
- ✅ `VendaIntegrationTest.java`

## 🚀 Como Executar os Testes

### Pré-requisitos

1. **Java 17** instalado
2. **Maven** instalado
3. **MongoDB** rodando (para testes de integração)
4. **Redis** rodando (para testes de integração)

### Verificar Serviços

```bash
# Verificar se MongoDB está rodando
sudo systemctl status mongodb

# Verificar se Redis está rodando
sudo systemctl status redis-server

# Iniciar serviços se necessário
sudo systemctl start mongodb
sudo systemctl start redis-server
```

### Executar Testes

#### Opção 1: Usando o Script Automatizado

```bash
# Executar todos os testes
./run-tests.sh all

# Executar apenas testes unitários
./run-tests.sh unit

# Executar apenas testes de integração
./run-tests.sh integration

# Executar testes com relatório de cobertura
./run-tests.sh coverage

# Executar testes em modo debug
./run-tests.sh debug

# Executar testes rápidos (sem integração)
./run-tests.sh quick
```

#### Opção 2: Usando Maven Diretamente

```bash
# Configurar Java 17
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

# Executar todos os testes
mvn clean test

# Executar apenas testes unitários
mvn test -Dtest="*Test" -Dtest=*Model*,*RepositoryTest,*ControllerTest,*ServiceTest

# Executar apenas testes de integração
mvn test -Dtest="*IntegrationTest"

# Executar testes com cobertura
mvn clean test jacoco:report

# Executar testes em modo debug
mvn test -Dspring.profiles.active=test -Dlogging.level.com.grupo7.api=DEBUG
```

## 📊 Relatórios de Cobertura

Após executar os testes com cobertura, o relatório JaCoCo estará disponível em:
```
target/site/jacoco/index.html
```

Para abrir o relatório:
```bash
# No Linux
xdg-open target/site/jacoco/index.html

# Ou abrir manualmente no navegador
```

## 🧪 Tipos de Teste

### Testes Unitários

**Características:**
- Testam componentes isoladamente
- Usam mocks para dependências externas
- Execução rápida
- Não requerem banco de dados


### Testes de Integração

**Características:**
- Testam a integração entre componentes
- Usam banco de dados real (MongoDB)
- Testam endpoints da API
- Execução mais lenta


## 🔧 Configuração de Teste

### Arquivo de Configuração
- `application-test.properties` - Configurações específicas para testes

### Configurações Principais:
```properties
# MongoDB para testes
spring.data.mongodb.uri=mongodb://localhost:27017/petshop_test

# Redis para testes
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.database=1

# Logging para debug
logging.level.com.grupo7.api=DEBUG
```

## 🐛 Troubleshooting

### Problemas Comuns

#### 1. Erro de Java Version
```
release version 17 not supported
```
**Solução:**
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
mvn clean test
```

#### 2. MongoDB não está rodando
```
Connection refused
```
**Solução:**
```bash
sudo systemctl start mongodb
```

#### 3. Redis não está rodando
```
Connection refused
```
**Solução:**
```bash
sudo systemctl start redis-server
```

#### 4. Porta já em uso
```
Web server failed to start. Port 8080 was already in use.
```
**Solução:**
```bash
# Encontrar processo usando a porta
lsof -i :8080
# Matar o processo
kill -9 <PID>
```

### Logs de Debug

Para ver logs detalhados durante os testes:
```bash
mvn test -Dlogging.level.com.grupo7.api=DEBUG
```

## 📈 Métricas de Cobertura

### Cobertura Esperada
- **Linhas de código:** > 80%
- **Branches:** > 70%
- **Métodos:** > 90%
- **Classes:** > 95%

### Verificar Cobertura
```bash
# Executar testes com cobertura
mvn clean test jacoco:report

# Verificar relatório
open target/site/jacoco/index.html
```

## 🎯 Cenários de Teste

### Testes de Sucesso
- ✅ Criação de entidades
- ✅ Busca por ID
- ✅ Listagem de todos os registros
- ✅ Atualização de dados
- ✅ Exclusão de registros
- ✅ Alteração de status

### Testes de Erro
- ✅ Dados inválidos
- ✅ Registros não encontrados
- ✅ Validações de negócio
- ✅ Conflitos de dados únicos

### Testes de Integração
- ✅ Fluxo completo CRUD
- ✅ Endpoints da API
- ✅ Respostas HTTP corretas
- ✅ Validação de JSON

## 📝 Convenções de Nomenclatura

### Arquivos de Teste
- `*Test.java` - Testes unitários
- `*IntegrationTest.java` - Testes de integração

### Métodos de Teste
- `test[Metodo]_[Cenario]` - Ex: `testFindById_Success`
- `test[Metodo]_[Erro]` - Ex: `testFindById_NotFound`

### Organização
- Um arquivo de teste por classe
- Métodos organizados por funcionalidade
- Setup comum no `@BeforeEach`

## 🔄 CI/CD

### Pipeline de Teste
```yaml
# Exemplo para GitHub Actions
- name: Run Tests
  run: |
    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
    mvn clean test jacoco:report

- name: Upload Coverage
  uses: codecov/codecov-action@v3
  with:
    file: ./target/site/jacoco/jacoco.xml
```

## 📚 Recursos Adicionais

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Test Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)

---

**Total de Testes Implementados: 30 arquivos**
- 6 testes de modelo
- 6 testes de repositório
- 6 testes de serviço
- 6 testes de controlador
- 6 testes de integração 