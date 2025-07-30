package com.grupo7.api.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    @Test
    void testProdutoGettersSetters() {
        Produto produto = new Produto("Ração", "Ração para cães", "Ração", "RAC001", new BigDecimal("89.90"), 100);
        produto.setMarca("Golden");
        produto.setPeso("10kg");
        produto.setTamanho("M");
        produto.setAtivo(false);

        assertEquals("Ração", produto.getNome());
        assertEquals("Ração para cães", produto.getDescricao());
        assertEquals("Ração", produto.getCategoria());
        assertEquals("RAC001", produto.getCodigo());
        assertEquals(new BigDecimal("89.90"), produto.getPreco());
        assertEquals(100, produto.getEstoque());
        assertEquals("Golden", produto.getMarca());
        assertEquals("10kg", produto.getPeso());
        assertEquals("M", produto.getTamanho());
        assertFalse(produto.isAtivo());
    }
}
