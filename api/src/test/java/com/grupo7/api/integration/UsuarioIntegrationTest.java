package com.grupo7.api.integration;

import com.grupo7.api.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UsuarioIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/usuarios";
        
        usuario = new Usuario();
        usuario.setNome("João");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("senha123");
        usuario.setRole("ADMIN");
        usuario.setAtivo(true);
    }

    @Test
    void testCreateAndRetrieveUsuario() {
        // Create usuario
        ResponseEntity<Usuario> createResponse = restTemplate.postForEntity(baseUrl, usuario, Usuario.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getId());
        assertEquals("João", createResponse.getBody().getNome());

        String usuarioId = createResponse.getBody().getId();

        // Retrieve usuario
        ResponseEntity<Usuario> getResponse = restTemplate.getForEntity(baseUrl + "/" + usuarioId, Usuario.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("João", getResponse.getBody().getNome());
    }

    @Test
    void testGetAllUsuarios() {
        ResponseEntity<List<Usuario>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Usuario>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetUsuarioByEmail() {
        ResponseEntity<Usuario> response = restTemplate.getForEntity(baseUrl + "/email/joao@email.com", Usuario.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetUsuariosAtivos() {
        ResponseEntity<List<Usuario>> response = restTemplate.exchange(
                baseUrl + "/ativos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Usuario>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateUsuario() {
        // Create usuario first
        ResponseEntity<Usuario> createResponse = restTemplate.postForEntity(baseUrl, usuario, Usuario.class);
        String usuarioId = createResponse.getBody().getId();

        // Update usuario
        usuario.setNome("João Atualizado");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Usuario> requestEntity = new HttpEntity<>(usuario, headers);

        ResponseEntity<Usuario> updateResponse = restTemplate.exchange(
                baseUrl + "/" + usuarioId,
                HttpMethod.PUT,
                requestEntity,
                Usuario.class
        );
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals("João Atualizado", updateResponse.getBody().getNome());
    }

    @Test
    void testDeleteUsuario() {
        // Create usuario first
        ResponseEntity<Usuario> createResponse = restTemplate.postForEntity(baseUrl, usuario, Usuario.class);
        String usuarioId = createResponse.getBody().getId();

        // Delete usuario
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + usuarioId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // Verify usuario is deleted
        ResponseEntity<Usuario> getResponse = restTemplate.getForEntity(baseUrl + "/" + usuarioId, Usuario.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testAlterarStatusUsuario() {
        // Create usuario first
        ResponseEntity<Usuario> createResponse = restTemplate.postForEntity(baseUrl, usuario, Usuario.class);
        String usuarioId = createResponse.getBody().getId();

        // Alter status
        ResponseEntity<Usuario> statusResponse = restTemplate.exchange(
                baseUrl + "/" + usuarioId + "/status?ativo=false",
                HttpMethod.PUT,
                null,
                Usuario.class
        );
        assertEquals(HttpStatus.OK, statusResponse.getStatusCode());
        assertFalse(statusResponse.getBody().isAtivo());
    }

    @Test
    void testCreateUsuarioWithInvalidData() {
        Usuario invalidUsuario = new Usuario();
        invalidUsuario.setNome(""); // Invalid name

        ResponseEntity<Usuario> response = restTemplate.postForEntity(baseUrl, invalidUsuario, Usuario.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCreateUsuarioWithDuplicateEmail() {
        // Create first usuario
        ResponseEntity<Usuario> createResponse1 = restTemplate.postForEntity(baseUrl, usuario, Usuario.class);
        assertEquals(HttpStatus.CREATED, createResponse1.getStatusCode());

        // Try to create second usuario with same email
        Usuario usuario2 = new Usuario();
        usuario2.setNome("João 2");
        usuario2.setEmail("joao@email.com"); // Same email
        usuario2.setSenha("senha456");
        usuario2.setRole("USER");
        usuario2.setAtivo(true);

        ResponseEntity<Usuario> createResponse2 = restTemplate.postForEntity(baseUrl, usuario2, Usuario.class);
        assertEquals(HttpStatus.BAD_REQUEST, createResponse2.getStatusCode());
    }

    @Test
    void testGetUsuarioNotFound() {
        ResponseEntity<Usuario> response = restTemplate.getForEntity(baseUrl + "/999", Usuario.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateUsuarioNotFound() {
        usuario.setNome("João Atualizado");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Usuario> requestEntity = new HttpEntity<>(usuario, headers);

        ResponseEntity<Usuario> response = restTemplate.exchange(
                baseUrl + "/999",
                HttpMethod.PUT,
                requestEntity,
                Usuario.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteUsuarioNotFound() {
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/999",
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAlterarStatusUsuarioNotFound() {
        ResponseEntity<Usuario> response = restTemplate.exchange(
                baseUrl + "/999/status?ativo=false",
                HttpMethod.PUT,
                null,
                Usuario.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
} 