package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.math.BigDecimal;

public class AgendamentoController {
    
    @FXML
    private ComboBox<String> cmbCliente;
    
    @FXML
    private ComboBox<String> cmbPet;
    
    @FXML
    private ComboBox<String> cmbServico;
    
    @FXML
    private DatePicker dpData;
    
    @FXML
    private ComboBox<String> cmbHora;
    
    @FXML
    private TextField txtValor;
    
    @FXML
    private ComboBox<String> cmbStatus;
    
    @FXML
    private TextArea txtObservacoes;
    
    @FXML
    private DatePicker dpFiltroData;
    
    @FXML
    private ComboBox<String> cmbFiltroStatus;
    
    @FXML
    private TableView<?> tabelaAgendamentos;
    
    @FXML
    private TableColumn<?, ?> colCliente;
    
    @FXML
    private TableColumn<?, ?> colPet;
    
    @FXML
    private TableColumn<?, ?> colServico;
    
    @FXML
    private TableColumn<?, ?> colDataHora;
    
    @FXML
    private TableColumn<?, ?> colValor;
    
    @FXML
    private TableColumn<?, ?> colStatus;
    
    @FXML
    public void initialize() {
        configurarComboBoxes();
        configurarTabela();
        carregarAgendamentos();
    }
    
    private void configurarComboBoxes() {
        // Configurar serviços
        ObservableList<String> servicos = FXCollections.observableArrayList(
            "Banho", "Tosa", "Consulta Veterinária", "Vacinação", "Exame", "Cirurgia", "Outros"
        );
        cmbServico.setItems(servicos);
        
        // Configurar horários
        ObservableList<String> horarios = FXCollections.observableArrayList();
        for (int hora = 8; hora <= 18; hora++) {
            for (int minuto = 0; minuto < 60; minuto += 30) {
                String horario = String.format("%02d:%02d", hora, minuto);
                horarios.add(horario);
            }
        }
        cmbHora.setItems(horarios);
        
        // Configurar status
        ObservableList<String> status = FXCollections.observableArrayList(
            "AGENDADO", "CONFIRMADO", "CANCELADO", "REALIZADO"
        );
        cmbStatus.setItems(status);
        cmbStatus.setValue("AGENDADO");
        
        // Configurar filtros
        cmbFiltroStatus.setItems(status);
        
        // TODO: Carregar clientes e pets da API
        ObservableList<String> clientes = FXCollections.observableArrayList(
            "Cliente 1", "Cliente 2", "Cliente 3"
        );
        cmbCliente.setItems(clientes);
        
        ObservableList<String> pets = FXCollections.observableArrayList(
            "Pet 1", "Pet 2", "Pet 3"
        );
        cmbPet.setItems(pets);
    }
    
    private void configurarTabela() {
        // TODO: Implementar configuração da tabela
    }
    
    private void carregarAgendamentos() {
        // TODO: Implementar carregamento de agendamentos da API
    }
    
    @FXML
    public void salvarAgendamento() {
        if (validarFormulario()) {
            // TODO: Implementar salvamento na API
            mostrarMensagem("Sucesso", "Agendamento salvo com sucesso!");
            limparFormulario();
            carregarAgendamentos();
        }
    }
    
    @FXML
    public void limparFormulario() {
        cmbCliente.setValue(null);
        cmbPet.setValue(null);
        cmbServico.setValue(null);
        dpData.setValue(null);
        cmbHora.setValue(null);
        txtValor.clear();
        cmbStatus.setValue("AGENDADO");
        txtObservacoes.clear();
    }
    
    @FXML
    public void excluirAgendamento() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir Agendamento");
        alert.setContentText("Deseja realmente excluir este agendamento?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // TODO: Implementar exclusão na API
                mostrarMensagem("Sucesso", "Agendamento excluído com sucesso!");
                carregarAgendamentos();
            }
        });
    }
    
    @FXML
    public void confirmarAgendamento() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Agendamento");
        alert.setHeaderText("Confirmar Agendamento");
        alert.setContentText("Deseja confirmar este agendamento?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // TODO: Implementar confirmação na API
                mostrarMensagem("Sucesso", "Agendamento confirmado com sucesso!");
                carregarAgendamentos();
            }
        });
    }
    
    @FXML
    public void filtrarAgendamentos() {
        // TODO: Implementar filtros
        mostrarMensagem("Filtro", "Filtros aplicados");
    }
    
    @FXML
    public void limparFiltros() {
        dpFiltroData.setValue(null);
        cmbFiltroStatus.setValue(null);
        carregarAgendamentos();
    }
    
    private boolean validarFormulario() {
        if (cmbCliente.getValue() == null) {
            mostrarErro("Cliente é obrigatório");
            return false;
        }
        
        if (cmbPet.getValue() == null) {
            mostrarErro("Pet é obrigatório");
            return false;
        }
        
        if (cmbServico.getValue() == null) {
            mostrarErro("Serviço é obrigatório");
            return false;
        }
        
        if (dpData.getValue() == null) {
            mostrarErro("Data é obrigatória");
            return false;
        }
        
        if (dpData.getValue().isBefore(LocalDate.now())) {
            mostrarErro("Data deve ser no futuro");
            return false;
        }
        
        if (cmbHora.getValue() == null) {
            mostrarErro("Hora é obrigatória");
            return false;
        }
        
        if (txtValor.getText().trim().isEmpty()) {
            mostrarErro("Valor é obrigatório");
            return false;
        }
        
        try {
            BigDecimal valor = new BigDecimal(txtValor.getText().trim());
            if (valor.compareTo(BigDecimal.ZERO) < 0) {
                mostrarErro("Valor deve ser maior ou igual a zero");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarErro("Valor deve ser um número válido");
            return false;
        }
        
        return true;
    }
    
    private void mostrarMensagem(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
} 