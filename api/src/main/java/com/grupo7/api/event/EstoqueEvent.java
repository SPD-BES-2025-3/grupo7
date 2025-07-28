package com.grupo7.api.event;

public class EstoqueEvent extends BaseEvent {
    private String produtoId;
    private String produtoNome;
    private int quantidadeAnterior;
    private int quantidadeAtual;
    private String motivo;

    public EstoqueEvent() {
        super();
    }

    public EstoqueEvent(String produtoId, String produtoNome, int quantidadeAnterior, int quantidadeAtual, String motivo) {
        super();
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.quantidadeAnterior = quantidadeAnterior;
        this.quantidadeAtual = quantidadeAtual;
        this.motivo = motivo;
    }

    public String getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(String produtoId) {
        this.produtoId = produtoId;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public int getQuantidadeAnterior() {
        return quantidadeAnterior;
    }

    public void setQuantidadeAnterior(int quantidadeAnterior) {
        this.quantidadeAnterior = quantidadeAnterior;
    }

    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(int quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
} 