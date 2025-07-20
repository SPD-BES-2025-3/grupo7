package com.grupo7.api.service;

import com.grupo7.api.model.Cliente;
import com.grupo7.api.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(String id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Optional<Cliente> findByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    public List<Cliente> findByAtivo(boolean ativo) {
        return clienteRepository.findByAtivo(ativo);
    }

    public List<Cliente> findByNomeContaining(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Cliente> findByEmailContaining(String email) {
        return clienteRepository.findByEmailContainingIgnoreCase(email);
    }

    public List<Cliente> findByTelefoneContaining(String telefone) {
        return clienteRepository.findByTelefoneContaining(telefone);
    }

    public Cliente save(Cliente cliente) {
        if (cliente.getId() != null) {
            // Verificar se email já existe para outro cliente
            Optional<Cliente> existingByEmail = clienteRepository.findByEmail(cliente.getEmail());
            if (existingByEmail.isPresent() && !existingByEmail.get().getId().equals(cliente.getId())) {
                throw new RuntimeException("Email já cadastrado para outro cliente");
            }
            
            // Verificar se CPF já existe para outro cliente
            Optional<Cliente> existingByCpf = clienteRepository.findByCpf(cliente.getCpf());
            if (existingByCpf.isPresent() && !existingByCpf.get().getId().equals(cliente.getId())) {
                throw new RuntimeException("CPF já cadastrado para outro cliente");
            }
        } else {
            // Novo cliente - verificar se email ou CPF já existem
            if (clienteRepository.existsByEmail(cliente.getEmail())) {
                throw new RuntimeException("Email já cadastrado");
            }
            if (clienteRepository.existsByCpf(cliente.getCpf())) {
                throw new RuntimeException("CPF já cadastrado");
            }
        }
        return clienteRepository.save(cliente);
    }

    public Cliente update(String id, Cliente cliente) {
        Optional<Cliente> existingCliente = clienteRepository.findById(id);
        if (existingCliente.isPresent()) {
            Cliente updatedCliente = existingCliente.get();
            updatedCliente.setNome(cliente.getNome());
            updatedCliente.setEmail(cliente.getEmail());
            updatedCliente.setTelefone(cliente.getTelefone());
            updatedCliente.setCpf(cliente.getCpf());
            updatedCliente.setEndereco(cliente.getEndereco());
            updatedCliente.setObservacoes(cliente.getObservacoes());
            updatedCliente.setAtivo(cliente.isAtivo());
            updatedCliente.setPetsIds(cliente.getPetsIds());
            return clienteRepository.save(updatedCliente);
        }
        throw new RuntimeException("Cliente não encontrado");
    }

    public void deleteById(String id) {
        clienteRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        return clienteRepository.existsById(id);
    }

    public boolean existsByEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }

    public boolean existsByCpf(String cpf) {
        return clienteRepository.existsByCpf(cpf);
    }

    // Métodos específicos para petshop
    public List<Cliente> findClientesAtivos() {
        return clienteRepository.findByAtivo(true);
    }

    public List<Cliente> buscarPorNomeOuEmail(String termo) {
        List<Cliente> porNome = clienteRepository.findByNomeContainingIgnoreCase(termo);
        List<Cliente> porEmail = clienteRepository.findByEmailContainingIgnoreCase(termo);
        
        // Combinar resultados removendo duplicatas
        porNome.addAll(porEmail.stream()
                .filter(cliente -> porNome.stream()
                        .noneMatch(existing -> existing.getId().equals(cliente.getId())))
                .toList());
        
        return porNome;
    }
} 