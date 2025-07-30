package com.grupo7.api.service;

import com.grupo7.api.model.Agendamento;
import com.grupo7.api.repository.AgendamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @InjectMocks
    private AgendamentoService agendamentoService;

    private Agendamento agendamento1;
    private Agendamento agendamento2;

    @BeforeEach
    void setUp() {
        agendamento1 = new Agendamento();
        agendamento1.setId("1");
        agendamento1.setClienteId("cli1");
        agendamento1.setPetId("pet1");
        agendamento1.setDataHora(LocalDateTime.now());
        agendamento1.setStatus("PENDENTE");

        agendamento2 = new Agendamento();
        agendamento2.setId("2");
        agendamento2.setClienteId("cli2");
        agendamento2.setPetId("pet2");
        agendamento2.setDataHora(LocalDateTime.now());
        agendamento2.setStatus("CONCLUIDO");
    }

    @Test
    void testFindAll() {
        List<Agendamento> agendamentos = Arrays.asList(agendamento1, agendamento2);
        when(agendamentoRepository.findAll()).thenReturn(agendamentos);
        List<Agendamento> result = agendamentoService.findAll();
        assertEquals(2, result.size());
        assertEquals("cli1", result.get(0).getClienteId());
        assertEquals("cli2", result.get(1).getClienteId());
        verify(agendamentoRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        when(agendamentoRepository.findById("1")).thenReturn(Optional.of(agendamento1));
        Optional<Agendamento> result = agendamentoService.findById("1");
        assertTrue(result.isPresent());
        assertEquals("cli1", result.get().getClienteId());
        verify(agendamentoRepository, times(1)).findById("1");
    }

    @Test
    void testFindById_NotFound() {
        when(agendamentoRepository.findById("999")).thenReturn(Optional.empty());
        Optional<Agendamento> result = agendamentoService.findById("999");
        assertFalse(result.isPresent());
        verify(agendamentoRepository, times(1)).findById("999");
    }

    @Test
    void testFindByClienteId() {
        List<Agendamento> agendamentos = Arrays.asList(agendamento1);
        when(agendamentoRepository.findByClienteId("cli1")).thenReturn(agendamentos);
        List<Agendamento> result = agendamentoService.findByClienteId("cli1");
        assertEquals(1, result.size());
        assertEquals("cli1", result.get(0).getClienteId());
        verify(agendamentoRepository, times(1)).findByClienteId("cli1");
    }

    @Test
    void testFindByStatus() {
        List<Agendamento> pendentes = Arrays.asList(agendamento1);
        when(agendamentoRepository.findByStatus("PENDENTE")).thenReturn(pendentes);
        List<Agendamento> result = agendamentoService.findByStatus("PENDENTE");
        assertEquals(1, result.size());
        assertEquals("PENDENTE", result.get(0).getStatus());
        verify(agendamentoRepository, times(1)).findByStatus("PENDENTE");
    }

    @Test
    void testSave() {
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento1);
        Agendamento saved = agendamentoService.save(agendamento1);
        assertNotNull(saved);
        assertEquals("cli1", saved.getClienteId());
        verify(agendamentoRepository, times(1)).save(agendamento1);
    }

    @Test
    void testUpdate_Success() {
        when(agendamentoRepository.findById("1")).thenReturn(Optional.of(agendamento1));
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento1);
        Agendamento updated = agendamentoService.update("1", agendamento1);
        assertNotNull(updated);
        assertEquals("cli1", updated.getClienteId());
        verify(agendamentoRepository, times(1)).findById("1");
        verify(agendamentoRepository, times(1)).save(any(Agendamento.class));
    }

    @Test
    void testUpdate_NotFound() {
        when(agendamentoRepository.findById("999")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> agendamentoService.update("999", agendamento1));
        verify(agendamentoRepository, times(1)).findById("999");
        verify(agendamentoRepository, never()).save(any(Agendamento.class));
    }

    @Test
    void testDeleteById() {
        doNothing().when(agendamentoRepository).deleteById("1");
        agendamentoService.deleteById("1");
        verify(agendamentoRepository, times(1)).deleteById("1");
    }

    @Test
    void testExistsById() {
        when(agendamentoRepository.existsById("1")).thenReturn(true);
        when(agendamentoRepository.existsById("999")).thenReturn(false);
        boolean exists = agendamentoService.existsById("1");
        boolean notExists = agendamentoService.existsById("999");
        assertTrue(exists);
        assertFalse(notExists);
        verify(agendamentoRepository, times(1)).existsById("1");
        verify(agendamentoRepository, times(1)).existsById("999");
    }
}