package com.grupo7.petshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import java.io.IOException;
import javafx.collections.ObservableList;
import com.grupo7.petshop.model.Pet;
import com.grupo7.petshop.model.Cliente;
import com.grupo7.petshop.service.ApiService;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

public class PetController {
    
    @FXML
    private TextField txtNome;
    
    @FXML
    private ComboBox<String> cmbEspecie;
    
    @FXML
    private TextField txtRaca;
    
    @FXML
    private DatePicker dpDataNascimento;
    
    @FXML
    private TextField txtCor;
    
    @FXML
    private ComboBox<String> cmbSexo;
    
    @FXML
    private TextField txtPeso;
    
    @FXML
    private ComboBox<Cliente> cmbCliente;
    
    @FXML
    private TextArea txtObservacoes;
    
    @FXML
    private CheckBox chkAtivo;
    
    @FXML
    private TextField txtPesquisa;
    
    @FXML
    private TableView<Pet> tabelaPets;
    @FXML
    private TableColumn<Pet, String> colNome;
    @FXML
    private TableColumn<Pet, String> colEspecie;
    @FXML
    private TableColumn<Pet, String> colRaca;
    @FXML
    private TableColumn<Pet, String> colCliente;
    @FXML
    private TableColumn<Pet, String> colIdade;
    @FXML
    private TableColumn<Pet, String> colPeso;
    @FXML
    private TableColumn<Pet, String> colStatus;
    
    @FXML
    public void initialize() {
        configurarComboBoxes();
        configurarTabela();
        carregarPets();
        carregarClientes();
    }
    
    private void configurarComboBoxes() {
        // Configurar espécies
        ObservableList<String> especies = FXCollections.observableArrayList(
            "Cachorro", "Gato", "Pássaro", "Peixe", "Hamster", "Coelho", "Outros"
        );
        cmbEspecie.setItems(especies);

        // Configurar sexo
        ObservableList<String> sexos = FXCollections.observableArrayList(
            "Macho", "Fêmea"
        );
        cmbSexo.setItems(sexos);
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
    
    private void configurarTabela() {
        colNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
        colEspecie.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEspecie()));
        colRaca.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRaca()));
        colCliente.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDonoCpf()));
        colIdade.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getIdade())));
        colPeso.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty("")); // Ajuste se adicionar peso no modelo
        colStatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty("")); // Ajuste se adicionar status no modelo
    }
    
    private void carregarPets() {
        try {
            List<Pet> pets = ApiService.getAllPets();
            ObservableList<Pet> observablePets = FXCollections.observableArrayList(pets);
            tabelaPets.setItems(observablePets);
        } catch (Exception e) {
            mostrarMensagem("Erro", "Erro ao carregar pets: " + e.getMessage());
        }
    }
    
    @FXML
    public void salvarPet() {
        if (validarCampos()) {
            try {
                Pet pet = new Pet();
                pet.setNome(txtNome.getText());
                pet.setEspecie(cmbEspecie.getValue());
                pet.setRaca(txtRaca.getText());
                pet.setDataNascimento(dpDataNascimento.getValue().toString());
                pet.setIdade(LocalDate.now().getYear() - dpDataNascimento.getValue().getYear());
                
                pet.setDonoCpf(cmbCliente.getValue().getCpf());
                
                ApiService.createPet(pet);
                mostrarMensagem("Sucesso", "Pet salvo com sucesso!");
                limparFormulario();
                carregarPets();
            } catch (Exception e) {
                mostrarMensagem("Erro", "Erro ao salvar pet: " + e.getMessage());
            }
        }
    }
    
    @FXML
    public void limparFormulario() {
        txtNome.clear();
        cmbEspecie.setValue(null);
        txtRaca.clear();
        dpDataNascimento.setValue(null);
        txtCor.clear();
        cmbSexo.setValue(null);
        txtPeso.clear();
        cmbCliente.setValue(null);
        txtObservacoes.clear();
        chkAtivo.setSelected(true);
    }
    
    @FXML
    public void excluirPet() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir Pet");
        alert.setContentText("Deseja realmente excluir este pet?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Pet petSelecionado = tabelaPets.getSelectionModel().getSelectedItem();
                if (petSelecionado != null) {
                    try {
    ApiService.deletePet(String.valueOf(petSelecionado.getId()));
} catch (IOException | InterruptedException e) {
    mostrarMensagem("Erro", "Erro ao excluir pet: " + e.getMessage());
}
                }
                mostrarMensagem("Sucesso", "Pet excluído com sucesso!");
                carregarPets();
            }
        });
    }
    
    @FXML
    public void pesquisarPets() {
        String termo = txtPesquisa.getText().trim();
        if (!termo.isEmpty()) {
            try {
                List<Pet> pets = ApiService.buscarPetsPorNome(termo);
                ObservableList<Pet> observablePets = FXCollections.observableArrayList(pets);
                tabelaPets.setItems(observablePets);
            } catch (Exception e) {
                mostrarMensagem("Erro", "Erro ao pesquisar pets: " + e.getMessage());
            }
        } else {
            carregarPets();
        }
    }
    
    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty()) {
            mostrarErro("Nome é obrigatório");
            return false;
        }
        
        if (cmbEspecie.getValue() == null) {
            mostrarErro("Espécie é obrigatória");
            return false;
        }
        
        if (txtRaca.getText().trim().isEmpty()) {
            mostrarErro("Raça é obrigatória");
            return false;
        }
        
        if (dpDataNascimento.getValue() == null) {
            mostrarErro("Data de nascimento é obrigatória");
            return false;
        }
        
        if (dpDataNascimento.getValue().isAfter(LocalDate.now())) {
            mostrarErro("Data de nascimento deve ser no passado");
            return false;
        }
        
        if (cmbCliente.getValue() == null) {
            mostrarErro("Cliente é obrigatório");
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