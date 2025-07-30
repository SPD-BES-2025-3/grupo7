package com.grupo7.api.repository;

import com.grupo7.api.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioRepositoryTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario();
        usuario1.setId("1");
        usuario1.setNome("João");
        usuario1.setEmail("joao@email.com");
        usuario1.setSenha("senha123");
        usuario1.setTelefone("11999999999");
        usuario1.setAtivo(true);

        usuario2 = new Usuario();
        usuario2.setId("2");
        usuario2.setNome("Maria");
        usuario2.setEmail("maria@email.com");
        usuario2.setSenha("senha456");
        usuario2.setTelefone("11888888888");
        usuario2.setAtivo(false);
    }

    @Test
    void testFindAll() {
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        List<Usuario> result = usuarioRepository.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("João", result.get(0).getNome());
        assertEquals("Maria", result.get(1).getNome());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario1));
        when(usuarioRepository.findById("999")).thenReturn(Optional.empty());
        Optional<Usuario> found = usuarioRepository.findById("1");
        Optional<Usuario> notFound = usuarioRepository.findById("999");
        assertTrue(found.isPresent());
        assertEquals("João", found.get().getNome());
        assertFalse(notFound.isPresent());
        verify(usuarioRepository, times(1)).findById("1");
        verify(usuarioRepository, times(1)).findById("999");
    }

    @Test
    void testFindByEmail() {
        when(usuarioRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(usuario1));
        when(usuarioRepository.findByEmail("inexistente@email.com")).thenReturn(Optional.empty());
        Optional<Usuario> found = usuarioRepository.findByEmail("joao@email.com");
        Optional<Usuario> notFound = usuarioRepository.findByEmail("inexistente@email.com");
        assertTrue(found.isPresent());
        assertEquals("joao@email.com", found.get().getEmail());
        assertFalse(notFound.isPresent());
        verify(usuarioRepository, times(1)).findByEmail("joao@email.com");
        verify(usuarioRepository, times(1)).findByEmail("inexistente@email.com");
    }

    @Test
    void testFindByAtivo() {
        List<Usuario> usuariosAtivos = Arrays.asList(usuario1);
        List<Usuario> usuariosInativos = Arrays.asList(usuario2);
        when(usuarioRepository.findByAtivo(true)).thenReturn(usuariosAtivos);
        when(usuarioRepository.findByAtivo(false)).thenReturn(usuariosInativos);
        List<Usuario> ativos = usuarioRepository.findByAtivo(true);
        List<Usuario> inativos = usuarioRepository.findByAtivo(false);
        assertEquals(1, ativos.size());
        assertTrue(ativos.get(0).isAtivo());
        assertEquals(1, inativos.size());
        assertFalse(inativos.get(0).isAtivo());
        verify(usuarioRepository, times(1)).findByAtivo(true);
        verify(usuarioRepository, times(1)).findByAtivo(false);
    }

    @Test
    void testSave() {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("Carlos");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(novoUsuario);
        Usuario saved = usuarioRepository.save(novoUsuario);
        assertNotNull(saved);
        assertEquals("Carlos", saved.getNome());
        verify(usuarioRepository, times(1)).save(novoUsuario);
    }

    @Test
    void testDeleteById() {
        doNothing().when(usuarioRepository).deleteById("1");
        usuarioRepository.deleteById("1");
        verify(usuarioRepository, times(1)).deleteById("1");
    }

    @Test
    void testExistsById() {
        when(usuarioRepository.existsById("1")).thenReturn(true);
        when(usuarioRepository.existsById("999")).thenReturn(false);
        boolean exists = usuarioRepository.existsById("1");
        boolean notExists = usuarioRepository.existsById("999");
        assertTrue(exists);
        assertFalse(notExists);
        verify(usuarioRepository, times(1)).existsById("1");
        verify(usuarioRepository, times(1)).existsById("999");
    }
}