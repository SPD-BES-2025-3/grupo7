package com.grupo7.api.controller;

import com.grupo7.api.model.Venda;
import com.grupo7.api.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vendas")
@CrossOrigin(origins = "*")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @GetMapping
    public ResponseEntity<List<Venda>> getAllVendas() {
        List<Venda> vendas = vendaService.findAll();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> getVendaById(@PathVariable String id) {
        Optional<Venda> venda = vendaService.findById(id);
        return venda.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Venda>> getVendasByCliente(@PathVariable String clienteId) {
        List<Venda> vendas = vendaService.findVendasPorCliente(clienteId);
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Venda>> getVendasByStatus(@PathVariable String status) {
        List<Venda> vendas = vendaService.findByStatus(status);
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/forma-pagamento/{formaPagamento}")
    public ResponseEntity<List<Venda>> getVendasByFormaPagamento(@PathVariable String formaPagamento) {
        List<Venda> vendas = vendaService.findVendasPorFormaPagamento(formaPagamento);
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/hoje")
    public ResponseEntity<List<Venda>> getVendasHoje() {
        List<Venda> vendas = vendaService.findVendasHoje();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/semana")
    public ResponseEntity<List<Venda>> getVendasSemana() {
        List<Venda> vendas = vendaService.findVendasSemana();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/mes")
    public ResponseEntity<List<Venda>> getVendasMes() {
        List<Venda> vendas = vendaService.findVendasMes();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<Venda>> getVendasPendentes() {
        List<Venda> vendas = vendaService.findVendasPendentes();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/pagas")
    public ResponseEntity<List<Venda>> getVendasPagas() {
        List<Venda> vendas = vendaService.findVendasPagas();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/total/hoje")
    public ResponseEntity<BigDecimal> getTotalVendasHoje() {
        BigDecimal total = vendaService.calcularTotalVendasHoje();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total/semana")
    public ResponseEntity<BigDecimal> getTotalVendasSemana() {
        BigDecimal total = vendaService.calcularTotalVendasSemana();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total/mes")
    public ResponseEntity<BigDecimal> getTotalVendasMes() {
        BigDecimal total = vendaService.calcularTotalVendasMes();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<Venda>> getVendasPorPeriodo(
            @RequestParam String inicio, 
            @RequestParam String fim) {
        LocalDateTime dataInicio = LocalDateTime.parse(inicio);
        LocalDateTime dataFim = LocalDateTime.parse(fim);
        List<Venda> vendas = vendaService.findByCreatedAtBetween(dataInicio, dataFim);
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/total/periodo")
    public ResponseEntity<BigDecimal> getTotalVendasPorPeriodo(
            @RequestParam String inicio, 
            @RequestParam String fim) {
        LocalDateTime dataInicio = LocalDateTime.parse(inicio);
        LocalDateTime dataFim = LocalDateTime.parse(fim);
        BigDecimal total = vendaService.calcularTotalVendasPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/faixa-preco")
    public ResponseEntity<List<Venda>> getVendasPorFaixaPreco(
            @RequestParam BigDecimal totalMin, 
            @RequestParam BigDecimal totalMax) {
        List<Venda> vendas = vendaService.findByTotalBetween(totalMin, totalMax);
        return ResponseEntity.ok(vendas);
    }

    @PostMapping
    public ResponseEntity<Venda> createVenda(@Valid @RequestBody Venda venda) {
        try {
            Venda savedVenda = vendaService.save(venda);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVenda);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venda> updateVenda(@PathVariable String id, @Valid @RequestBody Venda venda) {
        try {
            Venda updatedVenda = vendaService.update(id, venda);
            return ResponseEntity.ok(updatedVenda);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenda(@PathVariable String id) {
        if (vendaService.existsById(id)) {
            vendaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<Venda> finalizarVenda(@PathVariable String id, @RequestParam String formaPagamento) {
        try {
            Venda venda = vendaService.finalizarVenda(id, formaPagamento);
            return ResponseEntity.ok(venda);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Venda> cancelarVenda(@PathVariable String id) {
        try {
            Venda venda = vendaService.cancelarVenda(id);
            return ResponseEntity.ok(venda);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/forma-pagamento")
    public ResponseEntity<Venda> alterarFormaPagamento(@PathVariable String id, @RequestParam String formaPagamento) {
        Optional<Venda> venda = vendaService.findById(id);
        if (venda.isPresent()) {
            Venda v = venda.get();
            v.setFormaPagamento(formaPagamento);
            Venda updatedVenda = vendaService.save(v);
            return ResponseEntity.ok(updatedVenda);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/observacoes")
    public ResponseEntity<Venda> adicionarObservacoes(@PathVariable String id, @RequestParam String observacoes) {
        Optional<Venda> venda = vendaService.findById(id);
        if (venda.isPresent()) {
            Venda v = venda.get();
            v.setObservacoes(observacoes);
            Venda updatedVenda = vendaService.save(v);
            return ResponseEntity.ok(updatedVenda);
        }
        return ResponseEntity.notFound().build();
    }
} 