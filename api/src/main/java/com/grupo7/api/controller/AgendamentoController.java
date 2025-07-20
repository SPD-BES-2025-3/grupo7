package com.grupo7.api.controller;

import com.grupo7.api.model.Agendamento;
import com.grupo7.api.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agendamentos")
@CrossOrigin(origins = "*")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping
    public ResponseEntity<List<Agendamento>> getAllAgendamentos() {
        List<Agendamento> agendamentos = agendamentoService.findAll();
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> getAgendamentoById(@PathVariable String id) {
        Optional<Agendamento> agendamento = agendamentoService.findById(id);
        return agendamento.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Agendamento>> getAgendamentosByCliente(@PathVariable String clienteId) {
        List<Agendamento> agendamentos = agendamentoService.findAgendamentosPorCliente(clienteId);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<Agendamento>> getAgendamentosByPet(@PathVariable String petId) {
        List<Agendamento> agendamentos = agendamentoService.findAgendamentosPorPet(petId);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Agendamento>> getAgendamentosByStatus(@PathVariable String status) {
        List<Agendamento> agendamentos = agendamentoService.findByStatus(status);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/servico/{servico}")
    public ResponseEntity<List<Agendamento>> getAgendamentosByServico(@PathVariable String servico) {
        List<Agendamento> agendamentos = agendamentoService.findAgendamentosPorServico(servico);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<Agendamento>> getAgendamentosByFuncionario(@PathVariable String funcionarioId) {
        List<Agendamento> agendamentos = agendamentoService.findByFuncionarioId(funcionarioId);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/hoje")
    public ResponseEntity<List<Agendamento>> getAgendamentosHoje() {
        List<Agendamento> agendamentos = agendamentoService.findAgendamentosHoje();
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/semana")
    public ResponseEntity<List<Agendamento>> getAgendamentosSemana() {
        List<Agendamento> agendamentos = agendamentoService.findAgendamentosSemana();
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<Agendamento>> getAgendamentosPendentes() {
        List<Agendamento> agendamentos = agendamentoService.findAgendamentosPendentes();
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/confirmados")
    public ResponseEntity<List<Agendamento>> getAgendamentosConfirmados() {
        List<Agendamento> agendamentos = agendamentoService.findAgendamentosConfirmados();
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/realizados")
    public ResponseEntity<List<Agendamento>> getAgendamentosRealizados() {
        List<Agendamento> agendamentos = agendamentoService.findAgendamentosRealizados();
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/cancelados")
    public ResponseEntity<List<Agendamento>> getAgendamentosCancelados() {
        List<Agendamento> agendamentos = agendamentoService.findAgendamentosCancelados();
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<Agendamento>> getAgendamentosPorPeriodo(
            @RequestParam String inicio, 
            @RequestParam String fim) {
        LocalDateTime dataInicio = LocalDateTime.parse(inicio);
        LocalDateTime dataFim = LocalDateTime.parse(fim);
        List<Agendamento> agendamentos = agendamentoService.findByDataHoraBetween(dataInicio, dataFim);
        return ResponseEntity.ok(agendamentos);
    }

    @PostMapping
    public ResponseEntity<Agendamento> createAgendamento(@Valid @RequestBody Agendamento agendamento) {
        try {
            Agendamento savedAgendamento = agendamentoService.save(agendamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAgendamento);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> updateAgendamento(@PathVariable String id, @Valid @RequestBody Agendamento agendamento) {
        try {
            Agendamento updatedAgendamento = agendamentoService.update(id, agendamento);
            return ResponseEntity.ok(updatedAgendamento);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgendamento(@PathVariable String id) {
        if (agendamentoService.existsById(id)) {
            agendamentoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<Agendamento> confirmarAgendamento(@PathVariable String id) {
        try {
            Agendamento agendamento = agendamentoService.confirmarAgendamento(id);
            return ResponseEntity.ok(agendamento);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/realizar")
    public ResponseEntity<Agendamento> realizarAgendamento(@PathVariable String id) {
        try {
            Agendamento agendamento = agendamentoService.realizarAgendamento(id);
            return ResponseEntity.ok(agendamento);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Agendamento> cancelarAgendamento(@PathVariable String id) {
        try {
            Agendamento agendamento = agendamentoService.cancelarAgendamento(id);
            return ResponseEntity.ok(agendamento);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/valor")
    public ResponseEntity<Agendamento> definirValor(@PathVariable String id, @RequestParam Double valor) {
        Optional<Agendamento> agendamento = agendamentoService.findById(id);
        if (agendamento.isPresent()) {
            Agendamento a = agendamento.get();
            a.setValor(valor);
            Agendamento updatedAgendamento = agendamentoService.save(a);
            return ResponseEntity.ok(updatedAgendamento);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/funcionario")
    public ResponseEntity<Agendamento> definirFuncionario(@PathVariable String id, @RequestParam String funcionarioId) {
        Optional<Agendamento> agendamento = agendamentoService.findById(id);
        if (agendamento.isPresent()) {
            Agendamento a = agendamento.get();
            a.setFuncionarioId(funcionarioId);
            Agendamento updatedAgendamento = agendamentoService.save(a);
            return ResponseEntity.ok(updatedAgendamento);
        }
        return ResponseEntity.notFound().build();
    }
} 