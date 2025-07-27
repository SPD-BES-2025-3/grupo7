package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;

public class PetController {
    
    @FXML
    private TextField txtNome;
    
    @FXML
    private ComboBox<String> cmbEspecie;
    
    @FXML
    private TextField txtRaca;
    
    @FXML
    private DatePicker dpDataNascimento;
    
    @FXML
    private TextField txtCor;
    
    @FXML
    private ComboBox<String> cmbSexo;
    
    @FXML
    private TextField txtPeso;
    
    @FXML
    private ComboBox<String> cmbCliente;
    
    @FXML
    private TextArea txtObservacoes;
    
    @FXML
    private CheckBox chkAtivo;
    
    @FXML
    private TextField txtPesquisa;
    
    @FXML
    private TableView<?> tabelaPets;
    
    @FXML
    private TableColumn<?, ?> colNome;
    
    @FXML
    private TableColumn<?, ?> colEspecie;
    
    @FXML
    private TableColumn<?, ?> colRaca;
    
    @FXML
    private TableColumn<?, ?> colCliente;
    
    @FXML
    private TableColumn<?, ?> colIdade;
    
    @FXML
    private TableColumn<?, ?> colPeso;
    
    @FXML
    private TableColumn<?, ?> colStatus;
    
    @FXML
    public void initialize() {
        configurarComboBoxes();
        configurarTabela();
        carregarPets();
    }
    
    private void configurarComboBoxes() {
        // Configurar espécies
        ObservableList<String> especies = FXCollections.observableArrayList(
            "Cachorro", "Gato", "Pássaro", "Peixe", "Hamster", "Coelho", "Outros"
        );
        cmbEspecie.setItems(especies);
        
        // Configurar sexo
        ObservableList<String> sexos = FXCollections.observableArrayList(
            "Macho", "Fêmea"
        );
        cmbSexo.setItems(sexos);
        
        // TODO: Carregar clientes da API
        ObservableList<String> clientes = FXCollections.observableArrayList(
            "Cliente 1", "Cliente 2", "Cliente 3"
        );
        cmbCliente.setItems(clientes);
    }
    
    private void configurarTabela() {
        // TODO: Implementar configuração da tabela
    }
    
    private void carregarPets() {
        // TODO: Implementar carregamento de pets da API
    }
    
    @FXML
    public void salvarPet() {
        if (validarFormulario()) {
            // TODO: Implementar salvamento na API
            mostrarMensagem("Sucesso", "Pet salvo com sucesso!");
            limparFormulario();
            carregarPets();
        }
    }
    
    @FXML
    public void limparFormulario() {
        txtNome.clear();
        cmbEspecie.setValue(null);
        txtRaca.clear();
        dpDataNascimento.setValue(null);
        txtCor.clear();
        cmbSexo.setValue(null);
        txtPeso.clear();
        cmbCliente.setValue(null);
        txtObservacoes.clear();
        chkAtivo.setSelected(true);
    }
    
    @FXML
    public void excluirPet() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir Pet");
        alert.setContentText("Deseja realmente excluir este pet?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // TODO: Implementar exclusão na API
                mostrarMensagem("Sucesso", "Pet excluído com sucesso!");
                carregarPets();
            }
        });
    }
    
    @FXML
    public void pesquisarPets() {
        String termo = txtPesquisa.getText().trim();
        if (!termo.isEmpty()) {
            // TODO: Implementar pesquisa na API
            mostrarMensagem("Pesquisa", "Pesquisando por: " + termo);
        } else {
            carregarPets();
        }
    }
    
    private boolean validarFormulario() {
        if (txtNome.getText().trim().isEmpty()) {
            mostrarErro("Nome é obrigatório");
            return false;
        }
        
        if (cmbEspecie.getValue() == null) {
            mostrarErro("Espécie é obrigatória");
            return false;
        }
        
        if (txtRaca.getText().trim().isEmpty()) {
            mostrarErro("Raça é obrigatória");
            return false;
        }
        
        if (dpDataNascimento.getValue() == null) {
            mostrarErro("Data de nascimento é obrigatória");
            return false;
        }
        
        if (dpDataNascimento.getValue().isAfter(LocalDate.now())) {
            mostrarErro("Data de nascimento deve ser no passado");
            return false;
        }
        
        if (cmbCliente.getValue() == null) {
            mostrarErro("Cliente é obrigatório");
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