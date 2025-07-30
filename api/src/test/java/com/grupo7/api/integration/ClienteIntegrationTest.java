package com.grupo7.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo7.api.model.Cliente;
import com.grupo7.api.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@ActiveProfiles("test")
public class ClienteIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        baseUrl = "http://localhost:" + port + "/clientes";
        
        // Limpar dados de teste
        clienteRepository.deleteAll();
    }

    @Test
    void testCreateAndRetrieveCliente() throws Exception {
        // Given
        Cliente cliente = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        cliente.setEndereco("Rua A, 123");
        cliente.setObservacoes("Cliente VIP");

        // When - Create
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Cliente> request = new HttpEntity<>(cliente, headers);
        
        ResponseEntity<Cliente> createResponse = restTemplate.exchange(
            baseUrl, HttpMethod.POST, request, Cliente.class);

        // Then - Create
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getId());
        assertEquals("João Silva", createResponse.getBody().getNome());
        assertEquals("joao@email.com", createResponse.getBody().getEmail());

        String clienteId = createResponse.getBody().getId();

        // When - Retrieve
        ResponseEntity<Cliente> getResponse = restTemplate.getForEntity(
            baseUrl + "/" + clienteId, Cliente.class);

        // Then - Retrieve
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("João Silva", getResponse.getBody().getNome());
        assertEquals("joao@email.com", getResponse.getBody().getEmail());
        assertEquals("(11) 99999-9999", getResponse.getBody().getTelefone());
        assertEquals("123.456.789-00", getResponse.getBody().getCpf());
    }

    @Test
    void testGetAllClientes() throws Exception {
        // Given
        Cliente cliente1 = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        Cliente cliente2 = new Cliente("Maria Santos", "maria@email.com", "(11) 88888-8888", "987.654.321-00");
        
        clienteRepository.save(cliente1);
        clienteRepository.save(cliente2);

        // When
        ResponseEntity<Cliente[]> response = restTemplate.getForEntity(baseUrl, Cliente[].class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
        
        // Verificar se os clientes estão na resposta
        boolean foundJoao = false;
        boolean foundMaria = false;
        for (Cliente c : response.getBody()) {
            if (c.getNome().equals("João Silva")) foundJoao = true;
            if (c.getNome().equals("Maria Santos")) foundMaria = true;
        }
        assertTrue(foundJoao);
        assertTrue(foundMaria);
    }

    @Test
    void testGetClienteByEmail() throws Exception {
        // Given
        Cliente cliente = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        clienteRepository.save(cliente);

        // When
        ResponseEntity<Cliente> response = restTemplate.getForEntity(
            baseUrl + "/email/joao@email.com", Cliente.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("joao@email.com", response.getBody().getEmail());
        assertEquals("João Silva", response.getBody().getNome());
    }

    @Test
    void testGetClienteByCpf() throws Exception {
        // Given
        Cliente cliente = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        clienteRepository.save(cliente);

        // When
        ResponseEntity<Cliente> response = restTemplate.getForEntity(
            baseUrl + "/cpf/123.456.789-00", Cliente.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("123.456.789-00", response.getBody().getCpf());
        assertEquals("João Silva", response.getBody().getNome());
    }

    @Test
    void testGetClientesAtivos() throws Exception {
        // Given
        Cliente cliente1 = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        cliente1.setAtivo(true);
        Cliente cliente2 = new Cliente("Maria Santos", "maria@email.com", "(11) 88888-8888", "987.654.321-00");
        cliente2.setAtivo(false);
        
        clienteRepository.save(cliente1);
        clienteRepository.save(cliente2);

        // When
        ResponseEntity<Cliente[]> response = restTemplate.getForEntity(baseUrl + "/ativos", Cliente[].class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
        assertTrue(response.getBody()[0].isAtivo());
        assertEquals("João Silva", response.getBody()[0].getNome());
    }

    @Test
    void testBuscarPorNome() throws Exception {
        // Given
        Cliente cliente1 = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        Cliente cliente2 = new Cliente("João Santos", "joao.santos@email.com", "(11) 77777-7777", "111.222.333-44");
        Cliente cliente3 = new Cliente("Maria Silva", "maria@email.com", "(11) 88888-8888", "987.654.321-00");
        
        clienteRepository.save(cliente1);
        clienteRepository.save(cliente2);
        clienteRepository.save(cliente3);

        // When
        ResponseEntity<Cliente[]> response = restTemplate.getForEntity(
            baseUrl + "/busca?nome=João", Cliente[].class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
        
        // Verificar se ambos os Joões estão na resposta
        boolean foundJoaoSilva = false;
        boolean foundJoaoSantos = false;
        for (Cliente c : response.getBody()) {
            if (c.getNome().equals("João Silva")) foundJoaoSilva = true;
            if (c.getNome().equals("João Santos")) foundJoaoSantos = true;
        }
        assertTrue(foundJoaoSilva);
        assertTrue(foundJoaoSantos);
    }

    @Test
    void testUpdateCliente() throws Exception {
        // Given
        Cliente cliente = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        cliente = clienteRepository.save(cliente);
        
        Cliente clienteAtualizado = new Cliente("João Silva Atualizado", "joao.novo@email.com", "(11) 88888-8888", "123.456.789-00");
        clienteAtualizado.setEndereco("Nova Rua, 456");

        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Cliente> request = new HttpEntity<>(clienteAtualizado, headers);
        
        ResponseEntity<Cliente> response = restTemplate.exchange(
            baseUrl + "/" + cliente.getId(), HttpMethod.PUT, request, Cliente.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João Silva Atualizado", response.getBody().getNome());
        assertEquals("joao.novo@email.com", response.getBody().getEmail());
        assertEquals("(11) 88888-8888", response.getBody().getTelefone());
        assertEquals("Nova Rua, 456", response.getBody().getEndereco());
    }

    @Test
    void testDeleteCliente() throws Exception {
        // Given
        Cliente cliente = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        cliente = clienteRepository.save(cliente);

        // When
        ResponseEntity<Void> response = restTemplate.exchange(
            baseUrl + "/" + cliente.getId(), HttpMethod.DELETE, null, Void.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        
        // Verificar se foi realmente deletado
        ResponseEntity<Cliente> getResponse = restTemplate.getForEntity(
            baseUrl + "/" + cliente.getId(), Cliente.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testAlterarStatusCliente() throws Exception {
        // Given
        Cliente cliente = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        cliente.setAtivo(true);
        cliente = clienteRepository.save(cliente);

        // When
        ResponseEntity<Cliente> response = restTemplate.exchange(
            baseUrl + "/" + cliente.getId() + "/status?ativo=false", 
            HttpMethod.PATCH, null, Cliente.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isAtivo());
        
        // Verificar se o status foi realmente alterado no banco
        Cliente clienteAtualizado = clienteRepository.findById(cliente.getId()).orElse(null);
        assertNotNull(clienteAtualizado);
        assertFalse(clienteAtualizado.isAtivo());
    }

    @Test
    void testCreateClienteWithInvalidData() throws Exception {
        // Given
        Cliente clienteInvalido = new Cliente("", "", "", "");

        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Cliente> request = new HttpEntity<>(clienteInvalido, headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl, HttpMethod.POST, request, String.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetClienteNotFound() throws Exception {
        // When
        ResponseEntity<Cliente> response = restTemplate.getForEntity(
            baseUrl + "/999", Cliente.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateClienteWithDuplicateEmail() throws Exception {
        // Given
        Cliente cliente1 = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        clienteRepository.save(cliente1);
        
        Cliente cliente2 = new Cliente("Maria Santos", "joao@email.com", "(11) 88888-8888", "987.654.321-00");

        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Cliente> request = new HttpEntity<>(cliente2, headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl, HttpMethod.POST, request, String.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCreateClienteWithDuplicateCpf() throws Exception {
        // Given
        Cliente cliente1 = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        clienteRepository.save(cliente1);
        
        Cliente cliente2 = new Cliente("Maria Santos", "maria@email.com", "(11) 88888-8888", "123.456.789-00");

        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Cliente> request = new HttpEntity<>(cliente2, headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl, HttpMethod.POST, request, String.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
} 