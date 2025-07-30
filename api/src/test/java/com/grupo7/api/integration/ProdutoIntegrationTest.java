package com.grupo7.api.integration;

import com.grupo7.api.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProdutoIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private Produto produto;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/produtos";
        
        produto = new Produto();
        produto.setNome("Ração Premium");
        produto.setDescricao("Ração de alta qualidade para cães");
        produto.setPreco(new BigDecimal("89.90"));
        produto.setCategoria("Alimentação");
        produto.setEstoque(50);
        produto.setAtivo(true);
    }

    @Test
    void testCreateAndRetrieveProduto() {
        // Create produto
        ResponseEntity<Produto> createResponse = restTemplate.postForEntity(baseUrl, produto, Produto.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getId());
        assertEquals("Ração Premium", createResponse.getBody().getNome());

        String produtoId = createResponse.getBody().getId();

        // Retrieve produto
        ResponseEntity<Produto> getResponse = restTemplate.getForEntity(baseUrl + "/" + produtoId, Produto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("Ração Premium", getResponse.getBody().getNome());
    }

    @Test
    void testGetAllProdutos() {
        ResponseEntity<List<Produto>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Produto>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetProdutosByCategoria() {
        ResponseEntity<List<Produto>> response = restTemplate.exchange(
                baseUrl + "/categoria/Alimentação",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Produto>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetProdutosAtivos() {
        ResponseEntity<List<Produto>> response = restTemplate.exchange(
                baseUrl + "/ativos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Produto>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testBuscarPorNome() {
        ResponseEntity<List<Produto>> response = restTemplate.exchange(
                baseUrl + "/buscar?nome=Racao",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Produto>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetProdutosPorFaixaPreco() {
        ResponseEntity<List<Produto>> response = restTemplate.exchange(
                baseUrl + "/preco?minimo=20.00&maximo=100.00",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Produto>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetProdutosEmBaixoEstoque() {
        ResponseEntity<List<Produto>> response = restTemplate.exchange(
                baseUrl + "/estoque-baixo?limite=30",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Produto>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateProduto() {
        // Create produto first
        ResponseEntity<Produto> createResponse = restTemplate.postForEntity(baseUrl, produto, Produto.class);
        String produtoId = createResponse.getBody().getId();

        // Update produto
        produto.setNome("Ração Premium Atualizada");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Produto> requestEntity = new HttpEntity<>(produto, headers);

        ResponseEntity<Produto> updateResponse = restTemplate.exchange(
                baseUrl + "/" + produtoId,
                HttpMethod.PUT,
                requestEntity,
                Produto.class
        );
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals("Ração Premium Atualizada", updateResponse.getBody().getNome());
    }

    @Test
    void testDeleteProduto() {
        // Create produto first
        ResponseEntity<Produto> createResponse = restTemplate.postForEntity(baseUrl, produto, Produto.class);
        String produtoId = createResponse.getBody().getId();

        // Delete produto
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + produtoId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // Verify produto is deleted
        ResponseEntity<Produto> getResponse = restTemplate.getForEntity(baseUrl + "/" + produtoId, Produto.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testAlterarStatusProduto() {
        // Create produto first
        ResponseEntity<Produto> createResponse = restTemplate.postForEntity(baseUrl, produto, Produto.class);
        String produtoId = createResponse.getBody().getId();

        // Alter status
        ResponseEntity<Produto> statusResponse = restTemplate.exchange(
                baseUrl + "/" + produtoId + "/status?ativo=false",
                HttpMethod.PUT,
                null,
                Produto.class
        );
        assertEquals(HttpStatus.OK, statusResponse.getStatusCode());
        assertFalse(statusResponse.getBody().isAtivo());
    }

    @Test
    void testAtualizarEstoque() {
        // Create produto first
        ResponseEntity<Produto> createResponse = restTemplate.postForEntity(baseUrl, produto, Produto.class);
        String produtoId = createResponse.getBody().getId();

        // Update stock
        ResponseEntity<Produto> stockResponse = restTemplate.exchange(
                baseUrl + "/" + produtoId + "/estoque?quantidade=25",
                HttpMethod.PUT,
                null,
                Produto.class
        );
        assertEquals(HttpStatus.OK, stockResponse.getStatusCode());
        assertEquals(25, stockResponse.getBody().getEstoque());
    }

    @Test
    void testCreateProdutoWithInvalidData() {
        Produto invalidProduto = new Produto();
        invalidProduto.setNome(""); // Invalid name

        ResponseEntity<Produto> response = restTemplate.postForEntity(baseUrl, invalidProduto, Produto.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetProdutoNotFound() {
        ResponseEntity<Produto> response = restTemplate.getForEntity(baseUrl + "/999", Produto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateProdutoNotFound() {
        produto.setNome("Ração Premium Atualizada");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Produto> requestEntity = new HttpEntity<>(produto, headers);

        ResponseEntity<Produto> response = restTemplate.exchange(
                baseUrl + "/999",
                HttpMethod.PUT,
                requestEntity,
                Produto.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteProdutoNotFound() {
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/999",
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
} 