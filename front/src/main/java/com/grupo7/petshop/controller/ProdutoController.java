package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import java.io.IOException;
import javafx.collections.ObservableList;
import com.grupo7.petshop.model.Produto;
import com.grupo7.petshop.service.ApiService;
import java.util.List;
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
    private TableView<Produto> tabelaProdutos;
    @FXML
    private TableColumn<Produto, String> colNome;
    @FXML
    private TableColumn<Produto, String> colCodigo;
    @FXML
    private TableColumn<Produto, String> colCategoria;
    @FXML
    private TableColumn<Produto, String> colPreco;
    @FXML
    private TableColumn<Produto, String> colEstoque;
    @FXML
    private TableColumn<Produto, String> colMarca;
    @FXML
    private TableColumn<Produto, String> colStatus;
    
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
        colNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
        // colCodigo, colCategoria, colMarca, colStatus não existem no modelo Produto, então não configuramos
        colPreco.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getPreco())));
        colEstoque.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getQuantidadeEstoque())));
    }
    
    private void carregarProdutos() {
        try {
            List<Produto> produtos = ApiService.getAllProdutos();
            ObservableList<Produto> observableProdutos = FXCollections.observableArrayList(produtos);
            tabelaProdutos.setItems(observableProdutos);
        } catch (Exception e) {
            mostrarMensagem("Erro", "Erro ao carregar produtos: " + e.getMessage());
        }
    }
    
    @FXML
    public void salvarProduto() {
        if (validarFormulario()) {
            try {
                String nome = txtNome.getText().trim();
                String codigo = txtCodigo.getText().trim();
                String categoria = cmbCategoria.getValue();
                String descricao = txtDescricao.getText().trim();
                double preco = Double.parseDouble(txtPreco.getText().trim());
                int quantidadeEstoque = Integer.parseInt(txtEstoque.getText().trim());
                Produto produto = new Produto(nome, codigo, categoria, descricao, preco, quantidadeEstoque);
                ApiService.createProduto(produto);
                mostrarMensagem("Sucesso", "Produto salvo com sucesso!");
                limparFormulario();
                carregarProdutos();
            } catch (Exception e) {
                mostrarErro("Erro ao salvar produto: " + e.getMessage());
            }
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
                Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
                if (produtoSelecionado != null) {
                    try {
    ApiService.deleteProduto(String.valueOf(produtoSelecionado.getId()));
} catch (IOException | InterruptedException e) {
    mostrarMensagem("Erro", "Erro ao excluir produto: " + e.getMessage());
}
                }
                mostrarMensagem("Sucesso", "Produto excluído com sucesso!");
                carregarProdutos();
            }
        });
    }
    
    @FXML
    public void pesquisarProdutos() {
        String termo = txtPesquisa.getText().trim();
        if (!termo.isEmpty()) {
            try {
                List<Produto> produtos = ApiService.buscarProdutosPorNome(termo);
                ObservableList<Produto> observableProdutos = FXCollections.observableArrayList(produtos);
                tabelaProdutos.setItems(observableProdutos);
            } catch (Exception e) {
                mostrarMensagem("Erro", "Erro ao pesquisar produtos: " + e.getMessage());
            }
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