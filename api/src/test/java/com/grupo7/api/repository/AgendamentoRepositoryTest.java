package com.grupo7.api.repository;

import com.grupo7.api.model.Agendamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class AgendamentoRepositoryTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

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
        List<Agendamento> result = agendamentoRepository.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("cli1", result.get(0).getClienteId());
        assertEquals("cli2", result.get(1).getClienteId());
        verify(agendamentoRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(agendamentoRepository.findById("1")).thenReturn(Optional.of(agendamento1));
        when(agendamentoRepository.findById("999")).thenReturn(Optional.empty());
        Optional<Agendamento> found = agendamentoRepository.findById("1");
        Optional<Agendamento> notFound = agendamentoRepository.findById("999");
        assertTrue(found.isPresent());
        assertEquals("cli1", found.get().getClienteId());
        assertFalse(notFound.isPresent());
        verify(agendamentoRepository, times(1)).findById("1");
        verify(agendamentoRepository, times(1)).findById("999");
    }

    @Test
    void testFindByClienteId() {
        List<Agendamento> agendamentos = Arrays.asList(agendamento1);
        when(agendamentoRepository.findByClienteId("cli1")).thenReturn(agendamentos);
        List<Agendamento> result = agendamentoRepository.findByClienteId("cli1");
        assertEquals(1, result.size());
        assertEquals("cli1", result.get(0).getClienteId());
        verify(agendamentoRepository, times(1)).findByClienteId("cli1");
    }

    @Test
    void testFindByStatus() {
        List<Agendamento> pendentes = Arrays.asList(agendamento1);
        List<Agendamento> concluidos = Arrays.asList(agendamento2);
        when(agendamentoRepository.findByStatus("PENDENTE")).thenReturn(pendentes);
        when(agendamentoRepository.findByStatus("CONCLUIDO")).thenReturn(concluidos);
        List<Agendamento> resultPendentes = agendamentoRepository.findByStatus("PENDENTE");
        List<Agendamento> resultConcluidos = agendamentoRepository.findByStatus("CONCLUIDO");
        assertEquals(1, resultPendentes.size());
        assertEquals("PENDENTE", resultPendentes.get(0).getStatus());
        assertEquals(1, resultConcluidos.size());
        assertEquals("CONCLUIDO", resultConcluidos.get(0).getStatus());
        verify(agendamentoRepository, times(1)).findByStatus("PENDENTE");
        verify(agendamentoRepository, times(1)).findByStatus("CONCLUIDO");
    }

    @Test
    void testSave() {
        Agendamento novoAgendamento = new Agendamento();
        novoAgendamento.setClienteId("cli3");
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(novoAgendamento);
        Agendamento saved = agendamentoRepository.save(novoAgendamento);
        assertNotNull(saved);
        assertEquals("cli3", saved.getClienteId());
        verify(agendamentoRepository, times(1)).save(novoAgendamento);
    }

    @Test
    void testDeleteById() {
        doNothing().when(agendamentoRepository).deleteById("1");
        agendamentoRepository.deleteById("1");
        verify(agendamentoRepository, times(1)).deleteById("1");
    }

    @Test
    void testExistsById() {
        when(agendamentoRepository.existsById("1")).thenReturn(true);
        when(agendamentoRepository.existsById("999")).thenReturn(false);
        boolean exists = agendamentoRepository.existsById("1");
        boolean notExists = agendamentoRepository.existsById("999");
        assertTrue(exists);
        assertFalse(notExists);
        verify(agendamentoRepository, times(1)).existsById("1");
        verify(agendamentoRepository, times(1)).existsById("999");
    }
}