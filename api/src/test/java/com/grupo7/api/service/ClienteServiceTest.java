package com.grupo7.api.service;

import com.grupo7.api.model.Cliente;
import com.grupo7.api.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente1;
    private Cliente cliente2;

    @BeforeEach
    void setUp() {
        cliente1 = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        cliente1.setId("1");
        cliente1.setAtivo(true);

        cliente2 = new Cliente("Maria Santos", "maria@email.com", "(11) 88888-8888", "987.654.321-00");
        cliente2.setId("2");
        cliente2.setAtivo(false);
    }

    @Test
    void testFindAll() {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);
        when(clienteRepository.findAll()).thenReturn(clientes);

        // When
        List<Cliente> result = clienteService.findAll();

        // Then
        assertEquals(2, result.size());
        assertEquals("João Silva", result.get(0).getNome());
        assertEquals("Maria Santos", result.get(1).getNome());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        // Given
        when(clienteRepository.findById("1")).thenReturn(Optional.of(cliente1));

        // When
        Optional<Cliente> result = clienteService.findById("1");

        // Then
        assertTrue(result.isPresent());
        assertEquals("João Silva", result.get().getNome());
        verify(clienteRepository, times(1)).findById("1");
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(clienteRepository.findById("999")).thenReturn(Optional.empty());

        // When
        Optional<Cliente> result = clienteService.findById("999");

        // Then
        assertFalse(result.isPresent());
        verify(clienteRepository, times(1)).findById("999");
    }

    @Test
    void testFindByEmail_Success() {
        // Given
        when(clienteRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(cliente1));

        // When
        Optional<Cliente> result = clienteService.findByEmail("joao@email.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("joao@email.com", result.get().getEmail());
        verify(clienteRepository, times(1)).findByEmail("joao@email.com");
    }

    @Test
    void testFindByEmail_NotFound() {
        // Given
        when(clienteRepository.findByEmail("inexistente@email.com")).thenReturn(Optional.empty());

        // When
        Optional<Cliente> result = clienteService.findByEmail("inexistente@email.com");

        // Then
        assertFalse(result.isPresent());
        verify(clienteRepository, times(1)).findByEmail("inexistente@email.com");
    }

    @Test
    void testFindByCpf_Success() {
        // Given
        when(clienteRepository.findByCpf("123.456.789-00")).thenReturn(Optional.of(cliente1));

        // When
        Optional<Cliente> result = clienteService.findByCpf("123.456.789-00");

        // Then
        assertTrue(result.isPresent());
        assertEquals("123.456.789-00", result.get().getCpf());
        verify(clienteRepository, times(1)).findByCpf("123.456.789-00");
    }

    @Test
    void testFindByCpf_NotFound() {
        // Given
        when(clienteRepository.findByCpf("000.000.000-00")).thenReturn(Optional.empty());

        // When
        Optional<Cliente> result = clienteService.findByCpf("000.000.000-00");

        // Then
        assertFalse(result.isPresent());
        verify(clienteRepository, times(1)).findByCpf("000.000.000-00");
    }

    @Test
    void testFindByAtivo() {
        // Given
        List<Cliente> clientesAtivos = Arrays.asList(cliente1);
        when(clienteRepository.findByAtivo(true)).thenReturn(clientesAtivos);

        // When
        List<Cliente> result = clienteService.findByAtivo(true);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).isAtivo());
        verify(clienteRepository, times(1)).findByAtivo(true);
    }

    @Test
    void testFindByNomeContaining() {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente1);
        when(clienteRepository.findByNomeContainingIgnoreCase("João")).thenReturn(clientes);

        // When
        List<Cliente> result = clienteService.findByNomeContaining("João");

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).getNome().contains("João"));
        verify(clienteRepository, times(1)).findByNomeContainingIgnoreCase("João");
    }

    @Test
    void testFindByEmailContaining() {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente1);
        when(clienteRepository.findByEmailContainingIgnoreCase("joao")).thenReturn(clientes);

        // When
        List<Cliente> result = clienteService.findByEmailContaining("joao");

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).getEmail().contains("joao"));
        verify(clienteRepository, times(1)).findByEmailContainingIgnoreCase("joao");
    }

    @Test
    void testFindByTelefoneContaining() {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente1);
        when(clienteRepository.findByTelefoneContaining("99999")).thenReturn(clientes);

        // When
        List<Cliente> result = clienteService.findByTelefoneContaining("99999");

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).getTelefone().contains("99999"));
        verify(clienteRepository, times(1)).findByTelefoneContaining("99999");
    }

    @Test
    void testSave_NewCliente_Success() {
        // Given
        Cliente novoCliente = new Cliente("Pedro Costa", "pedro@email.com", "(11) 77777-7777", "111.222.333-44");
        when(clienteRepository.existsByEmail("pedro@email.com")).thenReturn(false);
        when(clienteRepository.existsByCpf("111.222.333-44")).thenReturn(false);
        when(clienteRepository.save(novoCliente)).thenReturn(novoCliente);

        // When
        Cliente result = clienteService.save(novoCliente);

        // Then
        assertNotNull(result);
        assertEquals("Pedro Costa", result.getNome());
        verify(clienteRepository, times(1)).existsByEmail("pedro@email.com");
        verify(clienteRepository, times(1)).existsByCpf("111.222.333-44");
        verify(clienteRepository, times(1)).save(novoCliente);
    }

    @Test
    void testSave_NewCliente_EmailAlreadyExists() {
        // Given
        Cliente novoCliente = new Cliente("Pedro Costa", "joao@email.com", "(11) 77777-7777", "111.222.333-44");
        when(clienteRepository.existsByEmail("joao@email.com")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.save(novoCliente);
        });

        assertEquals("Email já cadastrado", exception.getMessage());
        verify(clienteRepository, times(1)).existsByEmail("joao@email.com");
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void testSave_NewCliente_CpfAlreadyExists() {
        // Given
        Cliente novoCliente = new Cliente("Pedro Costa", "pedro@email.com", "(11) 77777-7777", "123.456.789-00");
        when(clienteRepository.existsByEmail("pedro@email.com")).thenReturn(false);
        when(clienteRepository.existsByCpf("123.456.789-00")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.save(novoCliente);
        });

        assertEquals("CPF já cadastrado", exception.getMessage());
        verify(clienteRepository, times(1)).existsByEmail("pedro@email.com");
        verify(clienteRepository, times(1)).existsByCpf("123.456.789-00");
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void testSave_UpdateCliente_Success() {
        // Given
        Cliente clienteAtualizado = new Cliente("João Silva Atualizado", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        clienteAtualizado.setId("1");
        
        when(clienteRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(cliente1));
        when(clienteRepository.findByCpf("123.456.789-00")).thenReturn(Optional.of(cliente1));
        when(clienteRepository.save(clienteAtualizado)).thenReturn(clienteAtualizado);

        // When
        Cliente result = clienteService.save(clienteAtualizado);

        // Then
        assertNotNull(result);
        assertEquals("João Silva Atualizado", result.getNome());
        verify(clienteRepository, times(1)).findByEmail("joao@email.com");
        verify(clienteRepository, times(1)).findByCpf("123.456.789-00");
        verify(clienteRepository, times(1)).save(clienteAtualizado);
    }

    @Test
    void testSave_UpdateCliente_EmailExistsForOtherCliente() {
        // Given
        Cliente clienteAtualizado = new Cliente("João Silva", "maria@email.com", "(11) 99999-9999", "123.456.789-00");
        clienteAtualizado.setId("1");
        
        when(clienteRepository.findByEmail("maria@email.com")).thenReturn(Optional.of(cliente2));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.save(clienteAtualizado);
        });

        assertEquals("Email já cadastrado para outro cliente", exception.getMessage());
        verify(clienteRepository, times(1)).findByEmail("maria@email.com");
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void testUpdate_Success() {
        // Given
        Cliente clienteAtualizado = new Cliente("João Silva Atualizado", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        when(clienteRepository.findById("1")).thenReturn(Optional.of(cliente1));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteAtualizado);

        // When
        Cliente result = clienteService.update("1", clienteAtualizado);

        // Then
        assertNotNull(result);
        assertEquals("João Silva Atualizado", result.getNome());
        verify(clienteRepository, times(1)).findById("1");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testUpdate_NotFound() {
        // Given
        Cliente clienteAtualizado = new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00");
        when(clienteRepository.findById("999")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.update("999", clienteAtualizado);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clienteRepository, times(1)).findById("999");
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void testDeleteById() {
        // Given
        doNothing().when(clienteRepository).deleteById("1");

        // When
        clienteService.deleteById("1");

        // Then
        verify(clienteRepository, times(1)).deleteById("1");
    }

    @Test
    void testExistsById() {
        // Given
        when(clienteRepository.existsById("1")).thenReturn(true);
        when(clienteRepository.existsById("999")).thenReturn(false);

        // When
        boolean exists = clienteService.existsById("1");
        boolean notExists = clienteService.existsById("999");

        // Then
        assertTrue(exists);
        assertFalse(notExists);
        verify(clienteRepository, times(1)).existsById("1");
        verify(clienteRepository, times(1)).existsById("999");
    }

    @Test
    void testExistsByEmail() {
        // Given
        when(clienteRepository.existsByEmail("joao@email.com")).thenReturn(true);
        when(clienteRepository.existsByEmail("inexistente@email.com")).thenReturn(false);

        // When
        boolean exists = clienteService.existsByEmail("joao@email.com");
        boolean notExists = clienteService.existsByEmail("inexistente@email.com");

        // Then
        assertTrue(exists);
        assertFalse(notExists);
        verify(clienteRepository, times(1)).existsByEmail("joao@email.com");
        verify(clienteRepository, times(1)).existsByEmail("inexistente@email.com");
    }

    @Test
    void testExistsByCpf() {
        // Given
        when(clienteRepository.existsByCpf("123.456.789-00")).thenReturn(true);
        when(clienteRepository.existsByCpf("000.000.000-00")).thenReturn(false);

        // When
        boolean exists = clienteService.existsByCpf("123.456.789-00");
        boolean notExists = clienteService.existsByCpf("000.000.000-00");

        // Then
        assertTrue(exists);
        assertFalse(notExists);
        verify(clienteRepository, times(1)).existsByCpf("123.456.789-00");
        verify(clienteRepository, times(1)).existsByCpf("000.000.000-00");
    }

    @Test
    void testFindClientesAtivos() {
        // Given
        List<Cliente> clientesAtivos = Arrays.asList(cliente1);
        when(clienteRepository.findByAtivo(true)).thenReturn(clientesAtivos);

        // When
        List<Cliente> result = clienteService.findClientesAtivos();

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).isAtivo());
        verify(clienteRepository, times(1)).findByAtivo(true);
    }

    @Test
    void testBuscarPorNomeOuEmail() {
        // Given
        List<Cliente> clientesPorNome = new ArrayList<>(Arrays.asList(cliente1));
        List<Cliente> clientesPorEmail = new ArrayList<>(Arrays.asList(cliente2)); // Cliente diferente
        when(clienteRepository.findByNomeContainingIgnoreCase("João")).thenReturn(clientesPorNome);
        when(clienteRepository.findByEmailContainingIgnoreCase("João")).thenReturn(clientesPorEmail);

        // When
        List<Cliente> result = clienteService.buscarPorNomeOuEmail("João");

        // Then
        assertEquals(2, result.size()); // Combinação de busca por nome e email
        verify(clienteRepository, times(1)).findByNomeContainingIgnoreCase("João");
        verify(clienteRepository, times(1)).findByEmailContainingIgnoreCase("João");
    }
} 