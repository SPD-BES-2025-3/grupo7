package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import java.io.IOException;
import javafx.collections.ObservableList;
import com.grupo7.petshop.model.Agendamento;
import com.grupo7.petshop.model.Cliente;
import com.grupo7.petshop.model.Pet;
import com.grupo7.petshop.service.ApiService;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.math.BigDecimal;

public class AgendamentoController {
    
    @FXML
    private ComboBox<Cliente> cmbCliente;
    @FXML
    private ComboBox<Pet> cmbPet;
    
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
    private TableView<Agendamento> tabelaAgendamentos;
    @FXML
    private TableColumn<Agendamento, String> colCliente;
    @FXML
    private TableColumn<Agendamento, String> colPet;
    @FXML
    private TableColumn<Agendamento, String> colServico;
    @FXML
    private TableColumn<Agendamento, String> colDataHora;
    @FXML
    private TableColumn<Agendamento, String> colValor;
    @FXML
    private TableColumn<Agendamento, String> colStatus;
    
    @FXML
    public void initialize() {
        configurarComboBoxes();
        configurarTabela();
        carregarAgendamentos();
    }
    
    private void configurarComboBoxes() {
        // Exibir nomes dos pets no ComboBox
        cmbPet.setCellFactory(lv -> new ListCell<Pet>() {
            @Override
            protected void updateItem(Pet item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.getNome());
            }
        });
        cmbPet.setButtonCell(new ListCell<Pet>() {
            @Override
            protected void updateItem(Pet item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.getNome());
            }
        });
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

        carregarClientes();
        carregarPets();
    }
    
    private void carregarClientes() {
        try {
            List<Cliente> clientes = ApiService.getAllClientes();
            ObservableList<Cliente> observableClientes = FXCollections.observableArrayList(clientes);
            cmbCliente.setItems(observableClientes);
        } catch (Exception e) {
            mostrarMensagem("Erro", "Erro ao carregar clientes: " + e.getMessage());
        }
    }
    
    private void carregarPets() {
        try {
            List<Pet> pets = ApiService.getAllPets();
            ObservableList<Pet> observablePets = FXCollections.observableArrayList(pets);
            cmbPet.setItems(observablePets);
        } catch (Exception e) {
            mostrarMensagem("Erro", "Erro ao carregar pets: " + e.getMessage());
        }
    }
    
    private void configurarTabela() {
        colCliente.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClienteCpf()));
        colPet.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPetNome()));
        colServico.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getServico()));
        colDataHora.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDataHora() != null ? cellData.getValue().getDataHora().toString() : ""));
        colStatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty("")); // Ajuste se adicionar status no modelo
        colValor.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty("")); // Ajuste se adicionar valor no modelo
    }
    
    private void carregarAgendamentos() {
        try {
            List<Agendamento> agendamentos = ApiService.getAllAgendamentos();
            ObservableList<Agendamento> observableAgendamentos = FXCollections.observableArrayList(agendamentos);
            tabelaAgendamentos.setItems(observableAgendamentos);
        } catch (Exception e) {
            mostrarMensagem("Erro", "Erro ao carregar agendamentos: " + e.getMessage());
        }
    }
    
    @FXML
    public void salvarAgendamento() {
        if (validarFormulario()) {
            try {
                Agendamento agendamento = new Agendamento();
                Cliente clienteSelecionado = cmbCliente.getValue();
                if (clienteSelecionado != null) {
                    agendamento.setClienteCpf(clienteSelecionado.getCpf());
                    agendamento.setClienteId(clienteSelecionado.getId());
                }
                Pet petSelecionado = cmbPet.getValue();
                if (petSelecionado != null) {
                    agendamento.setPetId(petSelecionado.getId());
                    agendamento.setPetNome(petSelecionado.getNome());
                }
                java.time.LocalDate data = dpData.getValue();
                String horaStr = cmbHora.getValue();
                String dataHoraStr = null;
                if (data != null && horaStr != null) {
                    String[] partes = horaStr.split(":");
                    int hora = Integer.parseInt(partes[0]);
                    int minuto = Integer.parseInt(partes[1]);
                    java.time.LocalDateTime ldt = data.atTime(hora, minuto);
                    dataHoraStr = ldt.toString(); // ISO-8601
                }
                agendamento.setDataHora(dataHoraStr);
                agendamento.setServico(cmbServico.getValue());
                agendamento.setObservacoes(txtObservacoes.getText());
                ApiService.createAgendamento(agendamento);
                mostrarMensagem("Sucesso", "Agendamento salvo com sucesso!");
                limparFormulario();
                carregarAgendamentos();
            } catch (Exception e) {
                mostrarMensagem("Erro", "Erro ao salvar agendamento: " + e.getMessage());
            }
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
                Agendamento agendamentoSelecionado = tabelaAgendamentos.getSelectionModel().getSelectedItem();
                if (agendamentoSelecionado != null) {
                    try {
    ApiService.deleteAgendamento(String.valueOf(agendamentoSelecionado.getId()));
} catch (IOException | InterruptedException e) {
    mostrarMensagem("Erro", "Erro ao excluir agendamento: " + e.getMessage());
}
                }
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
                Agendamento agendamentoSelecionado = tabelaAgendamentos.getSelectionModel().getSelectedItem();
                if (agendamentoSelecionado != null) {
                    
                    try {
    ApiService.updateAgendamento(String.valueOf(agendamentoSelecionado.getId()), agendamentoSelecionado);
} catch (IOException | InterruptedException e) {
    mostrarMensagem("Erro", "Erro ao atualizar agendamento: " + e.getMessage());
}
                }
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