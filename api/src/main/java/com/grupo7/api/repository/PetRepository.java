package com.grupo7.api.repository;

import com.grupo7.api.model.Pet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends MongoRepository<Pet, String> {

    List<Pet> findByClienteId(String clienteId);
    List<Pet> findByAtivo(boolean ativo);
    List<Pet> findByEspecie(String especie);
    List<Pet> findByRaca(String raca);
    
    @Query("{'nome': {$regex: ?0, $options: 'i'}}")
    List<Pet> findByNomeContainingIgnoreCase(String nome);
    
    @Query("{'raca': {$regex: ?0, $options: 'i'}}")
    List<Pet> findByRacaContainingIgnoreCase(String raca);
    
    List<Pet> findByClienteIdAndAtivo(String clienteId, boolean ativo);
} 