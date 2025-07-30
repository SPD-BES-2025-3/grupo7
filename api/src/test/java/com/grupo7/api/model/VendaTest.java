package com.grupo7.api.model;

import com.grupo7.api.model.Venda.ItemVenda;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VendaTest {

    @Test
    void testVendaGettersSetters() {
        ItemVenda item = new ItemVenda("PROD001", 2, new BigDecimal("20.00"));
        Venda venda = new Venda("CLIENTE01", List.of(item), new BigDecimal("40.00"));

        venda.setFormaPagamento("PIX");
        venda.setStatus("PAGO");
        venda.setObservacoes("Pagamento via app");

        assertEquals("CLIENTE01", venda.getClienteId());
        assertEquals(1, venda.getItens().size());
        assertEquals(new BigDecimal("40.00"), venda.getTotal());
        assertEquals("PIX", venda.getFormaPagamento());
        assertEquals("PAGO", venda.getStatus());
        assertEquals("Pagamento via app", venda.getObservacoes());

        ItemVenda i = venda.getItens().get(0);
        assertEquals("PROD001", i.getProdutoId());
        assertEquals(2, i.getQuantidade());
        assertEquals(new BigDecimal("20.00"), i.getPrecoUnitario());
        assertEquals(new BigDecimal("40.00"), i.getSubtotal());
    }
}
