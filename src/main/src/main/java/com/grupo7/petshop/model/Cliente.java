package com.grupo7.petshop.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "clientes")
public class Cliente {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false)
    private String nome;
    @DatabaseField(canBeNull = false)
    private String email;
    @DatabaseField(canBeNull = false)
    private String telefone;
    @DatabaseField(canBeNull = false, unique = true)
    private String cpf;
    @DatabaseField(canBeNull = false)
    private String endereco;
    @DatabaseField
    private String observacoes;
    @DatabaseField
    private boolean ativo;
    
    public Cliente() {
        // ORMLite precisa de um construtor sem argumentos
    }
    
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