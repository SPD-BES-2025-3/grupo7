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
        loadView("DashboardView");
    }
    
    @FXML
    public void showClientes() {
        loadView("ClienteView");
    }
    
    @FXML
    public void showPets() {
        loadView("PetView");
    }
    
    @FXML
    public void showProdutos() {
        loadView("ProdutoView");
    }
    
    @FXML
    public void showAgendamentos() {
        loadView("AgendamentoView");
    }
    
    @FXML
    public void showVendas() {
        loadView("VendaView");
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
            String fxmlName = fxmlPath.endsWith(".fxml") ? fxmlPath : fxmlPath + ".fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grupo7/petshop/" + fxmlName));
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