package com.grupo7.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo7.api.model.Usuario;
import com.grupo7.api.service.UsuarioService;
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
public class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
        objectMapper = new ObjectMapper();

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
    void testGetAllUsuarios() {
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        when(usuarioService.findAll()).thenReturn(usuarios);

        ResponseEntity<List<Usuario>> response = usuarioController.getAllUsuarios();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(usuarioService, times(1)).findAll();
    }

    @Test
    void testGetUsuarioById_Success() {
        when(usuarioService.findById("1")).thenReturn(Optional.of(usuario1));

        ResponseEntity<Usuario> response = usuarioController.getUsuarioById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("João", response.getBody().getNome());
        verify(usuarioService, times(1)).findById("1");
    }

    @Test
    void testGetUsuarioById_NotFound() {
        when(usuarioService.findById("999")).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = usuarioController.getUsuarioById("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(usuarioService, times(1)).findById("999");
    }

    @Test
    void testGetUsuarioByEmail() {
        when(usuarioService.findByEmail("joao@email.com")).thenReturn(Optional.of(usuario1));

        ResponseEntity<Usuario> response = usuarioController.getUsuarioByEmail("joao@email.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("joao@email.com", response.getBody().getEmail());
        verify(usuarioService, times(1)).findByEmail("joao@email.com");
    }

    @Test
    void testGetUsuariosAtivos() {
        List<Usuario> usuariosAtivos = Arrays.asList(usuario1);
        when(usuarioService.findByAtivo(true)).thenReturn(usuariosAtivos);

        ResponseEntity<List<Usuario>> response = usuarioController.getUsuariosAtivos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).isAtivo());
        verify(usuarioService, times(1)).findByAtivo(true);
    }

    @Test
    void testCreateUsuario_Success() throws Exception {
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario1);

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João"));

        verify(usuarioService, times(1)).save(any(Usuario.class));
    }

    @Test
    void testCreateUsuario_ValidationError() throws Exception {
        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setNome("");

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioInvalido)))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never()).save(any(Usuario.class));
    }

    @Test
    void testUpdateUsuario_Success() throws Exception {
        when(usuarioService.update(eq("1"), any(Usuario.class))).thenReturn(usuario1);

        mockMvc.perform(put("/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João"));

        verify(usuarioService, times(1)).update(eq("1"), any(Usuario.class));
    }

    @Test
    void testUpdateUsuario_NotFound() throws Exception {
        when(usuarioService.update(eq("999"), any(Usuario.class))).thenThrow(new RuntimeException("Usuário não encontrado"));

        mockMvc.perform(put("/usuarios/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario1)))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).update(eq("999"), any(Usuario.class));
    }

    @Test
    void testDeleteUsuario() {
        when(usuarioService.existsById("1")).thenReturn(true);
        doNothing().when(usuarioService).deleteById("1");

        ResponseEntity<Void> response = usuarioController.deleteUsuario("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(usuarioService, times(1)).existsById("1");
        verify(usuarioService, times(1)).deleteById("1");
    }

    @Test
    void testDeleteUsuario_NotFound() {
        when(usuarioService.existsById("999")).thenReturn(false);

        ResponseEntity<Void> response = usuarioController.deleteUsuario("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(usuarioService, times(1)).existsById("999");
        verify(usuarioService, never()).deleteById(anyString());
    }

    @Test
    void testAlterarStatusUsuario_Success() {
        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setAtivo(true);

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId("1");
        usuarioAtualizado.setAtivo(false);

        when(usuarioService.findById("1")).thenReturn(Optional.of(usuario));
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

        ResponseEntity<Usuario> response = usuarioController.alterarStatusUsuario("1", false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isAtivo());
        verify(usuarioService, times(1)).findById("1");
        verify(usuarioService, times(1)).save(any(Usuario.class));
    }

    @Test
    void testAlterarStatusUsuario_NotFound() {
        when(usuarioService.findById("999")).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = usuarioController.alterarStatusUsuario("999", false);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(usuarioService, times(1)).findById("999");
        verify(usuarioService, never()).save(any(Usuario.class));
    }
} 