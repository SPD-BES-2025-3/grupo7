package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class ClienteController {
    
    @FXML
    private TextField txtNome;
    
    @FXML
    private TextField txtEmail;
    
    @FXML
    private TextField txtTelefone;
    
    @FXML
    private TextField txtCpf;
    
    @FXML
    private TextField txtEndereco;
    
    @FXML
    private TextArea txtObservacoes;
    
    @FXML
    private CheckBox chkAtivo;
    
    @FXML
    private TextField txtPesquisa;
    
    @FXML
    private TableView<?> tabelaClientes;
    
    @FXML
    private TableColumn<?, ?> colNome;
    
    @FXML
    private TableColumn<?, ?> colEmail;
    
    @FXML
    private TableColumn<?, ?> colTelefone;
    
    @FXML
    private TableColumn<?, ?> colCpf;
    
    @FXML
    private TableColumn<?, ?> colStatus;
    
    @FXML
    public void initialize() {
        configurarTabela();
        carregarClientes();
    }
    
    private void configurarTabela() {
        // TODO: Implementar configuração da tabela
    }
    
    private void carregarClientes() {
        // TODO: Implementar carregamento de clientes da API
    }
    
    @FXML
    public void salvarCliente() {
        if (validarFormulario()) {
            // TODO: Implementar salvamento na API
            mostrarMensagem("Sucesso", "Cliente salvo com sucesso!");
            limparFormulario();
            carregarClientes();
        }
    }
    
    @FXML
    public void limparFormulario() {
        txtNome.clear();
        txtEmail.clear();
        txtTelefone.clear();
        txtCpf.clear();
        txtEndereco.clear();
        txtObservacoes.clear();
        chkAtivo.setSelected(true);
    }
    
    @FXML
    public void excluirCliente() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir Cliente");
        alert.setContentText("Deseja realmente excluir este cliente?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // TODO: Implementar exclusão na API
                mostrarMensagem("Sucesso", "Cliente excluído com sucesso!");
                carregarClientes();
            }
        });
    }
    
    @FXML
    public void pesquisarClientes() {
        String termo = txtPesquisa.getText().trim();
        if (!termo.isEmpty()) {
            // TODO: Implementar pesquisa na API
            mostrarMensagem("Pesquisa", "Pesquisando por: " + termo);
        } else {
            carregarClientes();
        }
    }
    
    private boolean validarFormulario() {
        if (txtNome.getText().trim().isEmpty()) {
            mostrarErro("Nome é obrigatório");
            return false;
        }
        
        if (txtEmail.getText().trim().isEmpty()) {
            mostrarErro("Email é obrigatório");
            return false;
        }
        
        if (txtTelefone.getText().trim().isEmpty()) {
            mostrarErro("Telefone é obrigatório");
            return false;
        }
        
        if (txtCpf.getText().trim().isEmpty()) {
            mostrarErro("CPF é obrigatório");
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