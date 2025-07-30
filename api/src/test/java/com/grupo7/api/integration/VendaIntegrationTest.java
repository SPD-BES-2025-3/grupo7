package com.grupo7.api.integration;

import com.grupo7.api.model.Venda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class VendaIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private Venda venda;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/vendas";
        
        venda = new Venda();
        venda.setClienteId("cli1");
        venda.setDataPagamento(LocalDateTime.now());
        venda.setTotal(new BigDecimal("100.00"));
        venda.setStatus("PENDENTE");
    }

    @Test
    void testCreateAndRetrieveVenda() {
        // Create venda
        ResponseEntity<Venda> createResponse = restTemplate.postForEntity(baseUrl, venda, Venda.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getId());
        assertEquals("cli1", createResponse.getBody().getClienteId());

        String vendaId = createResponse.getBody().getId();

        // Retrieve venda
        ResponseEntity<Venda> getResponse = restTemplate.getForEntity(baseUrl + "/" + vendaId, Venda.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("cli1", getResponse.getBody().getClienteId());
    }

    @Test
    void testGetAllVendas() {
        ResponseEntity<List<Venda>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Venda>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetVendasByClienteId() {
        ResponseEntity<List<Venda>> response = restTemplate.exchange(
                baseUrl + "/cliente/cli1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Venda>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetVendasByStatus() {
        ResponseEntity<List<Venda>> response = restTemplate.exchange(
                baseUrl + "/status/PENDENTE",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Venda>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateVenda() {
        // Create venda first
        ResponseEntity<Venda> createResponse = restTemplate.postForEntity(baseUrl, venda, Venda.class);
        String vendaId = createResponse.getBody().getId();

        // Update venda
        venda.setTotal(new BigDecimal("150.00"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Venda> requestEntity = new HttpEntity<>(venda, headers);

        ResponseEntity<Venda> updateResponse = restTemplate.exchange(
                baseUrl + "/" + vendaId,
                HttpMethod.PUT,
                requestEntity,
                Venda.class
        );
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals(new BigDecimal("150.00"), updateResponse.getBody().getTotal());
    }

    @Test
    void testDeleteVenda() {
        // Create venda first
        ResponseEntity<Venda> createResponse = restTemplate.postForEntity(baseUrl, venda, Venda.class);
        String vendaId = createResponse.getBody().getId();

        // Delete venda
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + vendaId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // Verify venda is deleted
        ResponseEntity<Venda> getResponse = restTemplate.getForEntity(baseUrl + "/" + vendaId, Venda.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testAlterarStatusVenda() {
        // Create venda first
        ResponseEntity<Venda> createResponse = restTemplate.postForEntity(baseUrl, venda, Venda.class);
        String vendaId = createResponse.getBody().getId();

        // Alter status
        ResponseEntity<Venda> statusResponse = restTemplate.exchange(
                baseUrl + "/" + vendaId + "/status?status=CONCLUIDA",
                HttpMethod.PUT,
                null,
                Venda.class
        );
        assertEquals(HttpStatus.OK, statusResponse.getStatusCode());
        assertEquals("CONCLUIDA", statusResponse.getBody().getStatus());
    }

    @Test
    void testCreateVendaWithInvalidData() {
        Venda invalidVenda = new Venda();
        invalidVenda.setClienteId(""); // Invalid clienteId

        ResponseEntity<Venda> response = restTemplate.postForEntity(baseUrl, invalidVenda, Venda.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetVendaNotFound() {
        ResponseEntity<Venda> response = restTemplate.getForEntity(baseUrl + "/999", Venda.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateVendaNotFound() {
        venda.setTotal(new BigDecimal("150.00"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Venda> requestEntity = new HttpEntity<>(venda, headers);

        ResponseEntity<Venda> response = restTemplate.exchange(
                baseUrl + "/999",
                HttpMethod.PUT,
                requestEntity,
                Venda.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteVendaNotFound() {
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/999",
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAlterarStatusVendaNotFound() {
        ResponseEntity<Venda> response = restTemplate.exchange(
                baseUrl + "/999/status?status=CONCLUIDA",
                HttpMethod.PUT,
                null,
                Venda.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
} 