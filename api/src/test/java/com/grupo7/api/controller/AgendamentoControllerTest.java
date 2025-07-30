package com.grupo7.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.grupo7.api.model.Agendamento;
import com.grupo7.api.service.AgendamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AgendamentoControllerTest {

    @Mock
    private AgendamentoService agendamentoService;

    @InjectMocks
    private AgendamentoController agendamentoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Agendamento agendamento1;
    private Agendamento agendamento2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(agendamentoController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        agendamento1 = new Agendamento();
        agendamento1.setId("1");
        agendamento1.setClienteId("cli1");
        agendamento1.setPetId("pet1");
        agendamento1.setServico("Banho e Tosa");
        agendamento1.setDataHora(LocalDateTime.now().plusDays(1));
        agendamento1.setStatus("PENDENTE");

        agendamento2 = new Agendamento();
        agendamento2.setId("2");
        agendamento2.setClienteId("cli2");
        agendamento2.setPetId("pet2");
        agendamento2.setServico("Consulta Veterinária");
        agendamento2.setDataHora(LocalDateTime.now().plusDays(2));
        agendamento2.setStatus("CONCLUIDO");
    }

    @Test
    void testGetAllAgendamentos() {
        List<Agendamento> agendamentos = Arrays.asList(agendamento1, agendamento2);
        when(agendamentoService.findAll()).thenReturn(agendamentos);

        ResponseEntity<List<Agendamento>> response = agendamentoController.getAllAgendamentos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(agendamentoService, times(1)).findAll();
    }

    @Test
    void testGetAgendamentoById_Success() {
        when(agendamentoService.findById("1")).thenReturn(Optional.of(agendamento1));

        ResponseEntity<Agendamento> response = agendamentoController.getAgendamentoById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("cli1", response.getBody().getClienteId());
        verify(agendamentoService, times(1)).findById("1");
    }

    @Test
    void testGetAgendamentoById_NotFound() {
        when(agendamentoService.findById("999")).thenReturn(Optional.empty());

        ResponseEntity<Agendamento> response = agendamentoController.getAgendamentoById("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(agendamentoService, times(1)).findById("999");
    }

    @Test
    void testGetAgendamentosByClienteId() {
        List<Agendamento> agendamentos = Arrays.asList(agendamento1);
        when(agendamentoService.findAgendamentosPorCliente("cli1")).thenReturn(agendamentos);

        ResponseEntity<List<Agendamento>> response = agendamentoController.getAgendamentosByCliente("cli1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("cli1", response.getBody().get(0).getClienteId());
        verify(agendamentoService, times(1)).findAgendamentosPorCliente("cli1");
    }

    @Test
    void testGetAgendamentosByStatus() {
        List<Agendamento> pendentes = Arrays.asList(agendamento1);
        when(agendamentoService.findByStatus("PENDENTE")).thenReturn(pendentes);

        ResponseEntity<List<Agendamento>> response = agendamentoController.getAgendamentosByStatus("PENDENTE");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("PENDENTE", response.getBody().get(0).getStatus());
        verify(agendamentoService, times(1)).findByStatus("PENDENTE");
    }

    @Test
    void testCreateAgendamento_Success() throws Exception {
        when(agendamentoService.save(any(Agendamento.class))).thenReturn(agendamento1);

        mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(agendamento1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId").value("cli1"));

        verify(agendamentoService, times(1)).save(any(Agendamento.class));
    }

    @Test
    void testCreateAgendamento_ValidationError() throws Exception {
        Agendamento agendamentoInvalido = new Agendamento();
        agendamentoInvalido.setClienteId("");

        mockMvc.perform(post("/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(agendamentoInvalido)))
                .andExpect(status().isBadRequest());

        verify(agendamentoService, never()).save(any(Agendamento.class));
    }

    @Test
    void testUpdateAgendamento_Success() throws Exception {
        when(agendamentoService.update(eq("1"), any(Agendamento.class))).thenReturn(agendamento1);

        mockMvc.perform(put("/agendamentos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(agendamento1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value("cli1"));

        verify(agendamentoService, times(1)).update(eq("1"), any(Agendamento.class));
    }

    @Test
    void testUpdateAgendamento_NotFound() throws Exception {
        when(agendamentoService.update(eq("999"), any(Agendamento.class))).thenThrow(new RuntimeException("Agendamento não encontrado"));

        mockMvc.perform(put("/agendamentos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(agendamento1)))
                .andExpect(status().isNotFound());

        verify(agendamentoService, times(1)).update(eq("999"), any(Agendamento.class));
    }

    @Test
    void testDeleteAgendamento() {
        when(agendamentoService.existsById("1")).thenReturn(true);
        doNothing().when(agendamentoService).deleteById("1");

        ResponseEntity<Void> response = agendamentoController.deleteAgendamento("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(agendamentoService, times(1)).existsById("1");
        verify(agendamentoService, times(1)).deleteById("1");
    }

    @Test
    void testDeleteAgendamento_NotFound() {
        when(agendamentoService.existsById("999")).thenReturn(false);

        ResponseEntity<Void> response = agendamentoController.deleteAgendamento("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(agendamentoService, times(1)).existsById("999");
        verify(agendamentoService, never()).deleteById(anyString());
    }

    // TODO: Implementar quando o método alterarStatusAgendamento for adicionado ao controller
    /*
    @Test
    void testAlterarStatusAgendamento_Success() {
        Agendamento agendamento = new Agendamento();
        agendamento.setId("1");
        agendamento.setStatus("PENDENTE");

        Agendamento agendamentoAtualizado = new Agendamento();
        agendamentoAtualizado.setId("1");
        agendamentoAtualizado.setStatus("CONCLUIDO");

        when(agendamentoService.findById("1")).thenReturn(Optional.of(agendamento));
        when(agendamentoService.save(any(Agendamento.class))).thenReturn(agendamentoAtualizado);

        ResponseEntity<Agendamento> response = agendamentoController.alterarStatusAgendamento("1", "CONCLUIDO");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("CONCLUIDO", response.getBody().getStatus());
        verify(agendamentoService, times(1)).findById("1");
        verify(agendamentoService, times(1)).save(any(Agendamento.class));
    }

    @Test
    void testAlterarStatusAgendamento_NotFound() {
        when(agendamentoService.findById("999")).thenReturn(Optional.empty());

        ResponseEntity<Agendamento> response = agendamentoController.alterarStatusAgendamento("999", "CONCLUIDO");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(agendamentoService, times(1)).findById("999");
        verify(agendamentoService, never()).save(any(Agendamento.class));
    }
    */
} 