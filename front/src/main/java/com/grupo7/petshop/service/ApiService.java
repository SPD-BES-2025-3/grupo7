package com.grupo7.petshop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.grupo7.petshop.model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class ApiService {
    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Cliente endpoints
    public static List<Cliente> getAllClientes() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/clientes"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(response.body(), 
                    typeFactory.constructCollectionType(List.class, Cliente.class));
        }
        throw new RuntimeException("Erro ao buscar clientes: " + response.statusCode());
    }

    public static Cliente getClienteById(String id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/clientes/" + id))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), Cliente.class);
        }
        return null;
    }

    public static Cliente createCliente(Cliente cliente) throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(cliente);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/clientes"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), Cliente.class);
        }
        throw new RuntimeException("Erro ao criar cliente: " + response.statusCode());
    }

    public static Cliente updateCliente(String id, Cliente cliente) throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(cliente);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/clientes/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), Cliente.class);
        }
        throw new RuntimeException("Erro ao atualizar cliente: " + response.statusCode());
    }

    public static void deleteCliente(String id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/clientes/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 204) {
            throw new RuntimeException("Erro ao excluir cliente: " + response.statusCode());
        }
    }

    public static List<Cliente> buscarClientesPorNome(String nome) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/clientes/busca?nome=" + nome))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(response.body(), 
                    typeFactory.constructCollectionType(List.class, Cliente.class));
        }
        throw new RuntimeException("Erro ao buscar clientes: " + response.statusCode());
    }

    // Produto endpoints
    public static List<Produto> getAllProdutos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/produtos"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(response.body(), 
                    typeFactory.constructCollectionType(List.class, Produto.class));
        }
        throw new RuntimeException("Erro ao buscar produtos: " + response.statusCode());
    }

    public static Produto createProduto(Produto produto) throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(produto);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/produtos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), Produto.class);
        }
        throw new RuntimeException("Erro ao criar produto: " + response.statusCode());
    }

    public static Produto updateProduto(String id, Produto produto) throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(produto);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/produtos/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), Produto.class);
        }
        throw new RuntimeException("Erro ao atualizar produto: " + response.statusCode());
    }

    public static void deleteProduto(String id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/produtos/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 204) {
            throw new RuntimeException("Erro ao excluir produto: " + response.statusCode());
        }
    }

    public static List<Produto> buscarProdutosPorNome(String nome) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/produtos/busca?nome=" + nome))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(response.body(), 
                    typeFactory.constructCollectionType(List.class, Produto.class));
        }
        throw new RuntimeException("Erro ao buscar produtos: " + response.statusCode());
    }

    // Pet endpoints
    public static List<Pet> getAllPets() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/pets"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(response.body(), 
                    typeFactory.constructCollectionType(List.class, Pet.class));
        }
        throw new RuntimeException("Erro ao buscar pets: " + response.statusCode());
    }

    public static Pet createPet(Pet pet) throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(pet);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/pets"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), Pet.class);
        }
        throw new RuntimeException("Erro ao criar pet: " + response.statusCode());
    }

    public static Pet updatePet(String id, Pet pet) throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(pet);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/pets/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), Pet.class);
        }
        throw new RuntimeException("Erro ao atualizar pet: " + response.statusCode());
    }

    public static void deletePet(String id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/pets/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 204) {
            throw new RuntimeException("Erro ao excluir pet: " + response.statusCode());
        }
    }

    public static List<Pet> buscarPetsPorNome(String nome) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/pets/busca?nome=" + nome))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(response.body(), 
                    typeFactory.constructCollectionType(List.class, Pet.class));
        }
        throw new RuntimeException("Erro ao buscar pets: " + response.statusCode());
    }

    // Agendamento endpoints
    public static List<Agendamento> getAllAgendamentos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/agendamentos"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(response.body(), 
                    typeFactory.constructCollectionType(List.class, Agendamento.class));
        }
        throw new RuntimeException("Erro ao buscar agendamentos: " + response.statusCode());
    }

    public static Agendamento createAgendamento(Agendamento agendamento) throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(agendamento);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/agendamentos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), Agendamento.class);
        }
        throw new RuntimeException("Erro ao criar agendamento: " + response.statusCode());
    }

    public static Agendamento updateAgendamento(String id, Agendamento agendamento) throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(agendamento);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/agendamentos/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), Agendamento.class);
        }
        throw new RuntimeException("Erro ao atualizar agendamento: " + response.statusCode());
    }

    public static void deleteAgendamento(String id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/agendamentos/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 204) {
            throw new RuntimeException("Erro ao excluir agendamento: " + response.statusCode());
        }
    }

    // Venda endpoints
    public static List<Venda> getAllVendas() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/vendas"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(response.body(), 
                    typeFactory.constructCollectionType(List.class, Venda.class));
        }
        throw new RuntimeException("Erro ao buscar vendas: " + response.statusCode());
    }

    public static Venda createVenda(Venda venda) throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(venda);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/vendas"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), Venda.class);
        }
        throw new RuntimeException("Erro ao criar venda: " + response.statusCode());
    }

    public static void deleteVenda(String id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/vendas/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 204) {
            throw new RuntimeException("Erro ao cancelar venda: " + response.statusCode());
        }
    }
}
