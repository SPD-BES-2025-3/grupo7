package com.grupo7.api.service;

import com.grupo7.api.model.Usuario;
import com.grupo7.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(String id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> findByAtivo(boolean ativo) {
        return usuarioRepository.findByAtivo(ativo);
    }

    public List<Usuario> findByNomeContaining(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Usuario save(Usuario usuario) {
        if (usuario.getId() != null && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario update(String id, Usuario usuario) {
        Optional<Usuario> existingUsuario = usuarioRepository.findById(id);
        if (existingUsuario.isPresent()) {
            Usuario updatedUsuario = existingUsuario.get();
            updatedUsuario.setNome(usuario.getNome());
            updatedUsuario.setEmail(usuario.getEmail());
            updatedUsuario.setTelefone(usuario.getTelefone());
            updatedUsuario.setAtivo(usuario.isAtivo());
            return usuarioRepository.save(updatedUsuario);
        }
        throw new RuntimeException("Usuário não encontrado");
    }

    public void deleteById(String id) {
        usuarioRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        return usuarioRepository.existsById(id);
    }
} 