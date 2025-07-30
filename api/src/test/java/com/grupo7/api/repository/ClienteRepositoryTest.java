package com.grupo7.api.repository;

import com.grupo7.api.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteRepositoryTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private MongoTemplate mongoTemplate;

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
        List<Cliente> result = clienteRepository.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("João Silva", result.get(0).getNome());
        assertEquals("Maria Santos", result.get(1).getNome());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Given
        when(clienteRepository.findById("1")).thenReturn(Optional.of(cliente1));
        when(clienteRepository.findById("999")).thenReturn(Optional.empty());

        // When
        Optional<Cliente> found = clienteRepository.findById("1");
        Optional<Cliente> notFound = clienteRepository.findById("999");

        // Then
        assertTrue(found.isPresent());
        assertEquals("João Silva", found.get().getNome());
        assertFalse(notFound.isPresent());
        verify(clienteRepository, times(1)).findById("1");
        verify(clienteRepository, times(1)).findById("999");
    }

    @Test
    void testFindByEmail() {
        // Given
        when(clienteRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(cliente1));
        when(clienteRepository.findByEmail("inexistente@email.com")).thenReturn(Optional.empty());

        // When
        Optional<Cliente> found = clienteRepository.findByEmail("joao@email.com");
        Optional<Cliente> notFound = clienteRepository.findByEmail("inexistente@email.com");

        // Then
        assertTrue(found.isPresent());
        assertEquals("joao@email.com", found.get().getEmail());
        assertFalse(notFound.isPresent());
        verify(clienteRepository, times(1)).findByEmail("joao@email.com");
        verify(clienteRepository, times(1)).findByEmail("inexistente@email.com");
    }

    @Test
    void testFindByCpf() {
        // Given
        when(clienteRepository.findByCpf("123.456.789-00")).thenReturn(Optional.of(cliente1));
        when(clienteRepository.findByCpf("000.000.000-00")).thenReturn(Optional.empty());

        // When
        Optional<Cliente> found = clienteRepository.findByCpf("123.456.789-00");
        Optional<Cliente> notFound = clienteRepository.findByCpf("000.000.000-00");

        // Then
        assertTrue(found.isPresent());
        assertEquals("123.456.789-00", found.get().getCpf());
        assertFalse(notFound.isPresent());
        verify(clienteRepository, times(1)).findByCpf("123.456.789-00");
        verify(clienteRepository, times(1)).findByCpf("000.000.000-00");
    }

    @Test
    void testFindByAtivo() {
        // Given
        List<Cliente> clientesAtivos = Arrays.asList(cliente1);
        List<Cliente> clientesInativos = Arrays.asList(cliente2);
        
        when(clienteRepository.findByAtivo(true)).thenReturn(clientesAtivos);
        when(clienteRepository.findByAtivo(false)).thenReturn(clientesInativos);

        // When
        List<Cliente> ativos = clienteRepository.findByAtivo(true);
        List<Cliente> inativos = clienteRepository.findByAtivo(false);

        // Then
        assertEquals(1, ativos.size());
        assertTrue(ativos.get(0).isAtivo());
        assertEquals(1, inativos.size());
        assertFalse(inativos.get(0).isAtivo());
        verify(clienteRepository, times(1)).findByAtivo(true);
        verify(clienteRepository, times(1)).findByAtivo(false);
    }

    @Test
    void testFindByNomeContainingIgnoreCase() {
        // Given
        List<Cliente> clientes = Arrays.asList(cliente1);
        when(clienteRepository.findByNomeContainingIgnoreCase("João")).thenReturn(clientes);

        // When
        List<Cliente> result = clienteRepository.findByNomeContainingIgnoreCase("João");

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).getNome().contains("João"));
        verify(clienteRepository, times(1)).findByNomeContainingIgnoreCase("João");
    }

    @Test
    void testSave() {
        // Given
        Cliente novoCliente = new Cliente("Pedro Costa", "pedro@email.com", "(11) 77777-7777", "111.222.333-44");
        when(clienteRepository.save(any(Cliente.class))).thenReturn(novoCliente);

        // When
        Cliente saved = clienteRepository.save(novoCliente);

        // Then
        assertNotNull(saved);
        assertEquals("Pedro Costa", saved.getNome());
        assertEquals("pedro@email.com", saved.getEmail());
        verify(clienteRepository, times(1)).save(novoCliente);
    }

    @Test
    void testDeleteById() {
        // Given
        doNothing().when(clienteRepository).deleteById("1");

        // When
        clienteRepository.deleteById("1");

        // Then
        verify(clienteRepository, times(1)).deleteById("1");
    }

    @Test
    void testExistsById() {
        // Given
        when(clienteRepository.existsById("1")).thenReturn(true);
        when(clienteRepository.existsById("999")).thenReturn(false);

        // When
        boolean exists = clienteRepository.existsById("1");
        boolean notExists = clienteRepository.existsById("999");

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
        boolean exists = clienteRepository.existsByEmail("joao@email.com");
        boolean notExists = clienteRepository.existsByEmail("inexistente@email.com");

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
        boolean exists = clienteRepository.existsByCpf("123.456.789-00");
        boolean notExists = clienteRepository.existsByCpf("000.000.000-00");

        // Then
        assertTrue(exists);
        assertFalse(notExists);
        verify(clienteRepository, times(1)).existsByCpf("123.456.789-00");
        verify(clienteRepository, times(1)).existsByCpf("000.000.000-00");
    }
} 