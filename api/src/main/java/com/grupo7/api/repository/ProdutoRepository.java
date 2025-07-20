package com.grupo7.api.repository;

import com.grupo7.api.model.Produto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends MongoRepository<Produto, String> {

    Optional<Produto> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
    List<Produto> findByAtivo(boolean ativo);
    List<Produto> findByCategoria(String categoria);
    List<Produto> findByMarca(String marca);
    List<Produto> findByEstoqueLessThan(Integer estoque);
    List<Produto> findByPrecoBetween(BigDecimal precoMin, BigDecimal precoMax);
    
    @Query("{'nome': {$regex: ?0, $options: 'i'}}")
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    
    @Query("{'descricao': {$regex: ?0, $options: 'i'}}")
    List<Produto> findByDescricaoContainingIgnoreCase(String descricao);
    
    @Query("{'categoria': {$regex: ?0, $options: 'i'}}")
    List<Produto> findByCategoriaContainingIgnoreCase(String categoria);
    
    @Query("{'marca': {$regex: ?0, $options: 'i'}}")
    List<Produto> findByMarcaContainingIgnoreCase(String marca);
} 