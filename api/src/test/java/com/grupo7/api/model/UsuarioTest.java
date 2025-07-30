package com.grupo7.api.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testUsuarioGettersSetters() {
        Usuario usuario = new Usuario("Paulo", "paulo@email.com", "senha123");
        usuario.setTelefone("11999999999");
        usuario.setAtivo(false);

        assertEquals("Paulo", usuario.getNome());
        assertEquals("paulo@email.com", usuario.getEmail());
        assertEquals("senha123", usuario.getSenha());
        assertEquals("11999999999", usuario.getTelefone());
        assertFalse(usuario.isAtivo());
    }
}
