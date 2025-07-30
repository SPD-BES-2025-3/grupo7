package com.grupo7.petshop.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@DatabaseTable(tableName = "produtos")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Produto {
    @DatabaseField(id = true)
    private String id;
    @DatabaseField(canBeNull = false)
    private String nome;

    @DatabaseField(canBeNull = false)
    private String codigo;

    @DatabaseField(canBeNull = false)
    private String categoria;
    @DatabaseField(canBeNull = false)
    private String descricao;
    @DatabaseField(canBeNull = false)
    private double preco;
    @DatabaseField(canBeNull = false)
    @com.fasterxml.jackson.annotation.JsonProperty("estoque")
    private int quantidadeEstoque;

    public Produto() {}

    public Produto(String nome, String codigo, String categoria, String descricao, double preco, int quantidadeEstoque) {
        this.nome = nome;
        this.codigo = codigo;
        this.categoria = categoria;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getCodigo() { return codigo; }
    public String getCategoria() { return categoria; }
    public String getDescricao() { return descricao; }
    public double getPreco() { return preco; }
    public int getQuantidadeEstoque() { return quantidadeEstoque; }

    public void setId(String id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setPreco(double preco) { this.preco = preco; }
    public void setQuantidadeEstoque(int quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
} 