package com.grupo7.api.repository;

import com.grupo7.api.model.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String> {

    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    List<Cliente> findByAtivo(boolean ativo);
    
    @Query("{'nome': {$regex: ?0, $options: 'i'}}")
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    
    @Query("{'email': {$regex: ?0, $options: 'i'}}")
    List<Cliente> findByEmailContainingIgnoreCase(String email);
    
    @Query("{'telefone': {$regex: ?0, $options: 'i'}}")
    List<Cliente> findByTelefoneContaining(String telefone);
} 