package com.grupo7.petshop.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "pets")
public class Pet {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false)
    private String nome;
    @DatabaseField(canBeNull = false)
    private String especie;
    @DatabaseField(canBeNull = false)
    private String raca;
    @DatabaseField(canBeNull = false)
    private int idade;
    @DatabaseField(canBeNull = false)
    private String donoCpf;

    public Pet() {}

    public Pet(String nome, String especie, String raca, int idade, String donoCpf) {
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.idade = idade;
        this.donoCpf = donoCpf;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEspecie() { return especie; }
    public String getRaca() { return raca; }
    public int getIdade() { return idade; }
    public String getDonoCpf() { return donoCpf; }

    public void setNome(String nome) { this.nome = nome; }
    public void setEspecie(String especie) { this.especie = especie; }
    public void setRaca(String raca) { this.raca = raca; }
    public void setIdade(int idade) { this.idade = idade; }
    public void setDonoCpf(String donoCpf) { this.donoCpf = donoCpf; }
} 