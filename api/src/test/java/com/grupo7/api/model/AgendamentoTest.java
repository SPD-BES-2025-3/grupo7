package com.grupo7.api.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AgendamentoTest {

    @Test
    void testAgendamentoGettersSetters() {
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1);
        Agendamento agendamento = new Agendamento("CLIENTE01", "PET01", "Banho", dataHora);

        agendamento.setObservacoes("Chegar 10min antes");
        agendamento.setStatus("CONFIRMADO");
        agendamento.setValor(80.0);
        agendamento.setFuncionarioId("FUNC001");

        assertEquals("CLIENTE01", agendamento.getClienteId());
        assertEquals("PET01", agendamento.getPetId());
        assertEquals("Banho", agendamento.getServico());
        assertEquals(dataHora, agendamento.getDataHora());
        assertEquals("Chegar 10min antes", agendamento.getObservacoes());
        assertEquals("CONFIRMADO", agendamento.getStatus());
        assertEquals(80.0, agendamento.getValor());
        assertEquals("FUNC001", agendamento.getFuncionarioId());
    }
}
