package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import com.grupo7.petshop.model.Cliente;
import javafx.scene.control.cell.PropertyValueFactory;

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
    
    @FXML
    public void initialize() {
        carregarClientes();
        configurarTabela();
    }
    
    private void configurarTabela() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("ativo"));
    }
    
    private void carregarClientes() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/clientes"))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Cliente> clientes = mapper.readValue(response.body(),
                mapper.getTypeFactory().constructCollectionType(List.class, Cliente.class));
            tabelaClientes.getItems().setAll(clientes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void criarCliente(Cliente novoCliente) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(novoCliente);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/clientes"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 201 || response.statusCode() == 200) {
                carregarClientes(); // Atualiza a tabela
            } else {
                // Trate o erro conforme necessário
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void salvarCliente() {
        if (validarFormulario()) {
            Cliente novoCliente = new Cliente();
            novoCliente.setNome(txtNome.getText().trim());
            novoCliente.setEmail(txtEmail.getText().trim());
            novoCliente.setTelefone(txtTelefone.getText().trim());
            novoCliente.setCpf(txtCpf.getText().trim());
            novoCliente.setEndereco(txtEndereco.getText().trim());
            novoCliente.setObservacoes(txtObservacoes.getText());
            novoCliente.setAtivo(chkAtivo.isSelected());

            criarCliente(novoCliente);
            mostrarMensagem("Sucesso", "Cliente salvo com sucesso!");
            limparFormulario();
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