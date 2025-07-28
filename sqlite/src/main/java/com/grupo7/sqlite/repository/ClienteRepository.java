package com.grupo7.sqlite.repository;

import com.grupo7.sqlite.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmail(String email);
    
    Optional<Cliente> findByCpf(String cpf);
    
    List<Cliente> findByAtivo(Integer ativo);
    
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    
    @Query("SELECT c FROM Cliente c WHERE c.nome LIKE %:termo% OR c.email LIKE %:termo%")
    List<Cliente> findByNomeOrEmailContaining(@Param("termo") String termo);
    
    boolean existsByEmail(String email);
    
    boolean existsByCpf(String cpf);
} 