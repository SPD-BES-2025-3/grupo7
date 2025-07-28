package com.grupo7.api.controller;

import com.grupo7.api.event.IntegrationEvent;
import com.grupo7.api.service.EventPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/integration")
@CrossOrigin(origins = "*")
public class IntegrationController {

    @Autowired
    private EventPublisherService eventPublisherService;

    @PostMapping("/orm-to-odm")
    public ResponseEntity<Map<String, String>> testOrmToOdmIntegration(@RequestBody Map<String, Object> request) {
        try {
            String entityType = (String) request.get("entityType");
            String entityId = (String) request.get("entityId");
            String operation = (String) request.get("operation");
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) request.get("data");
            
            IntegrationEvent event = new IntegrationEvent("ORM", entityType, operation, entityId, data);
            eventPublisherService.publishOrmEvent(event);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Evento ORM → ODM publicado com sucesso");
            response.put("entityType", entityType);
            response.put("entityId", entityId);
            response.put("operation", operation);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erro ao publicar evento ORM → ODM: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/odm-to-orm")
    public ResponseEntity<Map<String, String>> testOdmToOrmIntegration(@RequestBody Map<String, Object> request) {
        try {
            String entityType = (String) request.get("entityType");
            String entityId = (String) request.get("entityId");
            String operation = (String) request.get("operation");
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) request.get("data");
            
            IntegrationEvent event = new IntegrationEvent("ODM", entityType, operation, entityId, data);
            eventPublisherService.publishOdmEvent(event);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Evento ODM → ORM publicado com sucesso");
            response.put("entityType", entityType);
            response.put("entityId", entityId);
            response.put("operation", operation);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erro ao publicar evento ODM → ORM: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getIntegrationStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "Integration system is running");
        status.put("channels", new String[]{
            EventPublisherService.ORM_CHANNEL,
            EventPublisherService.ODM_CHANNEL
        });
        status.put("supportedEntities", new String[]{
            "CLIENTE", "PRODUTO", "VENDA", "PET", "AGENDAMENTO"
        });
        status.put("supportedOperations", new String[]{
            "CREATE", "UPDATE", "DELETE"
        });
        status.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(status);
    }

    @PostMapping("/test/cliente")
    public ResponseEntity<Map<String, String>> testClienteIntegration() {
        try {
            // Simular evento de cliente do ORM para ODM
            Map<String, Object> clienteData = new HashMap<>();
            clienteData.put("id", "cliente-123");
            clienteData.put("nome", "João Silva");
            clienteData.put("email", "joao@email.com");
            clienteData.put("telefone", "(11) 99999-9999");
            clienteData.put("endereco", "Rua das Flores, 123");
            clienteData.put("cpf", "123.456.789-00");
            clienteData.put("ativo", 1); // Integer para ORM
            
            IntegrationEvent event = new IntegrationEvent("ORM", "CLIENTE", "CREATE", "cliente-123", clienteData);
            eventPublisherService.publishOrmEvent(event);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Teste de integração de cliente executado");
            response.put("direction", "ORM → ODM");
            response.put("entityId", "cliente-123");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erro no teste de integração: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/test/produto")
    public ResponseEntity<Map<String, String>> testProdutoIntegration() {
        try {
            // Simular evento de produto do ODM para ORM
            Map<String, Object> produtoData = new HashMap<>();
            produtoData.put("id", "produto-456");
            produtoData.put("nome", "Ração Premium");
            produtoData.put("descricao", "Ração de alta qualidade para cães");
            produtoData.put("categoria", "Alimentação");
            produtoData.put("codigo", "RACAO001");
            produtoData.put("marca", "PetFood");
            produtoData.put("tamanho", "5kg");
            produtoData.put("preco", 45.90);
            produtoData.put("estoque", 100);
            produtoData.put("peso", 5.0);
            produtoData.put("ativo", true); // Boolean para ODM
            
            IntegrationEvent event = new IntegrationEvent("ODM", "PRODUTO", "CREATE", "produto-456", produtoData);
            eventPublisherService.publishOdmEvent(event);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Teste de integração de produto executado");
            response.put("direction", "ODM → ORM");
            response.put("entityId", "produto-456");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erro no teste de integração: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 