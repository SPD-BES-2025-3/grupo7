package com.grupo7.petshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import com.grupo7.petshop.model.Cliente;
import com.grupo7.petshop.service.ApiService;
import java.util.List;

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
    
    private Cliente clienteSelecionado;
    
    @FXML
    public void initialize() {
        try {
            colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
            colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));

            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            
            carregarClientes();
        } catch (Exception e) {
            mostrarMensagem("Erro", "Erro ao inicializar: " + e.getMessage());
        }
    }
    
    private void carregarClientes() {
        try {
            List<Cliente> clientes = ApiService.getAllClientes();
            ObservableList<Cliente> observableClientes = FXCollections.observableArrayList(clientes);
            tabelaClientes.setItems(observableClientes);
        } catch (Exception e) {
            mostrarMensagem("Erro", "Erro ao carregar clientes: " + e.getMessage());
        }
    }
    
    private void carregarClienteParaFormulario(Cliente cliente) {
        this.clienteSelecionado = cliente;
        txtNome.setText(cliente.getNome());
        txtEmail.setText(cliente.getEmail());
        txtTelefone.setText(cliente.getTelefone());
        txtCpf.setText(cliente.getCpf());
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
                        txtObservacoes.getText(),
                        chkAtivo.isSelected()
                    );
                }
                if (clienteSelecionado != null) {
                    // Atualização (PUT) - envia id
                    ApiService.updateCliente(String.valueOf(clienteSelecionado.getId()), cliente);
                } else {
                    // Criação (POST) - não envia id
                    try {
                        // Debug: imprimir JSON enviado
                        ObjectMapper mapper = new ObjectMapper();
                        String jsonDebug = mapper.writeValueAsString(cliente);
                        System.out.println("JSON enviado ao criar cliente: " + jsonDebug);
                    } catch (Exception ex) {
                        System.out.println("Erro ao serializar cliente para debug: " + ex.getMessage());
                    }
                    ApiService.createCliente(cliente);
                }
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
        txtObservacoes.clear();
        chkAtivo.setSelected(true);
        tabelaClientes.getSelectionModel().clearSelection();
        clienteSelecionado = null;
    }
    
    @FXML
    public void editarCliente() {
        Cliente clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            try {
                clienteSelecionado.setNome(txtNome.getText());
                clienteSelecionado.setEmail(txtEmail.getText());
                clienteSelecionado.setTelefone(txtTelefone.getText());
                clienteSelecionado.setCpf(txtCpf.getText());
                clienteSelecionado.setObservacoes(txtObservacoes.getText());
                clienteSelecionado.setAtivo(chkAtivo.isSelected());
                
                ApiService.updateCliente(String.valueOf(clienteSelecionado.getId()), clienteSelecionado);
                mostrarMensagem("Sucesso", "Cliente atualizado com sucesso!");
                limparFormulario();
                carregarClientes();
            } catch (Exception e) {
                mostrarMensagem("Erro", "Erro ao atualizar cliente: " + e.getMessage());
            }
        } else {
            mostrarMensagem("Aviso", "Selecione um cliente para editar.");
        }
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
                        ApiService.deleteCliente(String.valueOf(clienteSelecionado.getId()));
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
        String termo = txtPesquisa.getText().trim();
        if (!termo.isEmpty()) {
            try {
                List<Cliente> clientes = ApiService.buscarClientesPorNome(termo);
                ObservableList<Cliente> observableClientes = FXCollections.observableArrayList(clientes);
                tabelaClientes.setItems(observableClientes);
            } catch (Exception e) {
                mostrarMensagem("Erro", "Erro ao pesquisar clientes: " + e.getMessage());
            }
        } else {
            carregarClientes();
        }
    }
    
    private boolean validarFormulario() {
        // Obter valores dos campos
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String cpf = txtCpf.getText().trim();
        String endereco = txtEndereco.getText().trim();

        // Validação de campos obrigatórios
        if (nome.isEmpty()) {
            mostrarMensagem("Erro", "Nome é obrigatório.");
            return false;
        }
        if (email.isEmpty()) {
            mostrarMensagem("Erro", "Email é obrigatório.");
            return false;
        }
        // Validação básica de email
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            mostrarMensagem("Erro", "Email inválido.");
            return false;
        }
        if (telefone.isEmpty()) {
            mostrarMensagem("Erro", "Telefone é obrigatório.");
            return false;
        }
        // Formato: (99) 99999-9999
        if (!telefone.matches("\\(\\d{2}\\) \\d{4,5}-\\d{4}")) {
            mostrarMensagem("Erro", "Telefone deve estar no formato (99) 99999-9999");
            return false;
        }
        if (cpf.isEmpty()) {
            mostrarMensagem("Erro", "CPF é obrigatório.");
            return false;
        }
        // Formato: 999.999.999-99
        if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            mostrarMensagem("Erro", "CPF deve estar no formato 999.999.999-99");
            return false;
        }
        if (endereco.isEmpty()) {
            mostrarMensagem("Erro", "Endereço é obrigatório.");
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