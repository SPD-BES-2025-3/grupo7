package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import com.grupo7.petshop.model.Cliente;

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
    private TableView<Cliente> tabelaClientes;
    
    @FXML
    private TableColumn<Cliente, String> colNome;
    
    @FXML
    private TableColumn<Cliente, String> colEmail;
    
    @FXML
    private TableColumn<Cliente, String> colTelefone;
    
    @FXML
    private TableColumn<Cliente, String> colCpf;
    
    @FXML
    private TableColumn<Cliente, String> colStatus;
    
    private ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        configurarTabela();
        carregarClientes();
    }
    
    private void configurarTabela() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        tabelaClientes.setItems(clientes);
        
        // Seleção de linha
        tabelaClientes.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    carregarClienteParaFormulario(newValue);
                }
            }
        );
    }
    
    private void carregarClientes() {
        // TODO: Implementar carregamento de dados da API
        // Por enquanto, dados de exemplo
        clientes.clear();
        clientes.add(new Cliente("João Silva", "joao@email.com", "(11) 99999-9999", "123.456.789-00", "Rua A, 123", "Cliente ativo", true));
        clientes.add(new Cliente("Maria Santos", "maria@email.com", "(11) 88888-8888", "987.654.321-00", "Rua B, 456", "Cliente ativo", true));
    }
    
    private void carregarClienteParaFormulario(Cliente cliente) {
        txtNome.setText(cliente.getNome());
        txtEmail.setText(cliente.getEmail());
        txtTelefone.setText(cliente.getTelefone());
        txtCpf.setText(cliente.getCpf());
        txtEndereco.setText(cliente.getEndereco());
        txtObservacoes.setText(cliente.getObservacoes());
        chkAtivo.setSelected(cliente.isAtivo());
    }
    
    @FXML
    public void salvarCliente() {
        if (validarFormulario()) {
            Cliente cliente = new Cliente(
                txtNome.getText(),
                txtEmail.getText(),
                txtTelefone.getText(),
                txtCpf.getText(),
                txtEndereco.getText(),
                txtObservacoes.getText(),
                chkAtivo.isSelected()
            );
            
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
        tabelaClientes.getSelectionModel().clearSelection();
    }
    
    @FXML
    public void excluirCliente() {
        Cliente clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Exclusão");
            alert.setHeaderText("Excluir Cliente");
            alert.setContentText("Deseja realmente excluir o cliente " + clienteSelecionado.getNome() + "?");
            
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // TODO: Implementar exclusão na API
                    clientes.remove(clienteSelecionado);
                    mostrarMensagem("Sucesso", "Cliente excluído com sucesso!");
                    limparFormulario();
                }
            });
        } else {
            mostrarMensagem("Aviso", "Selecione um cliente para excluir.");
        }
    }
    
    @FXML
    public void pesquisarClientes() {
        String termo = txtPesquisa.getText().toLowerCase();
        if (termo.isEmpty()) {
            tabelaClientes.setItems(clientes);
        } else {
            ObservableList<Cliente> clientesFiltrados = FXCollections.observableArrayList();
            for (Cliente cliente : clientes) {
                if (cliente.getNome().toLowerCase().contains(termo) ||
                    cliente.getEmail().toLowerCase().contains(termo) ||
                    cliente.getCpf().contains(termo)) {
                    clientesFiltrados.add(cliente);
                }
            }
            tabelaClientes.setItems(clientesFiltrados);
        }
    }
    
    private boolean validarFormulario() {
        if (txtNome.getText().trim().isEmpty()) {
            mostrarMensagem("Erro", "Nome é obrigatório.");
            return false;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            mostrarMensagem("Erro", "Email é obrigatório.");
            return false;
        }
        if (txtTelefone.getText().trim().isEmpty()) {
            mostrarMensagem("Erro", "Telefone é obrigatório.");
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
} 