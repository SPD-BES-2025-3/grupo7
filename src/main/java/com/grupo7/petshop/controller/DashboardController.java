package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DashboardController {
    
    @FXML
    private Label totalClientesLabel;
    
    @FXML
    private Label totalPetsLabel;
    
    @FXML
    private Label agendamentosHojeLabel;
    
    @FXML
    private Label vendasMesLabel;
    
    @FXML
    private TableView<?> agendamentosTableView;
    
    @FXML
    private TableColumn<?, ?> colCliente;
    
    @FXML
    private TableColumn<?, ?> colPet;
    
    @FXML
    private TableColumn<?, ?> colServico;
    
    @FXML
    private TableColumn<?, ?> colDataHora;
    
    @FXML
    private TableColumn<?, ?> colStatus;
    
    @FXML
    public void initialize() {
        carregarEstatisticas();
        carregarAgendamentosRecentes();
    }
    
    private void carregarEstatisticas() {
        // TODO: Implementar carregamento de dados da API
        totalClientesLabel.setText("0");
        totalPetsLabel.setText("0");
        agendamentosHojeLabel.setText("0");
        vendasMesLabel.setText("R$ 0,00");
    }
    
    private void carregarAgendamentosRecentes() {
        // TODO: Implementar carregamento de agendamentos da API
    }
    
    @FXML
    public void novoCliente() {
        mostrarMensagem("Nova Funcionalidade", "Funcionalidade de novo cliente será implementada.");
    }
    
    @FXML
    public void novoPet() {
        mostrarMensagem("Nova Funcionalidade", "Funcionalidade de novo pet será implementada.");
    }
    
    @FXML
    public void novoAgendamento() {
        mostrarMensagem("Nova Funcionalidade", "Funcionalidade de novo agendamento será implementada.");
    }
    
    @FXML
    public void novaVenda() {
        mostrarMensagem("Nova Funcionalidade", "Funcionalidade de nova venda será implementada.");
    }
    
    private void mostrarMensagem(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
} 