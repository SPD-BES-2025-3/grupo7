package com.grupo7.petshop.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

@DatabaseTable(tableName = "agendamentos")
public class Agendamento {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false)
    private String clienteCpf;
    @DatabaseField(canBeNull = false)
    private String petNome;
    @DatabaseField(canBeNull = false)
    private Date dataHora;
    @DatabaseField(canBeNull = false)
    private String servico;
    @DatabaseField
    private String observacoes;

    public Agendamento() {}

    public Agendamento(String clienteCpf, String petNome, Date dataHora, String servico, String observacoes) {
        this.clienteCpf = clienteCpf;
        this.petNome = petNome;
        this.dataHora = dataHora;
        this.servico = servico;
        this.observacoes = observacoes;
    }

    public int getId() { return id; }
    public String getClienteCpf() { return clienteCpf; }
    public String getPetNome() { return petNome; }
    public Date getDataHora() { return dataHora; }
    public String getServico() { return servico; }
    public String getObservacoes() { return observacoes; }

    public void setClienteCpf(String clienteCpf) { this.clienteCpf = clienteCpf; }
    public void setPetNome(String petNome) { this.petNome = petNome; }
    public void setDataHora(Date dataHora) { this.dataHora = dataHora; }
    public void setServico(String servico) { this.servico = servico; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
} 