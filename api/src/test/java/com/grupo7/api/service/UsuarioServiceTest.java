package com.grupo7.api.service;

import com.grupo7.api.model.Usuario;
import com.grupo7.api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario();
        usuario1.setId("1");
        usuario1.setNome("João");
        usuario1.setEmail("joao@email.com");
        usuario1.setSenha("senha123");
        usuario1.setRole("ADMIN");
        usuario1.setAtivo(true);

        usuario2 = new Usuario();
        usuario2.setId("2");
        usuario2.setNome("Maria");
        usuario2.setEmail("maria@email.com");
        usuario2.setSenha("senha456");
        usuario2.setRole("USER");
        usuario2.setAtivo(false);
    }

    @Test
    void testFindAll() {
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        List<Usuario> result = usuarioService.findAll();
        assertEquals(2, result.size());
        assertEquals("João", result.get(0).getNome());
        assertEquals("Maria", result.get(1).getNome());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario1));
        Optional<Usuario> result = usuarioService.findById("1");
        assertTrue(result.isPresent());
        assertEquals("João", result.get().getNome());
        verify(usuarioRepository, times(1)).findById("1");
    }

    @Test
    void testFindById_NotFound() {
        when(usuarioRepository.findById("999")).thenReturn(Optional.empty());
        Optional<Usuario> result = usuarioService.findById("999");
        assertFalse(result.isPresent());
        verify(usuarioRepository, times(1)).findById("999");
    }

    @Test
    void testFindByEmail() {
        when(usuarioRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(usuario1));
        Optional<Usuario> result = usuarioService.findByEmail("joao@email.com");
        assertTrue(result.isPresent());
        assertEquals("joao@email.com", result.get().getEmail());
        verify(usuarioRepository, times(1)).findByEmail("joao@email.com");
    }

    @Test
    void testFindByAtivo() {
        List<Usuario> usuariosAtivos = Arrays.asList(usuario1);
        when(usuarioRepository.findByAtivo(true)).thenReturn(usuariosAtivos);
        List<Usuario> result = usuarioService.findByAtivo(true);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isAtivo());
        verify(usuarioRepository, times(1)).findByAtivo(true);
    }

    @Test
    void testSave() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario1);
        Usuario saved = usuarioService.save(usuario1);
        assertNotNull(saved);
        assertEquals("João", saved.getNome());
        verify(usuarioRepository, times(1)).save(usuario1);
    }

    @Test
    void testUpdate_Success() {
        when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario1));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario1);
        Usuario updated = usuarioService.update("1", usuario1);
        assertNotNull(updated);
        assertEquals("João", updated.getNome());
        verify(usuarioRepository, times(1)).findById("1");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testUpdate_NotFound() {
        when(usuarioRepository.findById("999")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> usuarioService.update("999", usuario1));
        verify(usuarioRepository, times(1)).findById("999");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testDeleteById() {
        doNothing().when(usuarioRepository).deleteById("1");
        usuarioService.deleteById("1");
        verify(usuarioRepository, times(1)).deleteById("1");
    }

    @Test
    void testExistsById() {
        when(usuarioRepository.existsById("1")).thenReturn(true);
        when(usuarioRepository.existsById("999")).thenReturn(false);
        boolean exists = usuarioService.existsById("1");
        boolean notExists = usuarioService.existsById("999");
        assertTrue(exists);
        assertFalse(notExists);
        verify(usuarioRepository, times(1)).existsById("1");
        verify(usuarioRepository, times(1)).existsById("999");
    }
}