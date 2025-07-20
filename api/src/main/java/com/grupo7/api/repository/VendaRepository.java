package com.grupo7.api.repository;

import com.grupo7.api.model.Venda;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends MongoRepository<Venda, String> {

    List<Venda> findByClienteId(String clienteId);
    List<Venda> findByStatus(String status);
    List<Venda> findByFormaPagamento(String formaPagamento);
    List<Venda> findByTotalBetween(BigDecimal totalMin, BigDecimal totalMax);
    
    List<Venda> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Venda> findByDataPagamentoBetween(LocalDateTime inicio, LocalDateTime fim);
    
    @Query("{'status': 'PAGO'}")
    List<Venda> findVendasPagas();
    
    @Query("{'status': 'PENDENTE'}")
    List<Venda> findVendasPendentes();
    
    List<Venda> findByClienteIdAndStatus(String clienteId, String status);
    
    @Query("{'createdAt': {$gte: ?0, $lte: ?1}, 'status': 'PAGO'}")
    List<Venda> findVendasPagasPorPeriodo(LocalDateTime inicio, LocalDateTime fim);
} 