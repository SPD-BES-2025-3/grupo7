# Guia de Testes - API PetShop

Este documento descreve como executar os testes unitários e de integração da API PetShop.

## Estrutura de Testes

O projeto possui os seguintes tipos de testes:

### 1. Testes de Entidades (Model)
- **Localização**: `src/test/java/com/grupo7/api/model/`
- **Arquivos**: 
  - `ClienteTest.java`
  - `PetTest.java`
  - `ProdutoTest.java`
  - `VendaTest.java`
  - `AgendamentoTest.java`
  - `UsuarioTest.java`
- **Status**: ✅ Concluídos
- **Descrição**: Testam os getters, setters e construtores das entidades

### 2. Testes de Repositórios (Mockados)
- **Localização**: `src/test/java/com/grupo7/api/repository/`
- **Arquivos**:
  - `ClienteRepositoryTest.java`
  - `ProdutoRepositoryTest.java`
- **Status**: ✅ Implementados
- **Descrição**: Testam as operações de repositório usando mocks

### 3. Testes de Controladores
- **Localização**: `src/test/java/com/grupo7/api/controller/`
- **Arquivos**:
  - `ClienteControllerTest.java`
- **Status**: ✅ Implementados
- **Descrição**: Testam os endpoints REST usando MockMvc

### 4. Testes de Serviços
- **Localização**: `src/test/java/com/grupo7/api/service/`
- **Arquivos**:
  - `ClienteServiceTest.java`
- **Status**: ✅ Implementados
- **Descrição**: Testam a lógica de negócio usando mocks

### 5. Testes de Integração
- **Localização**: `src/test/java/com/grupo7/api/integration/`
- **Arquivos**:
  - `ClienteIntegrationTest.java`
- **Status**: ✅ Implementados
- **Descrição**: Testam a integração completa usando TestRestTemplate

## Pré-requisitos

### 1. Dependências
- Java 17 ou superior
- Maven 3.6 ou superior
- MongoDB rodando na porta 27017
- Redis rodando na porta 6379

### 2. Configuração do Ambiente

#### MongoDB
```bash
# Instalar MongoDB (Ubuntu/Debian)
sudo apt update
sudo apt install mongodb

# Iniciar MongoDB
sudo systemctl start mongodb
sudo systemctl enable mongodb

# Verificar status
sudo systemctl status mongodb
```

#### Redis
```bash
# Instalar Redis (Ubuntu/Debian)
sudo apt update
sudo apt install redis-server

# Iniciar Redis
sudo systemctl start redis-server
sudo systemctl enable redis-server

# Verificar status
sudo systemctl status redis-server
```

## Como Executar os Testes

### 1. Executar Todos os Testes
```bash
# Navegar para o diretório da API
cd api

# Executar todos os testes
mvn test
```

### 2. Executar Testes Específicos

#### Testes de Entidades
```bash
mvn test -Dtest="*Test" -Dtest=*Model*
```

#### Testes de Repositórios
```bash
mvn test -Dtest="*RepositoryTest"
```

#### Testes de Controladores
```bash
mvn test -Dtest="*ControllerTest"
```

#### Testes de Serviços
```bash
mvn test -Dtest="*ServiceTest"
```

#### Testes de Integração
```bash
mvn test -Dtest="*IntegrationTest"
```

### 3. Executar Testes com Cobertura
```bash
# Executar testes com relatório de cobertura
mvn clean test jacoco:report

# Abrir relatório de cobertura
open target/site/jacoco/index.html
```

### 4. Executar Testes em Modo Debug
```bash
# Executar com logs detalhados
mvn test -Dspring.profiles.active=test -Dlogging.level.com.grupo7.api=DEBUG
```

## Configurações de Teste

### Perfil de Teste
O projeto usa o perfil `test` que carrega as configurações de `application-test.properties`:

- **MongoDB**: Conecta ao banco `petshop_test`
- **Redis**: Usa database 1
- **Logging**: Nível DEBUG para debugging

### Configurações Específicas

#### application-test.properties
```properties
# MongoDB para testes
spring.data.mongodb.uri=mongodb://localhost:27017/petshop_test

# Redis para testes
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.database=1

# Logging para testes
logging.level.com.grupo7.api=DEBUG
```

## Estrutura dos Testes

### Padrão AAA (Arrange-Act-Assert)

Todos os testes seguem o padrão AAA:

```java
@Test
void testExample() {
    // Arrange (Given)
    Cliente cliente = new Cliente("João", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
    when(repository.save(any())).thenReturn(cliente);
    
    // Act (When)
    Cliente result = service.save(cliente);
    
    // Assert (Then)
    assertNotNull(result);
    assertEquals("João", result.getNome());
    verify(repository, times(1)).save(cliente);
}
```

### Anotações Utilizadas

#### Testes Unitários
- `@ExtendWith(MockitoExtension.class)`: Habilita Mockito
- `@Mock`: Cria mocks
- `@InjectMocks`: Injeta mocks na classe testada
- `@BeforeEach`: Setup executado antes de cada teste

#### Testes de Integração
- `@SpringBootTest`: Carrega o contexto Spring completo
- `@AutoConfigureWebMvc`: Configura MockMvc
- `@ActiveProfiles("test")`: Usa o perfil de teste
- `@LocalServerPort`: Injeta a porta do servidor

## Exemplos de Testes

### Teste de Repositório (Mockado)
```java
@Test
void testFindById() {
    // Given
    when(clienteRepository.findById("1")).thenReturn(Optional.of(cliente1));
    
    // When
    Optional<Cliente> result = clienteService.findById("1");
    
    // Then
    assertTrue(result.isPresent());
    assertEquals("João Silva", result.get().getNome());
    verify(clienteRepository, times(1)).findById("1");
}
```

### Teste de Controlador
```java
@Test
void testGetClienteById() {
    // Given
    when(clienteService.findById("1")).thenReturn(Optional.of(cliente1));
    
    // When
    ResponseEntity<Cliente> response = clienteController.getClienteById("1");
    
    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("João Silva", response.getBody().getNome());
}
```

### Teste de Integração
```java
@Test
void testCreateAndRetrieveCliente() {
    // Given
    Cliente cliente = new Cliente("João", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
    
    // When - Create
    ResponseEntity<Cliente> createResponse = restTemplate.postForEntity(baseUrl, cliente, Cliente.class);
    
    // Then - Create
    assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
    
    // When - Retrieve
    ResponseEntity<Cliente> getResponse = restTemplate.getForEntity(
        baseUrl + "/" + createResponse.getBody().getId(), Cliente.class);
    
    // Then - Retrieve
    assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    assertEquals("João", getResponse.getBody().getNome());
}
```

## Troubleshooting

### Problemas Comuns

#### 1. MongoDB não está rodando
```bash
# Verificar status
sudo systemctl status mongodb

# Iniciar se necessário
sudo systemctl start mongodb
```

#### 2. Redis não está rodando
```bash
# Verificar status
sudo systemctl status redis-server

# Iniciar se necessário
sudo systemctl start redis-server
```

#### 3. Porta já em uso
```bash
# Verificar portas em uso
sudo netstat -tlnp | grep :27017
sudo netstat -tlnp | grep :6379

# Matar processo se necessário
sudo kill -9 <PID>
```

#### 4. Testes falhando por timeout
```bash
# Executar com timeout maior
mvn test -Dspring.test.timeout=30000
```

### Logs de Debug

Para ver logs detalhados durante os testes:

```bash
mvn test -Dlogging.level.com.grupo7.api=DEBUG -Dlogging.level.org.springframework.data.mongodb=DEBUG
```

## Relatórios de Teste

### Relatório Maven Surefire
Após executar os testes, o relatório estará em:
```
target/surefire-reports/
```

### Relatório de Cobertura (JaCoCo)
Para gerar relatório de cobertura:
```bash
mvn clean test jacoco:report
```

O relatório estará em:
```
target/site/jacoco/index.html
```

## Próximos Passos

### Testes Pendentes
- [ ] Implementar testes para outros repositórios (Pet, Venda, Agendamento, etc.)
- [ ] Implementar testes para outros controladores
- [ ] Implementar testes para outros serviços
- [ ] Implementar testes de integração para outras entidades

### Melhorias Sugeridas
- [ ] Adicionar testes de performance
- [ ] Implementar testes de segurança
- [ ] Adicionar testes de validação de dados
- [ ] Implementar testes de cenários de erro

## Contato

Para dúvidas sobre os testes, consulte a documentação do projeto ou entre em contato com a equipe de desenvolvimento. 