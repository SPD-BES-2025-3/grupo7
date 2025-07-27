package com.grupo7.petshop.model;

public class Cliente {
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private String endereco;
    private String observacoes;
    private boolean ativo;
    
    public Cliente(String nome, String email, String telefone, String cpf, 
                  String endereco, String observacoes, boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.endereco = endereco;
        this.observacoes = observacoes;
        this.ativo = ativo;
    }
    
    // Getters
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getCpf() { return cpf; }
    public String getEndereco() { return endereco; }
    public String getObservacoes() { return observacoes; }
    public boolean isAtivo() { return ativo; }
    public String getStatus() { return ativo ? "Ativo" : "Inativo"; }
    
    // Setters
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
} 