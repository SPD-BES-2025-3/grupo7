package com.grupo7.api.service;

import com.grupo7.api.model.Produto;
import com.grupo7.api.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

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
        List<Produto> produtos = Arrays.asList(produto1, produto2);
        when(produtoRepository.findAll()).thenReturn(produtos);
        List<Produto> result = produtoService.findAll();
        assertEquals(2, result.size());
        assertEquals("Ração Premium", result.get(0).getNome());
        assertEquals("Brinquedo para Gatos", result.get(1).getNome());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        when(produtoRepository.findById("1")).thenReturn(Optional.of(produto1));
        Optional<Produto> result = produtoService.findById("1");
        assertTrue(result.isPresent());
        assertEquals("Ração Premium", result.get().getNome());
        verify(produtoRepository, times(1)).findById("1");
    }

    @Test
    void testFindById_NotFound() {
        when(produtoRepository.findById("999")).thenReturn(Optional.empty());
        Optional<Produto> result = produtoService.findById("999");
        assertFalse(result.isPresent());
        verify(produtoRepository, times(1)).findById("999");
    }

    @Test
    void testFindByCategoria() {
        List<Produto> produtos = Arrays.asList(produto1);
        when(produtoRepository.findByCategoria("Alimentação")).thenReturn(produtos);
        List<Produto> result = produtoService.findByCategoria("Alimentação");
        assertEquals(1, result.size());
        assertEquals("Alimentação", result.get(0).getCategoria());
        verify(produtoRepository, times(1)).findByCategoria("Alimentação");
    }

    @Test
    void testFindByAtivo() {
        List<Produto> produtosAtivos = Arrays.asList(produto1);
        when(produtoRepository.findByAtivo(true)).thenReturn(produtosAtivos);
        List<Produto> result = produtoService.findByAtivo(true);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isAtivo());
        verify(produtoRepository, times(1)).findByAtivo(true);
    }

    @Test
    void testSave() {
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto1);
        Produto saved = produtoService.save(produto1);
        assertNotNull(saved);
        assertEquals("Ração Premium", saved.getNome());
        verify(produtoRepository, times(1)).save(produto1);
    }

    @Test
    void testUpdate_Success() {
        when(produtoRepository.findById("1")).thenReturn(Optional.of(produto1));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto1);
        Produto updated = produtoService.update("1", produto1);
        assertNotNull(updated);
        assertEquals("Ração Premium", updated.getNome());
        verify(produtoRepository, times(1)).findById("1");
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void testUpdate_NotFound() {
        when(produtoRepository.findById("999")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> produtoService.update("999", produto1));
        verify(produtoRepository, times(1)).findById("999");
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void testDeleteById() {
        doNothing().when(produtoRepository).deleteById("1");
        produtoService.deleteById("1");
        verify(produtoRepository, times(1)).deleteById("1");
    }

    @Test
    void testExistsById() {
        when(produtoRepository.existsById("1")).thenReturn(true);
        when(produtoRepository.existsById("999")).thenReturn(false);
        boolean exists = produtoService.existsById("1");
        boolean notExists = produtoService.existsById("999");
        assertTrue(exists);
        assertFalse(notExists);
        verify(produtoRepository, times(1)).existsById("1");
        verify(produtoRepository, times(1)).existsById("999");
    }
}