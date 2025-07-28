package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.grupo7.petshop.model.Cliente;
import com.grupo7.petshop.model.Produto;
import com.grupo7.petshop.model.Venda;
import com.grupo7.petshop.model.DatabaseManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
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
    
    private BigDecimal totalVenda = BigDecimal.ZERO;
    
    private ObservableList<ItemVenda> itensVenda = FXCollections.observableArrayList();
    
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

        // Carregar clientes do banco
        try {
            Dao<Cliente, Integer> clienteDao = DatabaseManager.getClienteDao();
            List<Cliente> listaClientes = clienteDao.queryForAll();
            ObservableList<String> clientes = FXCollections.observableArrayList(
                listaClientes.stream().map(c -> c.getNome() + " (" + c.getCpf() + ")").collect(Collectors.toList())
            );
            cmbCliente.setItems(clientes);
        } catch (SQLException e) {
            cmbCliente.setItems(FXCollections.observableArrayList());
            mostrarErro("Erro ao carregar clientes: " + e.getMessage());
        }

        // Carregar produtos do banco
        try {
            Dao<Produto, Integer> produtoDao = DatabaseManager.getProdutoDao();
            List<Produto> listaProdutos = produtoDao.queryForAll();
            ObservableList<String> produtos = FXCollections.observableArrayList(
                listaProdutos.stream().map(Produto::getNome).collect(Collectors.toList())
            );
            cmbProduto.setItems(produtos);
        } catch (SQLException e) {
            cmbProduto.setItems(FXCollections.observableArrayList());
            mostrarErro("Erro ao carregar produtos: " + e.getMessage());
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
            Dao<Venda, Integer> vendaDao = DatabaseManager.getVendaDao();
            List<Venda> lista = vendaDao.queryForAll();
            tabelaVendas.setItems(FXCollections.observableArrayList(lista));
        } catch (SQLException e) {
            tabelaVendas.setItems(FXCollections.observableArrayList());
            mostrarErro("Erro ao carregar vendas: " + e.getMessage());
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
        if (validarVenda()) {
            try {
                Dao<com.grupo7.petshop.model.Venda, Integer> vendaDao = com.grupo7.petshop.model.DatabaseManager.getVendaDao();
                // Extrair dados do formulário
                String clienteStr = cmbCliente.getValue();
                String clienteCpf = "";
                if (clienteStr != null && clienteStr.contains("(") && clienteStr.contains(")")) {
                    int ini = clienteStr.lastIndexOf('(') + 1;
                    int fim = clienteStr.lastIndexOf(')');
                    clienteCpf = clienteStr.substring(ini, fim);
                }
                java.util.Date data = new java.util.Date(); // Data atual
                double valorTotal = totalVenda.doubleValue();
                com.grupo7.petshop.model.Venda venda = new com.grupo7.petshop.model.Venda(clienteCpf, data, valorTotal);
                vendaDao.create(venda);
                mostrarMensagem("Sucesso", "Venda finalizada com sucesso!");
                limparFormulario();
                carregarVendas();
            } catch (Exception e) {
                mostrarErro("Erro ao salvar venda: " + e.getMessage());
            }
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