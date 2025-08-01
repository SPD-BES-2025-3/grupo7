package com.grupo7.petshop.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@DatabaseTable(tableName = "vendas")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Venda {
    @DatabaseField(id = true)
    private String id;
    @DatabaseField(canBeNull = false)
    private String clienteCpf;
    @DatabaseField(canBeNull = false)
    private Date data;
    @DatabaseField(canBeNull = false)
    private double valorTotal;

    public Venda() {}

    public Venda(String clienteCpf, Date data, double valorTotal) {
        this.clienteCpf = clienteCpf;
        this.data = data;
        this.valorTotal = valorTotal;
    }

    public String getId() { return id; }
    public String getClienteCpf() { return clienteCpf; }
    public Date getData() { return data; }
    public double getValorTotal() { return valorTotal; }

    public void setId(String id) { this.id = id; }
    public void setClienteCpf(String clienteCpf) { this.clienteCpf = clienteCpf; }
    public void setData(Date data) { this.data = data; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
}
