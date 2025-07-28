package com.grupo7.sqlite.service;

import com.grupo7.sqlite.event.IntegrationEvent;
import com.grupo7.sqlite.model.Cliente;
import com.grupo7.sqlite.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private EventPublisherService eventPublisherService;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Optional<Cliente> findByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    public List<Cliente> findByAtivo(Integer ativo) {
        return clienteRepository.findByAtivo(ativo);
    }

    public List<Cliente> findByNomeContaining(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Cliente> findByNomeOrEmailContaining(String termo) {
        return clienteRepository.findByNomeOrEmailContaining(termo);
    }

            public Cliente save(Cliente cliente) {
            // Definir timestamps
            if (cliente.getId() == null) {
                cliente.setCreatedAt(LocalDateTime.now());
            }
            cliente.setUpdatedAt(LocalDateTime.now());
            
            // Verificar se email já existe
            if (cliente.getId() == null) {
                if (clienteRepository.existsByEmail(cliente.getEmail())) {
                    throw new RuntimeException("Email já cadastrado");
                }
                if (clienteRepository.existsByCpf(cliente.getCpf())) {
                    throw new RuntimeException("CPF já cadastrado");
                }
            } else {
                Optional<Cliente> existingByEmail = clienteRepository.findByEmail(cliente.getEmail());
                if (existingByEmail.isPresent() && !existingByEmail.get().getId().equals(cliente.getId())) {
                    throw new RuntimeException("Email já cadastrado para outro cliente");
                }
                Optional<Cliente> existingByCpf = clienteRepository.findByCpf(cliente.getCpf());
                if (existingByCpf.isPresent() && !existingByCpf.get().getId().equals(cliente.getId())) {
                    throw new RuntimeException("CPF já cadastrado para outro cliente");
                }
            }

            Cliente savedCliente = clienteRepository.save(cliente);
        
        // Publicar evento de integração
        publishIntegrationEvent(savedCliente, cliente.getId() == null ? "CREATE" : "UPDATE");
        
        return savedCliente;
    }

    public Cliente update(Long id, Cliente cliente) {
        Optional<Cliente> existingCliente = clienteRepository.findById(id);
        if (existingCliente.isPresent()) {
            Cliente updatedCliente = existingCliente.get();
            updatedCliente.setNome(cliente.getNome());
            updatedCliente.setEmail(cliente.getEmail());
            updatedCliente.setTelefone(cliente.getTelefone());
            updatedCliente.setEndereco(cliente.getEndereco());
            updatedCliente.setCpf(cliente.getCpf());
            updatedCliente.setAtivo(cliente.getAtivo());
            
            Cliente savedCliente = clienteRepository.save(updatedCliente);
            
            // Publicar evento de integração
            publishIntegrationEvent(savedCliente, "UPDATE");
            
            return savedCliente;
        }
        throw new RuntimeException("Cliente não encontrado");
    }

    public void deleteById(Long id) {
        if (clienteRepository.existsById(id)) {
            Optional<Cliente> cliente = clienteRepository.findById(id);
            clienteRepository.deleteById(id);
            
            // Publicar evento de integração
            if (cliente.isPresent()) {
                publishIntegrationEvent(cliente.get(), "DELETE");
            }
        } else {
            throw new RuntimeException("Cliente não encontrado");
        }
    }

    public boolean existsById(Long id) {
        return clienteRepository.existsById(id);
    }

    public boolean existsByEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }

    public boolean existsByCpf(String cpf) {
        return clienteRepository.existsByCpf(cpf);
    }

    private void publishIntegrationEvent(Cliente cliente, String operation) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", cliente.getId().toString());
        data.put("nome", cliente.getNome());
        data.put("email", cliente.getEmail());
        data.put("telefone", cliente.getTelefone());
        data.put("endereco", cliente.getEndereco());
        data.put("cpf", cliente.getCpf());
        data.put("ativo", cliente.getAtivo());
        data.put("createdAt", cliente.getCreatedAt());
        data.put("updatedAt", cliente.getUpdatedAt());

        IntegrationEvent event = new IntegrationEvent("ORM", "CLIENTE", operation, cliente.getId().toString(), data);
        eventPublisherService.publishOrmEvent(event);
    }
} 