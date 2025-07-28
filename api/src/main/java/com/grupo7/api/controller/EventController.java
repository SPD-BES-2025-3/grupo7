package com.grupo7.api.controller;

import com.grupo7.api.event.EstoqueEvent;
import com.grupo7.api.event.VendaEvent;
import com.grupo7.api.service.EventPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/events")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventPublisherService eventPublisherService;

    @PostMapping("/test/venda")
    public ResponseEntity<Map<String, String>> testVendaEvent(@RequestBody Map<String, String> request) {
        try {
            String action = request.get("action");
            String vendaId = request.get("vendaId");
            
            // Criar evento de teste
            VendaEvent event = new VendaEvent();
            event.setAction(action);
            
            eventPublisherService.publishVendaEvent(event);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Evento de venda publicado com sucesso");
            response.put("action", action);
            response.put("vendaId", vendaId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erro ao publicar evento: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/test/estoque")
    public ResponseEntity<Map<String, String>> testEstoqueEvent(@RequestBody Map<String, Object> request) {
        try {
            String produtoId = (String) request.get("produtoId");
            String produtoNome = (String) request.get("produtoNome");
            Integer quantidadeAnterior = (Integer) request.get("quantidadeAnterior");
            Integer quantidadeAtual = (Integer) request.get("quantidadeAtual");
            String motivo = (String) request.get("motivo");
            
            EstoqueEvent event = new EstoqueEvent(
                produtoId, 
                produtoNome, 
                quantidadeAnterior, 
                quantidadeAtual, 
                motivo
            );
            
            eventPublisherService.publishEstoqueEvent(event);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Evento de estoque publicado com sucesso");
            response.put("produtoId", produtoId);
            response.put("motivo", motivo);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erro ao publicar evento: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getEventStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "Event system is running");
        status.put("channels", new String[]{
            EventPublisherService.VENDA_CHANNEL,
            EventPublisherService.ESTOQUE_CHANNEL,
            EventPublisherService.CLIENTE_CHANNEL,
            EventPublisherService.PET_CHANNEL,
            EventPublisherService.AGENDAMENTO_CHANNEL,
            EventPublisherService.PRODUTO_CHANNEL
        });
        status.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(status);
    }
} 