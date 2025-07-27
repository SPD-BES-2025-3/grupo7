package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.math.BigDecimal;

public class VendaController {
    
    @FXML
    private ComboBox<String> cmbCliente;
    
    @FXML
    private ComboBox<String> cmbProduto;
    
    @FXML
    private TextField txtQuantidade;
    
    @FXML
    private TextField txtPrecoUnitario;
    
    @FXML
    private ComboBox<String> cmbFormaPagamento;
    
    @FXML
    private ComboBox<String> cmbStatus;
    
    @FXML
    private TextArea txtObservacoes;
    
    @FXML
    private Label lblTotalVenda;
    
    @FXML
    private TableView<?> tabelaItens;
    
    @FXML
    private TableColumn<?, ?> colProduto;
    
    @FXML
    private TableColumn<?, ?> colQuantidade;
    
    @FXML
    private TableColumn<?, ?> colPrecoUnitario;
    
    @FXML
    private TableColumn<?, ?> colSubtotal;
    
    @FXML
    private TableColumn<?, ?> colAcoes;
    
    @FXML
    private TableView<?> tabelaVendas;
    
    @FXML
    private TableColumn<?, ?> colClienteVenda;
    
    @FXML
    private TableColumn<?, ?> colDataVenda;
    
    @FXML
    private TableColumn<?, ?> colTotalVenda;
    
    @FXML
    private TableColumn<?, ?> colFormaPagamento;
    
    @FXML
    private TableColumn<?, ?> colStatusVenda;
    
    private BigDecimal totalVenda = BigDecimal.ZERO;
    
    @FXML
    public void initialize() {
        configurarComboBoxes();
        configurarTabelas();
        carregarVendas();
        atualizarTotalVenda();
    }
    
    private void configurarComboBoxes() {
        // Configurar formas de pagamento
        ObservableList<String> formasPagamento = FXCollections.observableArrayList(
            "DINHEIRO", "CARTAO_CREDITO", "CARTAO_DEBITO", "PIX"
        );
        cmbFormaPagamento.setItems(formasPagamento);
        
        // Configurar status
        ObservableList<String> status = FXCollections.observableArrayList(
            "PENDENTE", "PAGO", "CANCELADO"
        );
        cmbStatus.setItems(status);
        cmbStatus.setValue("PENDENTE");
        
        // TODO: Carregar clientes e produtos da API
        ObservableList<String> clientes = FXCollections.observableArrayList(
            "Cliente 1", "Cliente 2", "Cliente 3"
        );
        cmbCliente.setItems(clientes);
        
        ObservableList<String> produtos = FXCollections.observableArrayList(
            "Produto 1", "Produto 2", "Produto 3"
        );
        cmbProduto.setItems(produtos);
    }
    
    private void configurarTabelas() {
        // TODO: Implementar configuração das tabelas
    }
    
    private void carregarVendas() {
        // TODO: Implementar carregamento de vendas da API
    }
    
    @FXML
    public void adicionarItem() {
        if (validarItem()) {
            // TODO: Implementar adição de item
            mostrarMensagem("Sucesso", "Item adicionado com sucesso!");
            limparCamposItem();
            atualizarTotalVenda();
        }
    }
    
    @FXML
    public void finalizarVenda() {
        if (validarVenda()) {
            // TODO: Implementar finalização da venda
            mostrarMensagem("Sucesso", "Venda finalizada com sucesso!");
            limparFormulario();
            carregarVendas();
        }
    }
    
    @FXML
    public void limparFormulario() {
        cmbCliente.setValue(null);
        cmbProduto.setValue(null);
        txtQuantidade.clear();
        txtPrecoUnitario.clear();
        cmbFormaPagamento.setValue(null);
        cmbStatus.setValue("PENDENTE");
        txtObservacoes.clear();
        totalVenda = BigDecimal.ZERO;
        atualizarTotalVenda();
        // TODO: Limpar tabela de itens
    }
    
    @FXML
    public void cancelarVenda() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Cancelamento");
        alert.setHeaderText("Cancelar Venda");
        alert.setContentText("Deseja realmente cancelar esta venda?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // TODO: Implementar cancelamento na API
                mostrarMensagem("Sucesso", "Venda cancelada com sucesso!");
                limparFormulario();
                carregarVendas();
            }
        });
    }
    
    private void limparCamposItem() {
        cmbProduto.setValue(null);
        txtQuantidade.clear();
        txtPrecoUnitario.clear();
    }
    
    private boolean validarItem() {
        if (cmbProduto.getValue() == null) {
            mostrarErro("Produto é obrigatório");
            return false;
        }
        
        if (txtQuantidade.getText().trim().isEmpty()) {
            mostrarErro("Quantidade é obrigatória");
            return false;
        }
        
        try {
            Integer quantidade = Integer.parseInt(txtQuantidade.getText().trim());
            if (quantidade <= 0) {
                mostrarErro("Quantidade deve ser maior que zero");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarErro("Quantidade deve ser um número válido");
            return false;
        }
        
        if (txtPrecoUnitario.getText().trim().isEmpty()) {
            mostrarErro("Preço unitário é obrigatório");
            return false;
        }
        
        try {
            BigDecimal preco = new BigDecimal(txtPrecoUnitario.getText().trim());
            if (preco.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarErro("Preço deve ser maior que zero");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarErro("Preço deve ser um número válido");
            return false;
        }
        
        return true;
    }
    
    private boolean validarVenda() {
        if (cmbCliente.getValue() == null) {
            mostrarErro("Cliente é obrigatório");
            return false;
        }
        
        if (cmbFormaPagamento.getValue() == null) {
            mostrarErro("Forma de pagamento é obrigatória");
            return false;
        }
        
        // TODO: Verificar se há itens na venda
        if (totalVenda.compareTo(BigDecimal.ZERO) <= 0) {
            mostrarErro("Venda deve ter pelo menos um item");
            return false;
        }
        
        return true;
    }
    
    private void atualizarTotalVenda() {
        lblTotalVenda.setText(String.format("R$ %.2f", totalVenda));
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