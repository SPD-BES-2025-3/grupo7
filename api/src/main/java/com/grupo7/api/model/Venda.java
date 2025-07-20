package com.grupo7.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "vendas")
public class Venda extends BaseEntity {

    @NotBlank(message = "Cliente é obrigatório")
    private String clienteId;

    @NotNull(message = "Itens são obrigatórios")
    private List<ItemVenda> itens;

    @NotNull(message = "Total é obrigatório")
    @Positive(message = "Total deve ser positivo")
    private BigDecimal total;

    private String formaPagamento; // DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO, PIX
    private String status = "PENDENTE"; // PENDENTE, PAGO, CANCELADO
    private String observacoes;
    private LocalDateTime dataPagamento;

    public Venda() {}

    public Venda(String clienteId, List<ItemVenda> itens, BigDecimal total) {
        this.clienteId = clienteId;
        this.itens = itens;
        this.total = total;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    // Classe interna para itens da venda
    public static class ItemVenda {
        @NotBlank(message = "Produto é obrigatório")
        private String produtoId;

        @NotNull(message = "Quantidade é obrigatória")
        @Positive(message = "Quantidade deve ser positiva")
        private Integer quantidade;

        @NotNull(message = "Preço unitário é obrigatório")
        @Positive(message = "Preço deve ser positivo")
        private BigDecimal precoUnitario;

        private BigDecimal subtotal;

        public ItemVenda() {}

        public ItemVenda(String produtoId, Integer quantidade, BigDecimal precoUnitario) {
            this.produtoId = produtoId;
            this.quantidade = quantidade;
            this.precoUnitario = precoUnitario;
            this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        }

        public String getProdutoId() {
            return produtoId;
        }

        public void setProdutoId(String produtoId) {
            this.produtoId = produtoId;
        }

        public Integer getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(Integer quantidade) {
            this.quantidade = quantidade;
        }

        public BigDecimal getPrecoUnitario() {
            return precoUnitario;
        }

        public void setPrecoUnitario(BigDecimal precoUnitario) {
            this.precoUnitario = precoUnitario;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }
    }
} 