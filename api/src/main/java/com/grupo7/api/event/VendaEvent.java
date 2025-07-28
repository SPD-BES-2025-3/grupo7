package com.grupo7.api.event;

import com.grupo7.api.model.Venda;

public class VendaEvent extends BaseEvent {
    private Venda venda;
    private String action;

    public VendaEvent() {
        super();
    }

    public VendaEvent(Venda venda, String action) {
        super();
        this.venda = venda;
        this.action = action;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
} 