package com.grupo7.api.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteTest {

    @Test
    public void testSettersAndGetters() {
        Cliente cliente = new Cliente();

        cliente.setNome("Paulo Roberto");
        cliente.setEmail("paulo@example.com");
        cliente.setTelefone("(62) 99999-9999");
        cliente.setCpf("123.456.789-00");
        cliente.setEndereco("Rua A, 123");
        cliente.setObservacoes("Cliente VIP");
        cliente.setAtivo(true);
        cliente.setPetsIds(Arrays.asList("pet1", "pet2"));

        assertEquals("Paulo Roberto", cliente.getNome());
        assertEquals("paulo@example.com", cliente.getEmail());
        assertEquals("(62) 99999-9999", cliente.getTelefone());
        assertEquals("123.456.789-00", cliente.getCpf());
        assertEquals("Rua A, 123", cliente.getEndereco());
        assertEquals("Cliente VIP", cliente.getObservacoes());
        assertTrue(cliente.isAtivo());
        assertEquals(Arrays.asList("pet1", "pet2"), cliente.getPetsIds());
    }

    @Test
    public void testConstrutorComParametros() {
        Cliente cliente = new Cliente("Ana Silva", "ana@email.com", "(62) 98888-7777", "987.654.321-00");

        assertEquals("Ana Silva", cliente.getNome());
        assertEquals("ana@email.com", cliente.getEmail());
        assertEquals("(62) 98888-7777", cliente.getTelefone());
        assertEquals("987.654.321-00", cliente.getCpf());
        // Os demais campos ficam null ou default:
        assertNull(cliente.getEndereco());
        assertNull(cliente.getObservacoes());
        assertTrue(cliente.isAtivo());
        assertNull(cliente.getPetsIds());
    }
}
