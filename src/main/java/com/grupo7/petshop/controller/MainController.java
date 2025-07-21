package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    
    @FXML
    private StackPane contentArea;
    
    @FXML
    public void initialize() {
        showDashboard();
    }
    
    @FXML
    public void showDashboard() {
        loadView("/fxml/DashboardView.fxml");
    }
    
    @FXML
    public void showClientes() {
        loadView("/fxml/ClienteView.fxml");
    }
    
    @FXML
    public void showPets() {
        loadView("/fxml/PetView.fxml");
    }
    
    @FXML
    public void showProdutos() {
        loadView("/fxml/ProdutoView.fxml");
    }
    
    @FXML
    public void showAgendamentos() {
        loadView("/fxml/AgendamentoView.fxml");
    }
    
    @FXML
    public void showVendas() {
        loadView("/fxml/VendaView.fxml");
    }
    
    @FXML
    public void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sair");
        alert.setHeaderText("Confirmar saída");
        alert.setContentText("Deseja realmente sair do sistema?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                Stage stage = (Stage) contentArea.getScene().getWindow();
                stage.close();
            }
        });
    }
    
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao carregar tela");
            alert.setContentText("Não foi possível carregar a tela solicitada.");
            alert.showAndWait();
        }
    }
} 