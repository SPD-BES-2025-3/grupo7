package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import com.grupo7.petshop.model.Cliente;
import com.grupo7.petshop.model.DatabaseManager;
import com.j256.ormlite.dao.Dao;

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
    
    private Dao<Cliente, Integer> clienteDao;
    
    private Cliente clienteSelecionado;
    
    @FXML
    public void initialize() {
        try {
            clienteDao = DatabaseManager.getClienteDao();
            configurarTabela();
            carregarClientes();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            clientes.clear();
            clientes.addAll(clienteDao.queryForAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void carregarClienteParaFormulario(Cliente cliente) {
        this.clienteSelecionado = cliente;
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
        System.out.println("Tentando salvar cliente...");
        if (validarFormulario()) {
            try {
                Cliente cliente;
                if (clienteSelecionado != null) {
                    // Atualização: mantém o id
                    cliente = new Cliente(
                        txtNome.getText(),
                        txtEmail.getText(),
                        txtTelefone.getText(),
                        txtCpf.getText(),
                        txtEndereco.getText(),
                        txtObservacoes.getText(),
                        chkAtivo.isSelected()
                    );
                    // Supondo que seu modelo tem setId/getId
                    cliente.setId(clienteSelecionado.getId());
                } else {
                    // Novo cliente
                    cliente = new Cliente(
                        txtNome.getText(),
                        txtEmail.getText(),
                        txtTelefone.getText(),
                        txtCpf.getText(),
                        txtEndereco.getText(),
                        txtObservacoes.getText(),
                        chkAtivo.isSelected()
                    );
                }
                clienteDao.createOrUpdate(cliente);
                System.out.println("Cliente salvo no banco!");
                mostrarMensagem("Sucesso", "Cliente salvo com sucesso!");
                limparFormulario();
                carregarClientes();
                clienteSelecionado = null;
            } catch (Exception e) {
                e.printStackTrace();
                mostrarMensagem("Erro", "Erro ao salvar cliente: " + e.getMessage());
            }
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
        clienteSelecionado = null;
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
                    try {
                        clienteDao.delete(clienteSelecionado);
                        clientes.remove(clienteSelecionado);
                        mostrarMensagem("Sucesso", "Cliente excluído com sucesso!");
                        limparFormulario();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            mostrarMensagem("Aviso", "Selecione um cliente para excluir.");
        }
    }
    
    @FXML
    public void pesquisarClientes() {
        System.out.println("Método pesquisarClientes chamado!");
        String termo = txtPesquisa.getText().toLowerCase();
        try {
            if (termo.isEmpty()) {
                clientes.setAll(clienteDao.queryForAll());
            } else {
                ObservableList<Cliente> clientesFiltrados = FXCollections.observableArrayList();
                for (Cliente cliente : clienteDao.queryForAll()) {
                    if (cliente.getNome().toLowerCase().contains(termo) ||
                        cliente.getEmail().toLowerCase().contains(termo) ||
                        cliente.getCpf().contains(termo)) {
                        clientesFiltrados.add(cliente);
                    }
                }
                clientes.setAll(clientesFiltrados);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tabelaClientes.setItems(clientes);
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