package com.grupo7.api.controller;

import com.grupo7.api.model.Cliente;
import com.grupo7.api.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() {
        List<Cliente> clientes = clienteService.findAll();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable String id) {
        Optional<Cliente> cliente = clienteService.findById(id);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Cliente> getClienteByEmail(@PathVariable String email) {
        Optional<Cliente> cliente = clienteService.findByEmail(email);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Cliente> getClienteByCpf(@PathVariable String cpf) {
        Optional<Cliente> cliente = clienteService.findByCpf(cpf);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Cliente>> getClientesAtivos() {
        List<Cliente> clientes = clienteService.findClientesAtivos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/busca")
    public ResponseEntity<List<Cliente>> buscarPorNome(@RequestParam String nome) {
        List<Cliente> clientes = clienteService.findByNomeContaining(nome);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/busca/email")
    public ResponseEntity<List<Cliente>> buscarPorEmail(@RequestParam String email) {
        List<Cliente> clientes = clienteService.findByEmailContaining(email);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/busca/telefone")
    public ResponseEntity<List<Cliente>> buscarPorTelefone(@RequestParam String telefone) {
        List<Cliente> clientes = clienteService.findByTelefoneContaining(telefone);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/busca/geral")
    public ResponseEntity<List<Cliente>> buscarGeral(@RequestParam String termo) {
        List<Cliente> clientes = clienteService.buscarPorNomeOuEmail(termo);
        return ResponseEntity.ok(clientes);
    }

    @PostMapping
    public ResponseEntity<Cliente> createCliente(@Valid @RequestBody Cliente cliente) {
        try {
            Cliente savedCliente = clienteService.save(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCliente);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable String id, @Valid @RequestBody Cliente cliente) {
        try {
            Cliente updatedCliente = clienteService.update(id, cliente);
            return ResponseEntity.ok(updatedCliente);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable String id) {
        if (clienteService.existsById(id)) {
            clienteService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Cliente> alterarStatusCliente(@PathVariable String id, @RequestParam boolean ativo) {
        Optional<Cliente> cliente = clienteService.findById(id);
        if (cliente.isPresent()) {
            Cliente c = cliente.get();
            c.setAtivo(ativo);
            Cliente updatedCliente = clienteService.save(c);
            return ResponseEntity.ok(updatedCliente);
        }
        return ResponseEntity.notFound().build();
    }
} 