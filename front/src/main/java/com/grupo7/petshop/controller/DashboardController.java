package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.cell.PropertyValueFactory;
import com.grupo7.petshop.model.Cliente;
import com.grupo7.petshop.model.Pet;
import com.grupo7.petshop.model.Agendamento;
import com.grupo7.petshop.model.Venda;
import com.grupo7.petshop.service.ApiService;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import java.text.SimpleDateFormat;

public class DashboardController {
    
    @FXML
    private Label totalClientesLabel;
    
    @FXML
    private Label totalPetsLabel;
    
    @FXML
    private Label agendamentosHojeLabel;
    
    @FXML
    private Label vendasMesLabel;
    
    @FXML
    private TableView<Agendamento> agendamentosTableView;
    @FXML
    private TableColumn<Agendamento, String> colCliente;
    @FXML
    private TableColumn<Agendamento, String> colPet;
    @FXML
    private TableColumn<Agendamento, String> colServico;
    @FXML
    private TableColumn<Agendamento, String> colDataHora;
    @FXML
    private TableColumn<Agendamento, String> colStatus;
    
    @FXML
    public void initialize() {
        // CONFIGURAR AS COLUNAS DA TABELA - ISSO ERA O QUE ESTAVA FALTANDO!
        configurarColunas();
        carregarEstatisticas();
        carregarAgendamentosRecentes();
    }
    
    private void configurarColunas() {
        // Mapeando as colunas para as propriedades do objeto Agendamento
        colCliente.setCellValueFactory(new PropertyValueFactory<>("clienteCpf"));
        colPet.setCellValueFactory(new PropertyValueFactory<>("petNome"));
        colServico.setCellValueFactory(new PropertyValueFactory<>("servico"));
        
        // Para data/hora, vamos usar uma factory customizada para formatar
        colDataHora.setCellValueFactory(cellData -> {
            String dataHoraStr = cellData.getValue().getDataHora();
            if (dataHoraStr != null && !dataHoraStr.isEmpty()) {
                try {
                    java.time.LocalDateTime ldt = java.time.LocalDateTime.parse(dataHoraStr);
                    String formatado = ldt.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                    return new javafx.beans.property.SimpleStringProperty(formatado);
                } catch (Exception e) {
                    return new javafx.beans.property.SimpleStringProperty(dataHoraStr); // fallback
                }
            } else {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });
        
        // Para status, vamos criar um status baseado na data (exemplo)
        colStatus.setCellValueFactory(cellData -> {
            // Como não há campo status no modelo, vamos criar um baseado na data
            if (cellData.getValue().getDataHora() != null) {
                java.time.LocalDateTime agendamentoData = java.time.LocalDateTime.parse(cellData.getValue().getDataHora());
                java.time.LocalDateTime agora = java.time.LocalDateTime.now();
                
                if (agendamentoData.isBefore(agora)) {
                    return new javafx.beans.property.SimpleStringProperty("Realizado");
                } else if (agendamentoData.toLocalDate().equals(agora.toLocalDate())) {
                    return new javafx.beans.property.SimpleStringProperty("Hoje");
                } else {
                    return new javafx.beans.property.SimpleStringProperty("Agendado");
                }
            }
            return new javafx.beans.property.SimpleStringProperty("Pendente");
        });
    }
    
    private void carregarEstatisticas() {
        try {
            int totalClientes = ApiService.getAllClientes().size();
            int totalPets = ApiService.getAllPets().size();
            int agendamentosHoje = (int) ApiService.getAllAgendamentos().stream()
                .filter(a -> a.getDataHora() != null &&
                    java.time.LocalDateTime.parse(a.getDataHora()).toLocalDate().equals(java.time.LocalDate.now()))
                .count();
            double vendasMes = ApiService.getAllVendas().stream()
                .filter(v -> v.getData() != null &&
                    v.getData().toInstant().atZone(java.time.ZoneId.systemDefault()).getMonthValue() == java.time.LocalDate.now().getMonthValue())
                .mapToDouble(Venda::getValorTotal)
                .sum();

            totalClientesLabel.setText(String.valueOf(totalClientes));
            totalPetsLabel.setText(String.valueOf(totalPets));
            agendamentosHojeLabel.setText(String.valueOf(agendamentosHoje));
            vendasMesLabel.setText(String.format("R$ %.2f", vendasMes));
        } catch (java.io.IOException | InterruptedException e) {
            totalClientesLabel.setText("0");
            totalPetsLabel.setText("0");
            agendamentosHojeLabel.setText("0");
            vendasMesLabel.setText("R$ 0,00");
            mostrarMensagem("Erro", "Erro ao carregar estatísticas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void carregarAgendamentosRecentes() {
        try {
            List<Agendamento> lista = ApiService.getAllAgendamentos();
            // Limitar aos últimos 10 agendamentos para não sobrecarregar a tela
            if (lista.size() > 10) {
                lista = lista.subList(0, 10);
            }
            agendamentosTableView.setItems(FXCollections.observableArrayList(lista));
            // Debug - verificar se os dados estão sendo carregados
            System.out.println("Agendamentos carregados: " + lista.size());
            for (Agendamento ag : lista) {
                System.out.println("Cliente: " + ag.getClienteCpf() + ", Pet: " + ag.getPetNome() + ", Serviço: " + ag.getServico());
            }
        } catch (java.io.IOException | InterruptedException e) {
            agendamentosTableView.setItems(FXCollections.observableArrayList());
            mostrarMensagem("Erro", "Erro ao carregar agendamentos recentes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    public void novoCliente(ActionEvent event) {
        trocarConteudoCentral("/com/grupo7/petshop/ClienteView.fxml", event);
    }

    @FXML
    public void novoPet(ActionEvent event) {
        trocarConteudoCentral("/com/grupo7/petshop/PetView.fxml", event);
    }

    @FXML
    public void novoAgendamento(ActionEvent event) {
        trocarConteudoCentral("/com/grupo7/petshop/AgendamentoView.fxml", event);
    }

    @FXML
    public void novaVenda(ActionEvent event) {
        trocarConteudoCentral("/com/grupo7/petshop/VendaView.fxml", event);
    }

    private void trocarConteudoCentral(String fxmlPath, ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Scene scene = source.getScene();
            StackPane contentArea = (StackPane) scene.lookup("#contentArea");
            if (contentArea == null) {
                mostrarMensagem("Erro", "Não foi possível encontrar o painel central (contentArea).");
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent novoConteudo = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(novoConteudo);
        } catch (Exception e) {
            mostrarMensagem("Erro", "Não foi possível abrir a tela: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void mostrarMensagem(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}