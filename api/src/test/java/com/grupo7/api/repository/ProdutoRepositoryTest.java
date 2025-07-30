package com.grupo7.api.repository;

import com.grupo7.api.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoRepositoryTest {

    @Mock
    private ProdutoRepository produtoRepository;

    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    void setUp() {
        produto1 = new Produto();
        produto1.setId("1");
        produto1.setNome("Ração Premium");
        produto1.setDescricao("Ração de alta qualidade para cães");
        produto1.setPreco(new BigDecimal("89.90"));
        produto1.setCategoria("Alimentação");
        produto1.setEstoque(50);
        produto1.setAtivo(true);

        produto2 = new Produto();
        produto2.setId("2");
        produto2.setNome("Brinquedo para Gatos");
        produto2.setDescricao("Brinquedo interativo para gatos");
        produto2.setPreco(new BigDecimal("29.90"));
        produto2.setCategoria("Brinquedos");
        produto2.setEstoque(25);
        produto2.setAtivo(false);
    }

    @Test
    void testFindAll() {
        // Given
        List<Produto> produtos = Arrays.asList(produto1, produto2);
        when(produtoRepository.findAll()).thenReturn(produtos);

        // When
        List<Produto> result = produtoRepository.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ração Premium", result.get(0).getNome());
        assertEquals("Brinquedo para Gatos", result.get(1).getNome());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Given
        when(produtoRepository.findById("1")).thenReturn(Optional.of(produto1));
        when(produtoRepository.findById("999")).thenReturn(Optional.empty());

        // When
        Optional<Produto> found = produtoRepository.findById("1");
        Optional<Produto> notFound = produtoRepository.findById("999");

        // Then
        assertTrue(found.isPresent());
        assertEquals("Ração Premium", found.get().getNome());
        assertFalse(notFound.isPresent());
        verify(produtoRepository, times(1)).findById("1");
        verify(produtoRepository, times(1)).findById("999");
    }

    @Test
    void testFindByNomeContainingIgnoreCase() {
        // Given
        List<Produto> produtos = Arrays.asList(produto1);
        when(produtoRepository.findByNomeContainingIgnoreCase("Ração")).thenReturn(produtos);

        // When
        List<Produto> result = produtoRepository.findByNomeContainingIgnoreCase("Ração");

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).getNome().contains("Ração"));
        verify(produtoRepository, times(1)).findByNomeContainingIgnoreCase("Ração");
    }

    @Test
    void testFindByCategoria() {
        // Given
        List<Produto> produtos = Arrays.asList(produto1);
        when(produtoRepository.findByCategoria("Alimentação")).thenReturn(produtos);

        // When
        List<Produto> result = produtoRepository.findByCategoria("Alimentação");

        // Then
        assertEquals(1, result.size());
        assertEquals("Alimentação", result.get(0).getCategoria());
        verify(produtoRepository, times(1)).findByCategoria("Alimentação");
    }

    @Test
    void testFindByAtivo() {
        // Given
        List<Produto> produtosAtivos = Arrays.asList(produto1);
        List<Produto> produtosInativos = Arrays.asList(produto2);
        
        when(produtoRepository.findByAtivo(true)).thenReturn(produtosAtivos);
        when(produtoRepository.findByAtivo(false)).thenReturn(produtosInativos);

        // When
        List<Produto> ativos = produtoRepository.findByAtivo(true);
        List<Produto> inativos = produtoRepository.findByAtivo(false);

        // Then
        assertEquals(1, ativos.size());
        assertTrue(ativos.get(0).isAtivo());
        assertEquals(1, inativos.size());
        assertFalse(inativos.get(0).isAtivo());
        verify(produtoRepository, times(1)).findByAtivo(true);
        verify(produtoRepository, times(1)).findByAtivo(false);
    }

    @Test
    void testFindByPrecoBetween() {
        // Given
        List<Produto> produtos = Arrays.asList(produto1, produto2);
        when(produtoRepository.findByPrecoBetween(any(), any())).thenReturn(produtos);

        // When
        List<Produto> result = produtoRepository.findByPrecoBetween(
            new BigDecimal("20.00"), new BigDecimal("100.00"));

        // Then
        assertEquals(2, result.size());
        verify(produtoRepository, times(1)).findByPrecoBetween(
            new BigDecimal("20.00"), new BigDecimal("100.00"));
    }

    @Test
    void testFindByEstoqueLessThan() {
        // Given
        List<Produto> produtos = Arrays.asList(produto2);
        when(produtoRepository.findByEstoqueLessThan(30)).thenReturn(produtos);

        // When
        List<Produto> result = produtoRepository.findByEstoqueLessThan(30);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).getEstoque() < 30);
        verify(produtoRepository, times(1)).findByEstoqueLessThan(30);
    }

    @Test
    void testSave() {
        // Given
        Produto novoProduto = new Produto();
        novoProduto.setNome("Novo Produto");
        novoProduto.setPreco(new BigDecimal("15.90"));
        
        when(produtoRepository.save(any(Produto.class))).thenReturn(novoProduto);

        // When
        Produto saved = produtoRepository.save(novoProduto);

        // Then
        assertNotNull(saved);
        assertEquals("Novo Produto", saved.getNome());
        verify(produtoRepository, times(1)).save(novoProduto);
    }

    @Test
    void testDeleteById() {
        // Given
        doNothing().when(produtoRepository).deleteById("1");

        // When
        produtoRepository.deleteById("1");

        // Then
        verify(produtoRepository, times(1)).deleteById("1");
    }

    @Test
    void testExistsById() {
        // Given
        when(produtoRepository.existsById("1")).thenReturn(true);
        when(produtoRepository.existsById("999")).thenReturn(false);

        // When
        boolean exists = produtoRepository.existsById("1");
        boolean notExists = produtoRepository.existsById("999");

        // Then
        assertTrue(exists);
        assertFalse(notExists);
        verify(produtoRepository, times(1)).existsById("1");
        verify(produtoRepository, times(1)).existsById("999");
    }

    @Test
    void testExistsByCodigo() {
        // Given
        when(produtoRepository.existsByCodigo("PROD001")).thenReturn(true);
        when(produtoRepository.existsByCodigo("PROD999")).thenReturn(false);

        // When
        boolean exists = produtoRepository.existsByCodigo("PROD001");
        boolean notExists = produtoRepository.existsByCodigo("PROD999");

        // Then
        assertTrue(exists);
        assertFalse(notExists);
        verify(produtoRepository, times(1)).existsByCodigo("PROD001");
        verify(produtoRepository, times(1)).existsByCodigo("PROD999");
    }
} 