package com.grupo7.api.model;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "agendamentos")
public class Agendamento extends BaseEntity {

    @NotBlank(message = "Cliente é obrigatório")
    private String clienteId;

    @NotBlank(message = "Pet é obrigatório")
    private String petId;

    @NotBlank(message = "Serviço é obrigatório")
    private String servico; // Banho, Tosa, Consulta, etc.

    @NotNull(message = "Data e hora são obrigatórias")
    @Future(message = "Data e hora devem ser no futuro")
    @Indexed
    private LocalDateTime dataHora;

    private String observacoes;
    private String status = "AGENDADO"; // AGENDADO, CONFIRMADO, CANCELADO, REALIZADO
    private Double valor;
    private String funcionarioId; // ID do funcionário responsável

    public Agendamento() {}

    public Agendamento(String clienteId, String petId, String servico, LocalDateTime dataHora) {
        this.clienteId = clienteId;
        this.petId = petId;
        this.servico = servico;
        this.dataHora = dataHora;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(String funcionarioId) {
        this.funcionarioId = funcionarioId;
    }
} 