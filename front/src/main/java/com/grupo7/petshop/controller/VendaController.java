package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import java.io.IOException;
import javafx.collections.ObservableList;
import com.grupo7.petshop.model.Venda;
import com.grupo7.petshop.model.Cliente;
import com.grupo7.petshop.model.Produto;
import com.grupo7.petshop.service.ApiService;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

public class VendaController {
    
    @FXML
    private ComboBox<Cliente> cmbCliente;
    
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
    private TableView<ItemVenda> tabelaItens;
    @FXML
    private TableColumn<ItemVenda, String> colProduto;
    @FXML
    private TableColumn<ItemVenda, Integer> colQuantidade;
    @FXML
    private TableColumn<ItemVenda, Double> colPrecoUnitario;
    @FXML
    private TableColumn<ItemVenda, Double> colSubtotal;
    @FXML
    private TableColumn<ItemVenda, ItemVenda> colAcoes;
    
    @FXML
    private TableView<Venda> tabelaVendas;
    @FXML
    private TableColumn<Venda, String> colClienteVenda;
    @FXML
    private TableColumn<Venda, String> colDataVenda;
    @FXML
    private TableColumn<Venda, String> colTotalVenda;
    @FXML
    private TableColumn<Venda, String> colFormaPagamento;
    @FXML
    private TableColumn<Venda, String> colStatusVenda;
    
    private BigDecimal totalVenda = BigDecimal.ZERO;
    
    private ObservableList<ItemVenda> itensVenda = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        carregarClientes();
        carregarProdutos();
        carregarVendas();
        configurarTabelas();
        atualizarTotalVenda();
    }
    
    private void carregarClientes() {
        try {
            List<Cliente> clientes = ApiService.getAllClientes();
            ObservableList<Cliente> observableClientes = FXCollections.observableArrayList(clientes);
            cmbCliente.setItems(observableClientes);
        } catch (Exception e) {
            mostrarMensagem("Erro", "Erro ao carregar clientes: " + e.getMessage());
        }
    }
    
    private void carregarProdutos() {
        try {
            List<Produto> produtos = ApiService.getAllProdutos();
            ObservableList<String> produtosStr = FXCollections.observableArrayList(
                produtos.stream().map(Produto::getNome).collect(Collectors.toList())
            );
            cmbProduto.setItems(produtosStr);
        } catch (Exception e) {
            mostrarMensagem("Erro", "Erro ao carregar produtos: " + e.getMessage());
        }
    }
    
    private void configurarTabelas() {
        // Tabela de itens
        tabelaItens.setItems(itensVenda);
        colProduto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProduto()));
        colQuantidade.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getQuantidade()).asObject());
        colPrecoUnitario.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPrecoUnitario()).asObject());
        colSubtotal.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getSubtotal()).asObject());
        colAcoes.setCellValueFactory(param -> new javafx.beans.property.ReadOnlyObjectWrapper<>(param.getValue()));
        colAcoes.setCellFactory(param -> new TableCell<ItemVenda, ItemVenda>() {
            private final Button btnRemover = new Button("Remover");
            {
                btnRemover.setOnAction(event -> {
                    ItemVenda item = getItem();
                    itensVenda.remove(item);
                    atualizarTotalVenda();
                });
            }
            @Override
            protected void updateItem(ItemVenda item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(btnRemover);
                }
            }
        });
        // Tabela de vendas
        colClienteVenda.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClienteCpf()));
        colDataVenda.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getData() != null ? cellData.getValue().getData().toString() : ""));
        colTotalVenda.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getValorTotal())));
    }
    
    private void carregarVendas() {
        try {
            List<Venda> vendas = ApiService.getAllVendas();
            ObservableList<Venda> observableVendas = FXCollections.observableArrayList(vendas);
            tabelaVendas.setItems(observableVendas);
        } catch (Exception e) {
            mostrarMensagem("Erro", "Erro ao carregar vendas: " + e.getMessage());
        }
    }
    
    @FXML
    public void adicionarItem() {
        if (validarItem()) {
            String produto = cmbProduto.getValue();
            int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
            double precoUnitario = Double.parseDouble(txtPrecoUnitario.getText().trim());
            ItemVenda item = new ItemVenda(produto, quantidade, precoUnitario);
            itensVenda.add(item);
            atualizarTotalVenda();
            mostrarMensagem("Sucesso", "Item adicionado com sucesso!");
            limparCamposItem();
        }
    }

    @FXML
    public void finalizarVenda() {
        salvarVenda();
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
        itensVenda.clear();
        totalVenda = BigDecimal.ZERO;
        atualizarTotalVenda();
    }

    @FXML
    public void cancelarVenda() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Cancelamento");
        alert.setHeaderText("Cancelar Venda");
        alert.setContentText("Deseja realmente cancelar esta venda?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Venda vendaSelecionada = tabelaVendas.getSelectionModel().getSelectedItem();
                if (vendaSelecionada != null) {
                    try {
                        ApiService.deleteVenda(String.valueOf(vendaSelecionada.getId()));
                    } catch (IOException | InterruptedException e) {
                        mostrarMensagem("Erro", "Erro ao cancelar venda: " + e.getMessage());
                    }
                }
                mostrarMensagem("Sucesso", "Venda cancelada com sucesso!");
                limparFormulario();
                carregarVendas();
            }
        });
    }
    
    @FXML
    public void salvarVenda() {
        if (validarVenda()) {
            try {
                // Extrair CPF do cliente selecionado
                Cliente clienteSelecionado = cmbCliente.getValue();
                String clienteCpf = "";
                if (clienteSelecionado != null) {
                    clienteCpf = clienteSelecionado.getCpf();
                }
                
                // Calcular valor total
                int quantidade = Integer.parseInt(txtQuantidade.getText());
                double precoUnitario = Double.parseDouble(txtPrecoUnitario.getText());
                double valorTotal = quantidade * precoUnitario;
                
                Venda venda = new Venda(clienteCpf, new java.util.Date(), valorTotal);
                ApiService.createVenda(venda);
                mostrarMensagem("Sucesso", "Venda salva com sucesso!");
                limparFormulario();
                carregarVendas();
            } catch (Exception e) {
                mostrarMensagem("Erro", "Erro ao salvar venda: " + e.getMessage());
            }
        }
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
        if (itensVenda.isEmpty()) {
            mostrarErro("Venda deve ter pelo menos um item");
            return false;
        }
        return true;
    }
    
    private void atualizarTotalVenda() {
        totalVenda = BigDecimal.valueOf(itensVenda.stream().mapToDouble(ItemVenda::getSubtotal).sum());
        lblTotalVenda.setText(String.format("R$ %.2f", totalVenda));
    }

    // Classe interna para representar um item da venda
    public static class ItemVenda {
        private final String produto;
        private final int quantidade;
        private final double precoUnitario;
        public ItemVenda(String produto, int quantidade, double precoUnitario) {
            this.produto = produto;
            this.quantidade = quantidade;
            this.precoUnitario = precoUnitario;
        }
        public String getProduto() { return produto; }
        public int getQuantidade() { return quantidade; }
        public double getPrecoUnitario() { return precoUnitario; }
        public double getSubtotal() { return quantidade * precoUnitario; }
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