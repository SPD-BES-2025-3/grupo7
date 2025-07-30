package com.grupo7.petshop.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@DatabaseTable(tableName = "agendamentos")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Agendamento {
    @DatabaseField(id = true)
    private String id;

    @DatabaseField(canBeNull = false)
    private String clienteId;

    @DatabaseField(canBeNull = false)
    private String petId;

    @DatabaseField(canBeNull = false)
    private String clienteCpf;
    @DatabaseField(canBeNull = false)
    private String petNome;
    @DatabaseField(canBeNull = false)
    private String dataHora; // Agora como ISO-8601
    @DatabaseField(canBeNull = false)
    private String servico;
    @DatabaseField
    private String observacoes;

    public Agendamento() {}

    public Agendamento(String clienteId, String petId, String clienteCpf, String petNome, String dataHora, String servico, String observacoes) {
        this.clienteId = clienteId;
        this.petId = petId;
        this.clienteCpf = clienteCpf;
        this.petNome = petNome;
        this.dataHora = dataHora;
        this.servico = servico;
        this.observacoes = observacoes;
    }

    public String getId() { return id; }
    public String getClienteId() { return clienteId; }
    public String getPetId() { return petId; }
    public String getClienteCpf() { return clienteCpf; }
    public String getPetNome() { return petNome; }
    public String getDataHora() { return dataHora; }
    public String getServico() { return servico; }
    public String getObservacoes() { return observacoes; }

    public void setId(String id) { this.id = id; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    public void setPetId(String petId) { this.petId = petId; }
    public void setClienteCpf(String clienteCpf) { this.clienteCpf = clienteCpf; }
    public void setPetNome(String petNome) { this.petNome = petNome; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }
    public void setServico(String servico) { this.servico = servico; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
} 