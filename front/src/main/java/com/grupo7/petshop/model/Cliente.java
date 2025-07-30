package com.grupo7.petshop.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@DatabaseTable(tableName = "clientes")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cliente {
    @DatabaseField(id = true)
    private String id;
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
    
    // Construtor para criação de novo cliente (POST), não define id
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

    // Construtor para atualização de cliente (PUT), define id
    public Cliente(String id, String nome, String email, String telefone, String cpf, 
                  String endereco, String observacoes, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.endereco = endereco;
        this.observacoes = observacoes;
        this.ativo = ativo;
    }
    
    // Adicione estes métodos:
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // Getters
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getCpf() { return cpf; }
    public String getEndereco() { return endereco; }
    public String getObservacoes() { return observacoes; }
    public boolean isAtivo() { return ativo; }
    public String getStatus() { return ativo ? "Ativo" : "Inativo"; }
    
    @Override
    public String toString() {
        return nome + " (" + cpf + ")";
    }
    
    // Setters
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
} 