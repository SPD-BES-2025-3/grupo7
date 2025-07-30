package com.grupo7.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo7.api.model.Venda;
import com.grupo7.api.service.VendaService;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class VendaControllerTest {

    @Mock
    private VendaService vendaService;

    @InjectMocks
    private VendaController vendaController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Venda venda1;
    private Venda venda2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(vendaController).build();
        objectMapper = new ObjectMapper();

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
    void testGetAllVendas() {
        List<Venda> vendas = Arrays.asList(venda1, venda2);
        when(vendaService.findAll()).thenReturn(vendas);

        ResponseEntity<List<Venda>> response = vendaController.getAllVendas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(vendaService, times(1)).findAll();
    }

    @Test
    void testGetVendaById_Success() {
        when(vendaService.findById("1")).thenReturn(Optional.of(venda1));

        ResponseEntity<Venda> response = vendaController.getVendaById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("cli1", response.getBody().getClienteId());
        verify(vendaService, times(1)).findById("1");
    }

    @Test
    void testGetVendaById_NotFound() {
        when(vendaService.findById("999")).thenReturn(Optional.empty());

        ResponseEntity<Venda> response = vendaController.getVendaById("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(vendaService, times(1)).findById("999");
    }

    @Test
    void testGetVendasByClienteId() {
        List<Venda> vendas = Arrays.asList(venda1);
        when(vendaService.findByClienteId("cli1")).thenReturn(vendas);

        ResponseEntity<List<Venda>> response = vendaController.getVendasByCliente("cli1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("cli1", response.getBody().get(0).getClienteId());
        verify(vendaService, times(1)).findByClienteId("cli1");
    }

    @Test
    void testGetVendasByStatus() {
        List<Venda> vendasConcluidas = Arrays.asList(venda1);
        when(vendaService.findByStatus("CONCLUIDA")).thenReturn(vendasConcluidas);

        ResponseEntity<List<Venda>> response = vendaController.getVendasByStatus("CONCLUIDA");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("CONCLUIDA", response.getBody().get(0).getStatus());
        verify(vendaService, times(1)).findByStatus("CONCLUIDA");
    }

    @Test
    void testCreateVenda_Success() throws Exception {
        when(vendaService.save(any(Venda.class))).thenReturn(venda1);

        mockMvc.perform(post("/vendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venda1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId").value("cli1"));

        verify(vendaService, times(1)).save(any(Venda.class));
    }

    @Test
    void testCreateVenda_ValidationError() throws Exception {
        Venda vendaInvalida = new Venda();
        vendaInvalida.setClienteId("");

        mockMvc.perform(post("/vendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vendaInvalida)))
                .andExpect(status().isBadRequest());

        verify(vendaService, never()).save(any(Venda.class));
    }

    @Test
    void testUpdateVenda_Success() throws Exception {
        when(vendaService.update(eq("1"), any(Venda.class))).thenReturn(venda1);

        mockMvc.perform(put("/vendas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venda1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value("cli1"));

        verify(vendaService, times(1)).update(eq("1"), any(Venda.class));
    }

    @Test
    void testUpdateVenda_NotFound() throws Exception {
        when(vendaService.update(eq("999"), any(Venda.class))).thenThrow(new RuntimeException("Venda não encontrada"));

        mockMvc.perform(put("/vendas/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venda1)))
                .andExpect(status().isNotFound());

        verify(vendaService, times(1)).update(eq("999"), any(Venda.class));
    }

    @Test
    void testDeleteVenda() {
        when(vendaService.existsById("1")).thenReturn(true);
        doNothing().when(vendaService).deleteById("1");

        ResponseEntity<Void> response = vendaController.deleteVenda("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(vendaService, times(1)).existsById("1");
        verify(vendaService, times(1)).deleteById("1");
    }

    @Test
    void testDeleteVenda_NotFound() {
        when(vendaService.existsById("999")).thenReturn(false);

        ResponseEntity<Void> response = vendaController.deleteVenda("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(vendaService, times(1)).existsById("999");
        verify(vendaService, never()).deleteById(anyString());
    }

    // TODO: Implementar quando o método alterarStatusVenda for adicionado ao controller
    /*
    @Test
    void testAlterarStatusVenda_Success() {
        Venda venda = new Venda();
        venda.setId("1");
        venda.setStatus("PENDENTE");

        Venda vendaAtualizada = new Venda();
        vendaAtualizada.setId("1");
        vendaAtualizada.setStatus("CONCLUIDA");

        when(vendaService.findById("1")).thenReturn(Optional.of(venda));
        when(vendaService.save(any(Venda.class))).thenReturn(vendaAtualizada);

        ResponseEntity<Venda> response = vendaController.alterarStatusVenda("1", "CONCLUIDA");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("CONCLUIDA", response.getBody().getStatus());
        verify(vendaService, times(1)).findById("1");
        verify(vendaService, times(1)).save(any(Venda.class));
    }

    @Test
    void testAlterarStatusVenda_NotFound() {
        when(vendaService.findById("999")).thenReturn(Optional.empty());

        ResponseEntity<Venda> response = vendaController.alterarStatusVenda("999", "CONCLUIDA");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(vendaService, times(1)).findById("999");
        verify(vendaService, never()).save(any(Venda.class));
    }
    */
} 