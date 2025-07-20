package com.grupo7.api.repository;

import com.grupo7.api.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<Usuario> findByAtivo(boolean ativo);
    
    @Query("{'nome': {$regex: ?0, $options: 'i'}}")
    List<Usuario> findByNomeContainingIgnoreCase(String nome);
    
    @Query("{'email': {$regex: ?0, $options: 'i'}}")
    List<Usuario> findByEmailContainingIgnoreCase(String email);
} 