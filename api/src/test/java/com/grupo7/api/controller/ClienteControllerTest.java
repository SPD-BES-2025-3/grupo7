package com.grupo7.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo7.api.model.Cliente;
import com.grupo7.api.service.ClienteService;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Cliente cliente1;
    private Cliente cliente2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
        objectMapper = new ObjectMapper();

        cliente1 = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        cliente1.setId("1");
        cliente1.setAtivo(true);

        cliente2 = new Cliente("Maria Santos", "maria@email.com", "(11) 88888-8888", "987.654.321-00");
        cliente2.setId("2");
        cliente2.setAtivo(false);
    }

    @Test
    void testGetAllClientes() {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);
        when(clienteService.findAll()).thenReturn(clientes);

        // When
        ResponseEntity<List<Cliente>> response = clienteController.getAllClientes();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("João Silva", response.getBody().get(0).getNome());
        assertEquals("Maria Santos", response.getBody().get(1).getNome());
        verify(clienteService, times(1)).findAll();
    }

    @Test
    void testGetClienteById_Success() {
        // Given
        when(clienteService.findById("1")).thenReturn(Optional.of(cliente1));

        // When
        ResponseEntity<Cliente> response = clienteController.getClienteById("1");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null);
        assertEquals("João Silva", response.getBody().getNome());
        verify(clienteService, times(1)).findById("1");
    }

    @Test
    void testGetClienteById_NotFound() {
        // Given
        when(clienteService.findById("999")).thenReturn(Optional.empty());

        // When
        ResponseEntity<Cliente> response = clienteController.getClienteById("999");

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(clienteService, times(1)).findById("999");
    }

    @Test
    void testGetClienteByEmail_Success() {
        // Given
        when(clienteService.findByEmail("joao@email.com")).thenReturn(Optional.of(cliente1));

        // When
        ResponseEntity<Cliente> response = clienteController.getClienteByEmail("joao@email.com");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null);
        assertEquals("joao@email.com", response.getBody().getEmail());
        verify(clienteService, times(1)).findByEmail("joao@email.com");
    }

    @Test
    void testGetClienteByEmail_NotFound() {
        // Given
        when(clienteService.findByEmail("inexistente@email.com")).thenReturn(Optional.empty());

        // When
        ResponseEntity<Cliente> response = clienteController.getClienteByEmail("inexistente@email.com");

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(clienteService, times(1)).findByEmail("inexistente@email.com");
    }

    @Test
    void testGetClienteByCpf_Success() {
        // Given
        when(clienteService.findByCpf("123.456.789-00")).thenReturn(Optional.of(cliente1));

        // When
        ResponseEntity<Cliente> response = clienteController.getClienteByCpf("123.456.789-00");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null);
        assertEquals("123.456.789-00", response.getBody().getCpf());
        verify(clienteService, times(1)).findByCpf("123.456.789-00");
    }

    @Test
    void testGetClienteByCpf_NotFound() {
        // Given
        when(clienteService.findByCpf("000.000.000-00")).thenReturn(Optional.empty());

        // When
        ResponseEntity<Cliente> response = clienteController.getClienteByCpf("000.000.000-00");

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(clienteService, times(1)).findByCpf("000.000.000-00");
    }

    @Test
    void testGetClientesAtivos() {
        // Given
        List<Cliente> clientesAtivos = Arrays.asList(cliente1);
        when(clienteService.findClientesAtivos()).thenReturn(clientesAtivos);

        // When
        ResponseEntity<List<Cliente>> response = clienteController.getClientesAtivos();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).isAtivo());
        verify(clienteService, times(1)).findClientesAtivos();
    }

    @Test
    void testBuscarPorNome() {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente1);
        when(clienteService.findByNomeContaining("João")).thenReturn(clientes);

        // When
        ResponseEntity<List<Cliente>> response = clienteController.buscarPorNome("João");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).getNome().contains("João"));
        verify(clienteService, times(1)).findByNomeContaining("João");
    }

    @Test
    void testBuscarPorEmail() {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente1);
        when(clienteService.findByEmailContaining("joao")).thenReturn(clientes);

        // When
        ResponseEntity<List<Cliente>> response = clienteController.buscarPorEmail("joao");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).getEmail().contains("joao"));
        verify(clienteService, times(1)).findByEmailContaining("joao");
    }

    @Test
    void testBuscarPorTelefone() {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente1);
        when(clienteService.findByTelefoneContaining("99999")).thenReturn(clientes);

        // When
        ResponseEntity<List<Cliente>> response = clienteController.buscarPorTelefone("99999");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).getTelefone().contains("99999"));
        verify(clienteService, times(1)).findByTelefoneContaining("99999");
    }

    @Test
    void testBuscarGeral() {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente1);
        when(clienteService.buscarPorNomeOuEmail("João")).thenReturn(clientes);

        // When
        ResponseEntity<List<Cliente>> response = clienteController.buscarGeral("João");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(clienteService, times(1)).buscarPorNomeOuEmail("João");
    }

    @Test
    void testCreateCliente_Success() throws Exception {
        // Given
        Cliente novoCliente = new Cliente("Pedro Costa", "pedro@email.com", "(11) 77777-7777", "111.222.333-44");
        when(clienteService.save(any(Cliente.class))).thenReturn(novoCliente);

        // When & Then
        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoCliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Pedro Costa"))
                .andExpect(jsonPath("$.email").value("pedro@email.com"));

        verify(clienteService, times(1)).save(any(Cliente.class));
    }

    @Test
    void testCreateCliente_ValidationError() throws Exception {
        // Given
        Cliente clienteInvalido = new Cliente("", "", "", "");

        // When & Then
        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInvalido)))
                .andExpect(status().isBadRequest());

        // Não deve chamar o service.save porque a validação falha antes
        verify(clienteService, never()).save(any(Cliente.class));
    }

    @Test
    void testUpdateCliente_Success() throws Exception {
        // Given
        Cliente clienteAtualizado = new Cliente("João Silva Atualizado", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        when(clienteService.update(eq("1"), any(Cliente.class))).thenReturn(clienteAtualizado);

        // When & Then
        mockMvc.perform(put("/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva Atualizado"));

        verify(clienteService, times(1)).update(eq("1"), any(Cliente.class));
    }

    @Test
    void testUpdateCliente_NotFound() throws Exception {
        // Given
        Cliente clienteAtualizado = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        when(clienteService.update(eq("999"), any(Cliente.class))).thenThrow(new RuntimeException("Cliente não encontrado"));

        // When & Then
        mockMvc.perform(put("/clientes/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteAtualizado)))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).update(eq("999"), any(Cliente.class));
    }

    @Test
    void testDeleteCliente() {
        // Given
        when(clienteService.existsById("1")).thenReturn(true);
        doNothing().when(clienteService).deleteById("1");

        // When
        ResponseEntity<Void> response = clienteController.deleteCliente("1");

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(clienteService, times(1)).existsById("1");
        verify(clienteService, times(1)).deleteById("1");
    }

    @Test
    void testAlterarStatusCliente_Success() {
        // Given
        Cliente cliente = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        cliente.setId("1");
        cliente.setAtivo(true);
        
        Cliente clienteAtualizado = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        clienteAtualizado.setId("1");
        clienteAtualizado.setAtivo(false);
        
        when(clienteService.findById("1")).thenReturn(Optional.of(cliente));
        when(clienteService.save(any(Cliente.class))).thenReturn(clienteAtualizado);

        // When
        ResponseEntity<Cliente> response = clienteController.alterarStatusCliente("1", false);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isAtivo());
        verify(clienteService, times(1)).findById("1");
        verify(clienteService, times(1)).save(any(Cliente.class));
    }

    @Test
    void testAlterarStatusCliente_NotFound() {
        // Given
        when(clienteService.findById("999")).thenReturn(Optional.empty());

        // When
        ResponseEntity<Cliente> response = clienteController.alterarStatusCliente("999", false);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(clienteService, times(1)).findById("999");
        verify(clienteService, never()).save(any(Cliente.class));
    }
} 