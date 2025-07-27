package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.math.BigDecimal;

public class ProdutoController {
    
    @FXML
    private TextField txtNome;
    
    @FXML
    private TextField txtCodigo;
    
    @FXML
    private ComboBox<String> cmbCategoria;
    
    @FXML
    private TextArea txtDescricao;
    
    @FXML
    private TextField txtPreco;
    
    @FXML
    private TextField txtEstoque;
    
    @FXML
    private TextField txtMarca;
    
    @FXML
    private TextField txtTamanho;
    
    @FXML
    private TextField txtPeso;
    
    @FXML
    private CheckBox chkAtivo;
    
    @FXML
    private TextField txtPesquisa;
    
    @FXML
    private TableView<?> tabelaProdutos;
    
    @FXML
    private TableColumn<?, ?> colNome;
    
    @FXML
    private TableColumn<?, ?> colCodigo;
    
    @FXML
    private TableColumn<?, ?> colCategoria;
    
    @FXML
    private TableColumn<?, ?> colPreco;
    
    @FXML
    private TableColumn<?, ?> colEstoque;
    
    @FXML
    private TableColumn<?, ?> colMarca;
    
    @FXML
    private TableColumn<?, ?> colStatus;
    
    @FXML
    public void initialize() {
        configurarComboBoxes();
        configurarTabela();
        carregarProdutos();
    }
    
    private void configurarComboBoxes() {
        // Configurar categorias
        ObservableList<String> categorias = FXCollections.observableArrayList(
            "Ração", "Brinquedo", "Medicamento", "Higiene", "Acessório", "Outros"
        );
        cmbCategoria.setItems(categorias);
    }
    
    private void configurarTabela() {
        // TODO: Implementar configuração da tabela
    }
    
    private void carregarProdutos() {
        // TODO: Implementar carregamento de produtos da API
    }
    
    @FXML
    public void salvarProduto() {
        if (validarFormulario()) {
            // TODO: Implementar salvamento na API
            mostrarMensagem("Sucesso", "Produto salvo com sucesso!");
            limparFormulario();
            carregarProdutos();
        }
    }
    
    @FXML
    public void limparFormulario() {
        txtNome.clear();
        txtCodigo.clear();
        cmbCategoria.setValue(null);
        txtDescricao.clear();
        txtPreco.clear();
        txtEstoque.clear();
        txtMarca.clear();
        txtTamanho.clear();
        txtPeso.clear();
        chkAtivo.setSelected(true);
    }
    
    @FXML
    public void excluirProduto() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir Produto");
        alert.setContentText("Deseja realmente excluir este produto?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // TODO: Implementar exclusão na API
                mostrarMensagem("Sucesso", "Produto excluído com sucesso!");
                carregarProdutos();
            }
        });
    }
    
    @FXML
    public void pesquisarProdutos() {
        String termo = txtPesquisa.getText().trim();
        if (!termo.isEmpty()) {
            // TODO: Implementar pesquisa na API
            mostrarMensagem("Pesquisa", "Pesquisando por: " + termo);
        } else {
            carregarProdutos();
        }
    }
    
    private boolean validarFormulario() {
        if (txtNome.getText().trim().isEmpty()) {
            mostrarErro("Nome é obrigatório");
            return false;
        }
        
        if (txtCodigo.getText().trim().isEmpty()) {
            mostrarErro("Código é obrigatório");
            return false;
        }
        
        if (cmbCategoria.getValue() == null) {
            mostrarErro("Categoria é obrigatória");
            return false;
        }
        
        if (txtDescricao.getText().trim().isEmpty()) {
            mostrarErro("Descrição é obrigatória");
            return false;
        }
        
        if (txtPreco.getText().trim().isEmpty()) {
            mostrarErro("Preço é obrigatório");
            return false;
        }
        
        try {
            BigDecimal preco = new BigDecimal(txtPreco.getText().trim());
            if (preco.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarErro("Preço deve ser maior que zero");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarErro("Preço deve ser um número válido");
            return false;
        }
        
        if (txtEstoque.getText().trim().isEmpty()) {
            mostrarErro("Estoque é obrigatório");
            return false;
        }
        
        try {
            Integer estoque = Integer.parseInt(txtEstoque.getText().trim());
            if (estoque < 0) {
                mostrarErro("Estoque deve ser maior ou igual a zero");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarErro("Estoque deve ser um número válido");
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