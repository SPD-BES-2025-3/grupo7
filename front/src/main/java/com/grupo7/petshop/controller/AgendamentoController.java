package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.grupo7.petshop.model.Agendamento;
import com.grupo7.petshop.model.Cliente;
import com.grupo7.petshop.model.Pet;
import com.grupo7.petshop.model.DatabaseManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
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

        // Carregar clientes do banco
        try {
            Dao<Cliente, Integer> clienteDao = DatabaseManager.getClienteDao();
            List<Cliente> listaClientes = clienteDao.queryForAll();
            ObservableList<String> clientes = FXCollections.observableArrayList(
                listaClientes.stream().map(c -> c.getNome() + " (" + c.getCpf() + ")").collect(Collectors.toList())
            );
            cmbCliente.setItems(clientes);
        } catch (SQLException e) {
            cmbCliente.setItems(FXCollections.observableArrayList());
            mostrarErro("Erro ao carregar clientes: " + e.getMessage());
        }

        // Carregar pets do banco
        try {
            Dao<Pet, Integer> petDao = DatabaseManager.getPetDao();
            List<Pet> listaPets = petDao.queryForAll();
            ObservableList<String> pets = FXCollections.observableArrayList(
                listaPets.stream().map(Pet::getNome).collect(Collectors.toList())
            );
            cmbPet.setItems(pets);
        } catch (SQLException e) {
            cmbPet.setItems(FXCollections.observableArrayList());
            mostrarErro("Erro ao carregar pets: " + e.getMessage());
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
            Dao<Agendamento, Integer> agendamentoDao = DatabaseManager.getAgendamentoDao();
            List<Agendamento> lista = agendamentoDao.queryForAll();
            tabelaAgendamentos.setItems(FXCollections.observableArrayList(lista));
        } catch (SQLException e) {
            tabelaAgendamentos.setItems(FXCollections.observableArrayList());
            mostrarErro("Erro ao carregar agendamentos: " + e.getMessage());
        }
    }
    
    @FXML
    public void salvarAgendamento() {
        if (validarFormulario()) {
            try {
                Dao<com.grupo7.petshop.model.Agendamento, Integer> agendamentoDao = com.grupo7.petshop.model.DatabaseManager.getAgendamentoDao();
                // Extrair dados do formulário
                String clienteStr = cmbCliente.getValue();
                String clienteCpf = "";
                if (clienteStr != null && clienteStr.contains("(") && clienteStr.contains(")")) {
                    int ini = clienteStr.lastIndexOf('(') + 1;
                    int fim = clienteStr.lastIndexOf(')');
                    clienteCpf = clienteStr.substring(ini, fim);
                }
                String petNome = cmbPet.getValue();
                java.time.LocalDate data = dpData.getValue();
                String horaStr = cmbHora.getValue();
                java.util.Date dataHora = null;
                if (data != null && horaStr != null) {
                    String[] partes = horaStr.split(":");
                    int hora = Integer.parseInt(partes[0]);
                    int minuto = Integer.parseInt(partes[1]);
                    java.time.LocalDateTime ldt = data.atTime(hora, minuto);
                    dataHora = java.util.Date.from(ldt.atZone(java.time.ZoneId.systemDefault()).toInstant());
                }
                String servico = cmbServico.getValue();
                String observacoes = txtObservacoes.getText();
                com.grupo7.petshop.model.Agendamento agendamento = new com.grupo7.petshop.model.Agendamento(
                    clienteCpf, petNome, dataHora, servico, observacoes
                );
                agendamentoDao.create(agendamento);
                mostrarMensagem("Sucesso", "Agendamento salvo com sucesso!");
                limparFormulario();
                carregarAgendamentos();
            } catch (Exception e) {
                mostrarErro("Erro ao salvar agendamento: " + e.getMessage());
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