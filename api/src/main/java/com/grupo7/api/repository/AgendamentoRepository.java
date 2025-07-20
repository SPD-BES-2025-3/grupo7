package com.grupo7.api.repository;

import com.grupo7.api.model.Agendamento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends MongoRepository<Agendamento, String> {

    List<Agendamento> findByClienteId(String clienteId);
    List<Agendamento> findByPetId(String petId);
    List<Agendamento> findByStatus(String status);
    List<Agendamento> findByServico(String servico);
    List<Agendamento> findByFuncionarioId(String funcionarioId);
    
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Agendamento> findByDataHoraGreaterThan(LocalDateTime dataHora);
    List<Agendamento> findByDataHoraLessThan(LocalDateTime dataHora);
    
    @Query("{'servico': {$regex: ?0, $options: 'i'}}")
    List<Agendamento> findByServicoContainingIgnoreCase(String servico);
    
    List<Agendamento> findByClienteIdAndStatus(String clienteId, String status);
    List<Agendamento> findByPetIdAndStatus(String petId, String status);
    
    @Query("{'dataHora': {$gte: ?0, $lte: ?1}, 'status': {$ne: 'CANCELADO'}}")
    List<Agendamento> findAgendamentosAtivosPorPeriodo(LocalDateTime inicio, LocalDateTime fim);
} 