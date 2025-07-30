package com.grupo7.api.integration;

import com.grupo7.api.model.Agendamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AgendamentoIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private Agendamento agendamento;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/agendamentos";
        
        agendamento = new Agendamento();
        agendamento.setClienteId("cli1");
        agendamento.setPetId("pet1");
        agendamento.setDataHora(new Date());
        agendamento.setStatus("PENDENTE");
    }

    @Test
    void testCreateAndRetrieveAgendamento() {
        // Create agendamento
        ResponseEntity<Agendamento> createResponse = restTemplate.postForEntity(baseUrl, agendamento, Agendamento.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getId());
        assertEquals("cli1", createResponse.getBody().getClienteId());

        String agendamentoId = createResponse.getBody().getId();

        // Retrieve agendamento
        ResponseEntity<Agendamento> getResponse = restTemplate.getForEntity(baseUrl + "/" + agendamentoId, Agendamento.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("cli1", getResponse.getBody().getClienteId());
    }

    @Test
    void testGetAllAgendamentos() {
        ResponseEntity<List<Agendamento>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Agendamento>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetAgendamentosByClienteId() {
        ResponseEntity<List<Agendamento>> response = restTemplate.exchange(
                baseUrl + "/cliente/cli1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Agendamento>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetAgendamentosByStatus() {
        ResponseEntity<List<Agendamento>> response = restTemplate.exchange(
                baseUrl + "/status/PENDENTE",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Agendamento>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateAgendamento() {
        // Create agendamento first
        ResponseEntity<Agendamento> createResponse = restTemplate.postForEntity(baseUrl, agendamento, Agendamento.class);
        String agendamentoId = createResponse.getBody().getId();

        // Update agendamento
        agendamento.setStatus("CONCLUIDO");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Agendamento> requestEntity = new HttpEntity<>(agendamento, headers);

        ResponseEntity<Agendamento> updateResponse = restTemplate.exchange(
                baseUrl + "/" + agendamentoId,
                HttpMethod.PUT,
                requestEntity,
                Agendamento.class
        );
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals("CONCLUIDO", updateResponse.getBody().getStatus());
    }

    @Test
    void testDeleteAgendamento() {
        // Create agendamento first
        ResponseEntity<Agendamento> createResponse = restTemplate.postForEntity(baseUrl, agendamento, Agendamento.class);
        String agendamentoId = createResponse.getBody().getId();

        // Delete agendamento
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + agendamentoId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // Verify agendamento is deleted
        ResponseEntity<Agendamento> getResponse = restTemplate.getForEntity(baseUrl + "/" + agendamentoId, Agendamento.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testAlterarStatusAgendamento() {
        // Create agendamento first
        ResponseEntity<Agendamento> createResponse = restTemplate.postForEntity(baseUrl, agendamento, Agendamento.class);
        String agendamentoId = createResponse.getBody().getId();

        // Alter status
        ResponseEntity<Agendamento> statusResponse = restTemplate.exchange(
                baseUrl + "/" + agendamentoId + "/status?status=CONCLUIDO",
                HttpMethod.PUT,
                null,
                Agendamento.class
        );
        assertEquals(HttpStatus.OK, statusResponse.getStatusCode());
        assertEquals("CONCLUIDO", statusResponse.getBody().getStatus());
    }

    @Test
    void testCreateAgendamentoWithInvalidData() {
        Agendamento invalidAgendamento = new Agendamento();
        invalidAgendamento.setClienteId(""); // Invalid clienteId

        ResponseEntity<Agendamento> response = restTemplate.postForEntity(baseUrl, invalidAgendamento, Agendamento.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetAgendamentoNotFound() {
        ResponseEntity<Agendamento> response = restTemplate.getForEntity(baseUrl + "/999", Agendamento.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateAgendamentoNotFound() {
        agendamento.setStatus("CONCLUIDO");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Agendamento> requestEntity = new HttpEntity<>(agendamento, headers);

        ResponseEntity<Agendamento> response = restTemplate.exchange(
                baseUrl + "/999",
                HttpMethod.PUT,
                requestEntity,
                Agendamento.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteAgendamentoNotFound() {
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/999",
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAlterarStatusAgendamentoNotFound() {
        ResponseEntity<Agendamento> response = restTemplate.exchange(
                baseUrl + "/999/status?status=CONCLUIDO",
                HttpMethod.PUT,
                null,
                Agendamento.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
} 