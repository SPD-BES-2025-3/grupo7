package com.grupo7.api.service;

import com.grupo7.api.model.Venda;
import com.grupo7.api.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @InjectMocks
    private VendaService vendaService;

    private Venda venda1;
    private Venda venda2;

    @BeforeEach
    void setUp() {
        venda1 = new Venda();
        venda1.setId("1");
        venda1.setClienteId("cli1");
        venda1.setDataPagamento(LocalDateTime.now());
        venda1.setTotal(new BigDecimal("100.00"));
        venda1.setStatus("CONCLUIDA");

        venda2 = new Venda();
        venda2.setId("2");
        venda2.setClienteId("cli2");
        venda2.setDataPagamento(LocalDateTime.now());
        venda2.setTotal(new BigDecimal("200.00"));
        venda2.setStatus("CANCELADA");
    }

    @Test
    void testFindAll() {
        List<Venda> vendas = Arrays.asList(venda1, venda2);
        when(vendaRepository.findAll()).thenReturn(vendas);
        List<Venda> result = vendaService.findAll();
        assertEquals(2, result.size());
        assertEquals("cli1", result.get(0).getClienteId());
        assertEquals("cli2", result.get(1).getClienteId());
        verify(vendaRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        when(vendaRepository.findById("1")).thenReturn(Optional.of(venda1));
        Optional<Venda> result = vendaService.findById("1");
        assertTrue(result.isPresent());
        assertEquals("cli1", result.get().getClienteId());
        verify(vendaRepository, times(1)).findById("1");
    }

    @Test
    void testFindById_NotFound() {
        when(vendaRepository.findById("999")).thenReturn(Optional.empty());
        Optional<Venda> result = vendaService.findById("999");
        assertFalse(result.isPresent());
        verify(vendaRepository, times(1)).findById("999");
    }

    @Test
    void testFindByClienteId() {
        List<Venda> vendas = Arrays.asList(venda1);
        when(vendaRepository.findByClienteId("cli1")).thenReturn(vendas);
        List<Venda> result = vendaService.findByClienteId("cli1");
        assertEquals(1, result.size());
        assertEquals("cli1", result.get(0).getClienteId());
        verify(vendaRepository, times(1)).findByClienteId("cli1");
    }

    @Test
    void testFindByStatus() {
        List<Venda> vendasConcluidas = Arrays.asList(venda1);
        when(vendaRepository.findByStatus("CONCLUIDA")).thenReturn(vendasConcluidas);
        List<Venda> result = vendaService.findByStatus("CONCLUIDA");
        assertEquals(1, result.size());
        assertEquals("CONCLUIDA", result.get(0).getStatus());
        verify(vendaRepository, times(1)).findByStatus("CONCLUIDA");
    }

    @Test
    void testSave() {
        when(vendaRepository.save(any(Venda.class))).thenReturn(venda1);
        Venda saved = vendaService.save(venda1);
        assertNotNull(saved);
        assertEquals("cli1", saved.getClienteId());
        verify(vendaRepository, times(1)).save(venda1);
    }

    @Test
    void testUpdate_Success() {
        when(vendaRepository.findById("1")).thenReturn(Optional.of(venda1));
        when(vendaRepository.save(any(Venda.class))).thenReturn(venda1);
        Venda updated = vendaService.update("1", venda1);
        assertNotNull(updated);
        assertEquals("cli1", updated.getClienteId());
        verify(vendaRepository, times(1)).findById("1");
        verify(vendaRepository, times(1)).save(any(Venda.class));
    }

    @Test
    void testUpdate_NotFound() {
        when(vendaRepository.findById("999")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> vendaService.update("999", venda1));
        verify(vendaRepository, times(1)).findById("999");
        verify(vendaRepository, never()).save(any(Venda.class));
    }

    @Test
    void testDeleteById() {
        doNothing().when(vendaRepository).deleteById("1");
        vendaService.deleteById("1");
        verify(vendaRepository, times(1)).deleteById("1");
    }

    @Test
    void testExistsById() {
        when(vendaRepository.existsById("1")).thenReturn(true);
        when(vendaRepository.existsById("999")).thenReturn(false);
        boolean exists = vendaService.existsById("1");
        boolean notExists = vendaService.existsById("999");
        assertTrue(exists);
        assertFalse(notExists);
        verify(vendaRepository, times(1)).existsById("1");
        verify(vendaRepository, times(1)).existsById("999");
    }
}