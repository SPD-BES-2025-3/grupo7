package com.grupo7.api.repository;

import com.grupo7.api.model.Venda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VendaRepositoryTest {

    @Mock
    private VendaRepository vendaRepository;

    private Venda venda1;
    private Venda venda2;

    @BeforeEach
    void setUp() {
        venda1 = new Venda();
        venda1.setId("1");
        venda1.setClienteId("cli1");
        venda1.setData(new Date());
        venda1.setValorTotal(100.0);
        venda1.setStatus("CONCLUIDA");

        venda2 = new Venda();
        venda2.setId("2");
        venda2.setClienteId("cli2");
        venda2.setData(new Date());
        venda2.setValorTotal(200.0);
        venda2.setStatus("CANCELADA");
    }

    @Test
    void testFindAll() {
        List<Venda> vendas = Arrays.asList(venda1, venda2);
        when(vendaRepository.findAll()).thenReturn(vendas);
        List<Venda> result = vendaRepository.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("cli1", result.get(0).getClienteId());
        assertEquals("cli2", result.get(1).getClienteId());
        verify(vendaRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(vendaRepository.findById("1")).thenReturn(Optional.of(venda1));
        when(vendaRepository.findById("999")).thenReturn(Optional.empty());
        Optional<Venda> found = vendaRepository.findById("1");
        Optional<Venda> notFound = vendaRepository.findById("999");
        assertTrue(found.isPresent());
        assertEquals("cli1", found.get().getClienteId());
        assertFalse(notFound.isPresent());
        verify(vendaRepository, times(1)).findById("1");
        verify(vendaRepository, times(1)).findById("999");
    }

    @Test
    void testFindByClienteId() {
        List<Venda> vendas = Arrays.asList(venda1);
        when(vendaRepository.findByClienteId("cli1")).thenReturn(vendas);
        List<Venda> result = vendaRepository.findByClienteId("cli1");
        assertEquals(1, result.size());
        assertEquals("cli1", result.get(0).getClienteId());
        verify(vendaRepository, times(1)).findByClienteId("cli1");
    }

    @Test
    void testFindByStatus() {
        List<Venda> vendasConcluidas = Arrays.asList(venda1);
        List<Venda> vendasCanceladas = Arrays.asList(venda2);
        when(vendaRepository.findByStatus("CONCLUIDA")).thenReturn(vendasConcluidas);
        when(vendaRepository.findByStatus("CANCELADA")).thenReturn(vendasCanceladas);
        List<Venda> concluidas = vendaRepository.findByStatus("CONCLUIDA");
        List<Venda> canceladas = vendaRepository.findByStatus("CANCELADA");
        assertEquals(1, concluidas.size());
        assertEquals("CONCLUIDA", concluidas.get(0).getStatus());
        assertEquals(1, canceladas.size());
        assertEquals("CANCELADA", canceladas.get(0).getStatus());
        verify(vendaRepository, times(1)).findByStatus("CONCLUIDA");
        verify(vendaRepository, times(1)).findByStatus("CANCELADA");
    }

    @Test
    void testSave() {
        Venda novaVenda = new Venda();
        novaVenda.setClienteId("cli3");
        when(vendaRepository.save(any(Venda.class))).thenReturn(novaVenda);
        Venda saved = vendaRepository.save(novaVenda);
        assertNotNull(saved);
        assertEquals("cli3", saved.getClienteId());
        verify(vendaRepository, times(1)).save(novaVenda);
    }

    @Test
    void testDeleteById() {
        doNothing().when(vendaRepository).deleteById("1");
        vendaRepository.deleteById("1");
        verify(vendaRepository, times(1)).deleteById("1");
    }

    @Test
    void testExistsById() {
        when(vendaRepository.existsById("1")).thenReturn(true);
        when(vendaRepository.existsById("999")).thenReturn(false);
        boolean exists = vendaRepository.existsById("1");
        boolean notExists = vendaRepository.existsById("999");
        assertTrue(exists);
        assertFalse(notExists);
        verify(vendaRepository, times(1)).existsById("1");
        verify(vendaRepository, times(1)).existsById("999");
    }
}