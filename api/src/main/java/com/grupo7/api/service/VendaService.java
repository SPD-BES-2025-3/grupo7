package com.grupo7.api.service;

import com.grupo7.api.model.Produto;
import com.grupo7.api.model.Venda;
import com.grupo7.api.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ProdutoService produtoService;

    public List<Venda> findAll() {
        return vendaRepository.findAll();
    }

    public Optional<Venda> findById(String id) {
        return vendaRepository.findById(id);
    }

    public List<Venda> findByClienteId(String clienteId) {
        return vendaRepository.findByClienteId(clienteId);
    }

    public List<Venda> findByStatus(String status) {
        return vendaRepository.findByStatus(status);
    }

    public List<Venda> findByFormaPagamento(String formaPagamento) {
        return vendaRepository.findByFormaPagamento(formaPagamento);
    }

    public List<Venda> findByTotalBetween(BigDecimal totalMin, BigDecimal totalMax) {
        return vendaRepository.findByTotalBetween(totalMin, totalMax);
    }

    public List<Venda> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fim) {
        return vendaRepository.findByCreatedAtBetween(inicio, fim);
    }

    public List<Venda> findByDataPagamentoBetween(LocalDateTime inicio, LocalDateTime fim) {
        return vendaRepository.findByDataPagamentoBetween(inicio, fim);
    }

    public List<Venda> findVendasPagas() {
        return vendaRepository.findVendasPagas();
    }

    public List<Venda> findVendasPendentes() {
        return vendaRepository.findVendasPendentes();
    }

    public List<Venda> findByClienteIdAndStatus(String clienteId, String status) {
        return vendaRepository.findByClienteIdAndStatus(clienteId, status);
    }

    public List<Venda> findVendasPagasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return vendaRepository.findVendasPagasPorPeriodo(inicio, fim);
    }

    public Venda save(Venda venda) {
        // Validar estoque dos produtos
        for (Venda.ItemVenda item : venda.getItens()) {
            Optional<Produto> produto = produtoService.findById(item.getProdutoId());
            if (produto.isPresent()) {
                if (produto.get().getEstoque() < item.getQuantidade()) {
                    throw new RuntimeException("Estoque insuficiente para o produto: " + produto.get().getNome());
                }
            } else {
                throw new RuntimeException("Produto não encontrado: " + item.getProdutoId());
            }
        }

        // Calcular total se não fornecido
        if (venda.getTotal() == null) {
            BigDecimal total = BigDecimal.ZERO;
            for (Venda.ItemVenda item : venda.getItens()) {
                Optional<Produto> produto = produtoService.findById(item.getProdutoId());
                if (produto.isPresent()) {
                    item.setPrecoUnitario(produto.get().getPreco());
                    item.setSubtotal(produto.get().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));
                    total = total.add(item.getSubtotal());
                }
            }
            venda.setTotal(total);
        }

        return vendaRepository.save(venda);
    }

    public Venda update(String id, Venda venda) {
        Optional<Venda> existingVenda = vendaRepository.findById(id);
        if (existingVenda.isPresent()) {
            Venda updatedVenda = existingVenda.get();
            updatedVenda.setClienteId(venda.getClienteId());
            updatedVenda.setItens(venda.getItens());
            updatedVenda.setTotal(venda.getTotal());
            updatedVenda.setFormaPagamento(venda.getFormaPagamento());
            updatedVenda.setStatus(venda.getStatus());
            updatedVenda.setObservacoes(venda.getObservacoes());
            updatedVenda.setDataPagamento(venda.getDataPagamento());
            return vendaRepository.save(updatedVenda);
        }
        throw new RuntimeException("Venda não encontrada");
    }

    public void deleteById(String id) {
        vendaRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        return vendaRepository.existsById(id);
    }

    // Métodos específicos para petshop
    public List<Venda> findVendasHoje() {
        LocalDateTime hoje = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime amanha = hoje.plusDays(1);
        return vendaRepository.findByCreatedAtBetween(hoje, amanha);
    }

    public List<Venda> findVendasSemana() {
        LocalDateTime hoje = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fimSemana = hoje.plusDays(7);
        return vendaRepository.findByCreatedAtBetween(hoje, fimSemana);
    }

    public List<Venda> findVendasMes() {
        LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fimMes = inicioMes.plusMonths(1);
        return vendaRepository.findByCreatedAtBetween(inicioMes, fimMes);
    }

    public Venda finalizarVenda(String id, String formaPagamento) {
        Optional<Venda> venda = vendaRepository.findById(id);
        if (venda.isPresent()) {
            Venda v = venda.get();
            v.setStatus("PAGO");
            v.setFormaPagamento(formaPagamento);
            v.setDataPagamento(LocalDateTime.now());

            // Atualizar estoque dos produtos
            for (Venda.ItemVenda item : v.getItens()) {
                produtoService.atualizarEstoque(item.getProdutoId(), -item.getQuantidade());
            }

            return vendaRepository.save(v);
        }
        throw new RuntimeException("Venda não encontrada");
    }

    public Venda cancelarVenda(String id) {
        Optional<Venda> venda = vendaRepository.findById(id);
        if (venda.isPresent()) {
            Venda v = venda.get();
            v.setStatus("CANCELADO");
            return vendaRepository.save(v);
        }
        throw new RuntimeException("Venda não encontrada");
    }

    public BigDecimal calcularTotalVendasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        List<Venda> vendas = vendaRepository.findVendasPagasPorPeriodo(inicio, fim);
        return vendas.stream()
                .map(Venda::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalVendasHoje() {
        LocalDateTime hoje = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime amanha = hoje.plusDays(1);
        return calcularTotalVendasPorPeriodo(hoje, amanha);
    }

    public BigDecimal calcularTotalVendasSemana() {
        LocalDateTime hoje = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fimSemana = hoje.plusDays(7);
        return calcularTotalVendasPorPeriodo(hoje, fimSemana);
    }

    public BigDecimal calcularTotalVendasMes() {
        LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fimMes = inicioMes.plusMonths(1);
        return calcularTotalVendasPorPeriodo(inicioMes, fimMes);
    }

    public List<Venda> findVendasPorCliente(String clienteId) {
        return vendaRepository.findByClienteId(clienteId);
    }

    public List<Venda> findVendasPorFormaPagamento(String formaPagamento) {
        return vendaRepository.findByFormaPagamento(formaPagamento);
    }
} 