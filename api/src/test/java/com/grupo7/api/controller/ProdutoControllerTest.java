package com.grupo7.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.grupo7.api.model.Produto;
import com.grupo7.api.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoControllerTest {

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        produto1 = new Produto();
        produto1.setId("1");
        produto1.setNome("Ração Premium");
        produto1.setDescricao("Ração de alta qualidade para cães");
        produto1.setPreco(new BigDecimal("89.90"));
        produto1.setCategoria("Alimentação");
        produto1.setCodigo("RACAO001");
        produto1.setEstoque(50);
        produto1.setMarca("Marca Premium");
        produto1.setAtivo(true);

        produto2 = new Produto();
        produto2.setId("2");
        produto2.setNome("Brinquedo para Gatos");
        produto2.setDescricao("Brinquedo interativo para gatos");
        produto2.setPreco(new BigDecimal("29.90"));
        produto2.setCategoria("Brinquedos");
        produto2.setCodigo("BRINQ001");
        produto2.setEstoque(25);
        produto2.setMarca("Marca Brinquedos");
        produto2.setAtivo(false);
    }

    @Test
    void testGetAllProdutos() {
        // Given
        List<Produto> produtos = Arrays.asList(produto1, produto2);
        when(produtoService.findAll()).thenReturn(produtos);

        // When
        ResponseEntity<List<Produto>> response = produtoController.getAllProdutos();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Ração Premium", response.getBody().get(0).getNome());
        assertEquals("Brinquedo para Gatos", response.getBody().get(1).getNome());
        verify(produtoService, times(1)).findAll();
    }

    @Test
    void testGetProdutoById_Success() {
        // Given
        when(produtoService.findById("1")).thenReturn(Optional.of(produto1));

        // When
        ResponseEntity<Produto> response = produtoController.getProdutoById("1");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null);
        assertEquals("Ração Premium", response.getBody().getNome());
        verify(produtoService, times(1)).findById("1");
    }

    @Test
    void testGetProdutoById_NotFound() {
        // Given
        when(produtoService.findById("999")).thenReturn(Optional.empty());

        // When
        ResponseEntity<Produto> response = produtoController.getProdutoById("999");

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(produtoService, times(1)).findById("999");
    }

    @Test
    void testGetProdutosByCategoria() {
        // Given
        List<Produto> produtos = Arrays.asList(produto1);
        when(produtoService.findProdutosPorCategoria("Alimentação")).thenReturn(produtos);

        // When
        ResponseEntity<List<Produto>> response = produtoController.getProdutosByCategoria("Alimentação");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Alimentação", response.getBody().get(0).getCategoria());
        verify(produtoService, times(1)).findProdutosPorCategoria("Alimentação");
    }

    @Test
    void testGetProdutosAtivos() {
        // Given
        List<Produto> produtosAtivos = Arrays.asList(produto1);
        when(produtoService.findProdutosAtivos()).thenReturn(produtosAtivos);

        // When
        ResponseEntity<List<Produto>> response = produtoController.getProdutosAtivos();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).isAtivo());
        verify(produtoService, times(1)).findProdutosAtivos();
    }

    @Test
    void testBuscarPorNome() {
        // Given
        List<Produto> produtos = Arrays.asList(produto1);
        when(produtoService.findByNomeContaining("Ração")).thenReturn(produtos);

        // When
        ResponseEntity<List<Produto>> response = produtoController.buscarPorNome("Ração");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).getNome().contains("Ração"));
        verify(produtoService, times(1)).findByNomeContaining("Ração");
    }

    @Test
    void testGetProdutosPorFaixaPreco() {
        // Given
        List<Produto> produtos = Arrays.asList(produto1, produto2);
        when(produtoService.findProdutosPorFaixaPreco(any(), any())).thenReturn(produtos);

        // When
        ResponseEntity<List<Produto>> response = produtoController.getProdutosPorFaixaPreco(
            new BigDecimal("20.00"), new BigDecimal("100.00"));

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(produtoService, times(1)).findProdutosPorFaixaPreco(
            new BigDecimal("20.00"), new BigDecimal("100.00"));
    }

    @Test
    void testGetProdutosEmBaixoEstoque() {
        // Given
        List<Produto> produtos = Arrays.asList(produto2);
        when(produtoService.findProdutosEmBaixoEstoque(30)).thenReturn(produtos);

        // When
        ResponseEntity<List<Produto>> response = produtoController.getProdutosEmBaixoEstoque(30);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).getEstoque() < 30);
        verify(produtoService, times(1)).findProdutosEmBaixoEstoque(30);
    }

    @Test
    void testCreateProduto_Success() throws Exception {
        // Given
        Produto novoProduto = new Produto();
        novoProduto.setNome("Novo Produto");
        novoProduto.setDescricao("Descrição do produto");
        novoProduto.setPreco(new BigDecimal("15.90"));
        novoProduto.setCategoria("Teste");
        novoProduto.setCodigo("PROD001");
        novoProduto.setEstoque(10);
        novoProduto.setMarca("Marca Teste");
        novoProduto.setAtivo(true);
        
        when(produtoService.save(any(Produto.class))).thenReturn(novoProduto);

        // When & Then
        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoProduto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Novo Produto"))
                .andExpect(jsonPath("$.preco").value(15.90));

        verify(produtoService, times(1)).save(any(Produto.class));
    }

    @Test
    void testCreateProduto_ValidationError() throws Exception {
        // Given
        Produto produtoInvalido = new Produto();
        produtoInvalido.setNome("");
        produtoInvalido.setPreco(new BigDecimal("-10.00"));
        
        // When & Then
        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoInvalido)))
                .andExpect(status().isBadRequest());

        verify(produtoService, times(0)).save(any(Produto.class));
    }

    @Test
    void testUpdateProduto_Success() throws Exception {
        // Given
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setNome("Ração Premium Atualizada");
        produtoAtualizado.setDescricao("Descrição atualizada");
        produtoAtualizado.setPreco(new BigDecimal("99.90"));
        produtoAtualizado.setCategoria("Alimentação");
        produtoAtualizado.setCodigo("RACAO001");
        produtoAtualizado.setEstoque(50);
        produtoAtualizado.setMarca("Marca Premium");
        produtoAtualizado.setAtivo(true);
        
        when(produtoService.update(eq("1"), any(Produto.class))).thenReturn(produtoAtualizado);

        // When & Then
        mockMvc.perform(put("/produtos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ração Premium Atualizada"))
                .andExpect(jsonPath("$.preco").value(99.90));

        verify(produtoService, times(1)).update(eq("1"), any(Produto.class));
    }

    @Test
    void testUpdateProduto_NotFound() throws Exception {
        // Given
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setNome("Produto Atualizado");
        produtoAtualizado.setDescricao("Descrição do produto");
        produtoAtualizado.setPreco(new BigDecimal("99.90"));
        produtoAtualizado.setCategoria("Teste");
        produtoAtualizado.setCodigo("TEST001");
        produtoAtualizado.setEstoque(10);
        produtoAtualizado.setMarca("Marca Teste");
        produtoAtualizado.setAtivo(true);
        
        when(produtoService.update(eq("999"), any(Produto.class))).thenThrow(new RuntimeException("Produto não encontrado"));

        // When & Then
        mockMvc.perform(put("/produtos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoAtualizado)))
                .andExpect(status().isNotFound());

        verify(produtoService, times(1)).update(eq("999"), any(Produto.class));
    }

    @Test
    void testDeleteProduto() {
        // Given
        when(produtoService.existsById("1")).thenReturn(true);
        doNothing().when(produtoService).deleteById("1");

        // When
        ResponseEntity<Void> response = produtoController.deleteProduto("1");

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(produtoService, times(1)).existsById("1");
        verify(produtoService, times(1)).deleteById("1");
    }

    @Test
    void testDeleteProduto_NotFound() {
        // Given
        when(produtoService.existsById("999")).thenReturn(false);

        // When
        ResponseEntity<Void> response = produtoController.deleteProduto("999");

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(produtoService, times(1)).existsById("999");
        verify(produtoService, times(0)).deleteById("999");
    }

    @Test
    void testAlterarStatusProduto_Success() {
        // Given
        Produto produtoOriginal = new Produto();
        produtoOriginal.setId("1");
        produtoOriginal.setNome("Ração Premium");
        produtoOriginal.setAtivo(true);
        
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId("1");
        produtoAtualizado.setNome("Ração Premium");
        produtoAtualizado.setAtivo(false);
        
        when(produtoService.findById("1")).thenReturn(Optional.of(produtoOriginal));
        when(produtoService.save(any(Produto.class))).thenReturn(produtoAtualizado);

        // When
        ResponseEntity<Produto> response = produtoController.alterarStatusProduto("1", false);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isAtivo());
        verify(produtoService, times(1)).findById("1");
        verify(produtoService, times(1)).save(any(Produto.class));
    }

    @Test
    void testAlterarStatusProduto_NotFound() {
        // Given
        when(produtoService.findById("999")).thenReturn(Optional.empty());

        // When
        ResponseEntity<Produto> response = produtoController.alterarStatusProduto("999", false);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(produtoService, times(1)).findById("999");
        verify(produtoService, times(0)).save(any(Produto.class));
    }

    @Test
    void testAtualizarEstoque_Success() {
        // Given
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId("1");
        produtoAtualizado.setNome("Ração Premium");
        produtoAtualizado.setEstoque(75);
        
        when(produtoService.atualizarEstoque("1", 75)).thenReturn(true);
        when(produtoService.findById("1")).thenReturn(Optional.of(produtoAtualizado));

        // When
        ResponseEntity<Produto> response = produtoController.atualizarEstoque("1", 75);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(75, response.getBody().getEstoque());
        verify(produtoService, times(1)).atualizarEstoque("1", 75);
        verify(produtoService, times(1)).findById("1");
    }

    @Test
    void testAtualizarEstoque_NotFound() {
        // Given
        when(produtoService.atualizarEstoque("999", 50)).thenReturn(false);

        // When
        ResponseEntity<Produto> response = produtoController.atualizarEstoque("999", 50);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(produtoService, times(1)).atualizarEstoque("999", 50);
        verify(produtoService, times(0)).findById("999");
    }
} 