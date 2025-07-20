package com.grupo7.api.service;

import com.grupo7.api.model.Agendamento;
import com.grupo7.api.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    public List<Agendamento> findAll() {
        return agendamentoRepository.findAll();
    }

    public Optional<Agendamento> findById(String id) {
        return agendamentoRepository.findById(id);
    }

    public List<Agendamento> findByClienteId(String clienteId) {
        return agendamentoRepository.findByClienteId(clienteId);
    }

    public List<Agendamento> findByPetId(String petId) {
        return agendamentoRepository.findByPetId(petId);
    }

    public List<Agendamento> findByStatus(String status) {
        return agendamentoRepository.findByStatus(status);
    }

    public List<Agendamento> findByServico(String servico) {
        return agendamentoRepository.findByServico(servico);
    }

    public List<Agendamento> findByFuncionarioId(String funcionarioId) {
        return agendamentoRepository.findByFuncionarioId(funcionarioId);
    }

    public List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim) {
        return agendamentoRepository.findByDataHoraBetween(inicio, fim);
    }

    public List<Agendamento> findByDataHoraGreaterThan(LocalDateTime dataHora) {
        return agendamentoRepository.findByDataHoraGreaterThan(dataHora);
    }

    public List<Agendamento> findByDataHoraLessThan(LocalDateTime dataHora) {
        return agendamentoRepository.findByDataHoraLessThan(dataHora);
    }

    public List<Agendamento> findByServicoContaining(String servico) {
        return agendamentoRepository.findByServicoContainingIgnoreCase(servico);
    }

    public List<Agendamento> findByClienteIdAndStatus(String clienteId, String status) {
        return agendamentoRepository.findByClienteIdAndStatus(clienteId, status);
    }

    public List<Agendamento> findByPetIdAndStatus(String petId, String status) {
        return agendamentoRepository.findByPetIdAndStatus(petId, status);
    }

    public List<Agendamento> findAgendamentosAtivosPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return agendamentoRepository.findAgendamentosAtivosPorPeriodo(inicio, fim);
    }

    public Agendamento save(Agendamento agendamento) {
        // Verificar se já existe agendamento no mesmo horário para o mesmo pet
        List<Agendamento> agendamentosExistentes = agendamentoRepository.findByPetIdAndStatus(
                agendamento.getPetId(), "AGENDADO");
        
        for (Agendamento existente : agendamentosExistentes) {
            if (Math.abs(existente.getDataHora().getHour() - agendamento.getDataHora().getHour()) < 2) {
                throw new RuntimeException("Já existe um agendamento próximo a este horário para este pet");
            }
        }
        
        return agendamentoRepository.save(agendamento);
    }

    public Agendamento update(String id, Agendamento agendamento) {
        Optional<Agendamento> existingAgendamento = agendamentoRepository.findById(id);
        if (existingAgendamento.isPresent()) {
            Agendamento updatedAgendamento = existingAgendamento.get();
            updatedAgendamento.setClienteId(agendamento.getClienteId());
            updatedAgendamento.setPetId(agendamento.getPetId());
            updatedAgendamento.setServico(agendamento.getServico());
            updatedAgendamento.setDataHora(agendamento.getDataHora());
            updatedAgendamento.setObservacoes(agendamento.getObservacoes());
            updatedAgendamento.setStatus(agendamento.getStatus());
            updatedAgendamento.setValor(agendamento.getValor());
            updatedAgendamento.setFuncionarioId(agendamento.getFuncionarioId());
            return agendamentoRepository.save(updatedAgendamento);
        }
        throw new RuntimeException("Agendamento não encontrado");
    }

    public void deleteById(String id) {
        agendamentoRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        return agendamentoRepository.existsById(id);
    }

    // Métodos específicos para petshop
    public List<Agendamento> findAgendamentosHoje() {
        LocalDateTime hoje = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime amanha = hoje.plusDays(1);
        return agendamentoRepository.findByDataHoraBetween(hoje, amanha);
    }

    public List<Agendamento> findAgendamentosSemana() {
        LocalDateTime hoje = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fimSemana = hoje.plusDays(7);
        return agendamentoRepository.findByDataHoraBetween(hoje, fimSemana);
    }

    public List<Agendamento> findAgendamentosPendentes() {
        return agendamentoRepository.findByStatus("AGENDADO");
    }

    public List<Agendamento> findAgendamentosConfirmados() {
        return agendamentoRepository.findByStatus("CONFIRMADO");
    }

    public List<Agendamento> findAgendamentosRealizados() {
        return agendamentoRepository.findByStatus("REALIZADO");
    }

    public List<Agendamento> findAgendamentosCancelados() {
        return agendamentoRepository.findByStatus("CANCELADO");
    }

    public Agendamento confirmarAgendamento(String id) {
        Optional<Agendamento> agendamento = agendamentoRepository.findById(id);
        if (agendamento.isPresent()) {
            Agendamento a = agendamento.get();
            a.setStatus("CONFIRMADO");
            return agendamentoRepository.save(a);
        }
        throw new RuntimeException("Agendamento não encontrado");
    }

    public Agendamento realizarAgendamento(String id) {
        Optional<Agendamento> agendamento = agendamentoRepository.findById(id);
        if (agendamento.isPresent()) {
            Agendamento a = agendamento.get();
            a.setStatus("REALIZADO");
            return agendamentoRepository.save(a);
        }
        throw new RuntimeException("Agendamento não encontrado");
    }

    public Agendamento cancelarAgendamento(String id) {
        Optional<Agendamento> agendamento = agendamentoRepository.findById(id);
        if (agendamento.isPresent()) {
            Agendamento a = agendamento.get();
            a.setStatus("CANCELADO");
            return agendamentoRepository.save(a);
        }
        throw new RuntimeException("Agendamento não encontrado");
    }

    public List<Agendamento> findAgendamentosPorCliente(String clienteId) {
        return agendamentoRepository.findByClienteId(clienteId);
    }

    public List<Agendamento> findAgendamentosPorPet(String petId) {
        return agendamentoRepository.findByPetId(petId);
    }

    public List<Agendamento> findAgendamentosPorServico(String servico) {
        return agendamentoRepository.findByServicoContainingIgnoreCase(servico);
    }
} 