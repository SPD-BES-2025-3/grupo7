package com.grupo7.api.integration.transformer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.grupo7.api.event.IntegrationEvent;
import com.grupo7.api.model.Cliente;
import com.grupo7.api.model.Produto;
import com.grupo7.api.model.Venda;

@Component
public class OdmToOrmTransformer {
    
    private static final Logger logger = LoggerFactory.getLogger(OdmToOrmTransformer.class);
    
    public Object transform(IntegrationEvent event) {
        logger.info("Transformando dados ODM → ORM para entidade: {}", event.getEntityType());
        
        switch (event.getEntityType().toUpperCase()) {
            case "CLIENTE":
                return transformCliente(event);
            case "PRODUTO":
                return transformProduto(event);
            case "VENDA":
                return transformVenda(event);
            default:
                logger.warn("Tipo de entidade não suportado: {}", event.getEntityType());
                return event.getData();
        }
    }
    
    private Map<String, Object> transformCliente(IntegrationEvent event) {
        Map<String, Object> data = event.getData();
        Map<String, Object> ormData = new HashMap<>();
        
        // Converter dados do formato documento para relacional
        ormData.put("id", data.get("id"));
        ormData.put("nome", data.get("nome"));
        ormData.put("email", data.get("email"));
        ormData.put("telefone", data.get("telefone"));
        ormData.put("endereco", data.get("endereco"));
        ormData.put("cpf", data.get("cpf"));
        
        // Converter boolean para integer (padrão SQLite)
        if (data.get("ativo") instanceof Boolean) {
            ormData.put("ativo", ((Boolean) data.get("ativo")) ? 1 : 0);
        } else {
            ormData.put("ativo", data.get("ativo"));
        }
        
        // Adicionar campos específicos do ORM se necessário
        ormData.put("created_at", data.get("createdAt"));
        ormData.put("updated_at", data.get("updatedAt"));
        
        return ormData;
    }
    
    private Map<String, Object> transformProduto(IntegrationEvent event) {
        Map<String, Object> data = event.getData();
        Map<String, Object> ormData = new HashMap<>();
        
        // Converter dados do formato documento para relacional
        ormData.put("id", data.get("id"));
        ormData.put("nome", data.get("nome"));
        ormData.put("descricao", data.get("descricao"));
        ormData.put("categoria", data.get("categoria"));
        ormData.put("codigo", data.get("codigo"));
        ormData.put("marca", data.get("marca"));
        ormData.put("tamanho", data.get("tamanho"));
        
        // Converter tipos numéricos
        if (data.get("preco") instanceof java.math.BigDecimal) {
            ormData.put("preco", ((java.math.BigDecimal) data.get("preco")).doubleValue());
        } else {
            ormData.put("preco", data.get("preco"));
        }
        
        if (data.get("estoque") instanceof Integer) {
            ormData.put("estoque", data.get("estoque"));
        } else if (data.get("estoque") instanceof Number) {
            ormData.put("estoque", ((Number) data.get("estoque")).intValue());
        }
        
        if (data.get("peso") instanceof String) {
            ormData.put("peso", data.get("peso"));
        } else if (data.get("peso") instanceof Number) {
            ormData.put("peso", data.get("peso").toString());
        }
        
        // Converter boolean para integer
        if (data.get("ativo") instanceof Boolean) {
            ormData.put("ativo", ((Boolean) data.get("ativo")) ? 1 : 0);
        } else {
            ormData.put("ativo", data.get("ativo"));
        }
        
        // Adicionar campos específicos do ORM
        ormData.put("created_at", data.get("createdAt"));
        ormData.put("updated_at", data.get("updatedAt"));
        
        return ormData;
    }
    
    private Map<String, Object> transformVenda(IntegrationEvent event) {
        Map<String, Object> data = event.getData();
        Map<String, Object> ormData = new HashMap<>();
        
        // Converter dados do formato documento para relacional
        ormData.put("id", data.get("id"));
        ormData.put("cliente_id", data.get("clienteId")); // snake_case para ORM
        ormData.put("status", data.get("status"));
        ormData.put("forma_pagamento", data.get("formaPagamento"));
        ormData.put("observacoes", data.get("observacoes"));
        
        // Converter tipos numéricos
        if (data.get("total") instanceof java.math.BigDecimal) {
            ormData.put("total", ((java.math.BigDecimal) data.get("total")).doubleValue());
        } else {
            ormData.put("total", data.get("total"));
        }
        
        // Converter datas
        if (data.get("dataPagamento") instanceof java.time.LocalDateTime) {
            ormData.put("data_pagamento", data.get("dataPagamento").toString());
        } else {
            ormData.put("data_pagamento", data.get("dataPagamento"));
        }
        
        // Transformar itens da venda se existirem
        if (data.get("itens") instanceof java.util.List) {
            // Implementar transformação dos itens para formato relacional
            logger.info("Transformando itens da venda para formato relacional");
            ormData.put("itens", transformVendaItens((java.util.List<?>) data.get("itens")));
        }
        
        // Adicionar campos específicos do ORM
        ormData.put("created_at", data.get("createdAt"));
        ormData.put("updated_at", data.get("updatedAt"));
        
        return ormData;
    }
    
    private java.util.List<Map<String, Object>> transformVendaItens(java.util.List<?> itens) {
        java.util.List<Map<String, Object>> ormItens = new java.util.ArrayList<>();
        
        for (Object item : itens) {
            if (item instanceof Map) {
                Map<String, Object> itemMap = (Map<String, Object>) item;
                Map<String, Object> ormItem = new HashMap<>();
                
                ormItem.put("produto_id", itemMap.get("produtoId"));
                ormItem.put("quantidade", itemMap.get("quantidade"));
                
                if (itemMap.get("precoUnitario") instanceof java.math.BigDecimal) {
                    ormItem.put("preco_unitario", ((java.math.BigDecimal) itemMap.get("precoUnitario")).doubleValue());
                } else {
                    ormItem.put("preco_unitario", itemMap.get("precoUnitario"));
                }
                
                if (itemMap.get("subtotal") instanceof java.math.BigDecimal) {
                    ormItem.put("subtotal", ((java.math.BigDecimal) itemMap.get("subtotal")).doubleValue());
                } else {
                    ormItem.put("subtotal", itemMap.get("subtotal"));
                }
                
                ormItens.add(ormItem);
            }
        }
        
        return ormItens;
    }
    
    public void synchronizeToOrm(String entityType, String entityId, Object transformedData) {
        logger.info("Sincronizando {} com ID {} para ORM", entityType, entityId);
        
        try {
            // Aqui você implementaria a sincronização com o banco SQLite
            // Por exemplo, usando JDBC ou um ORM como Hibernate
            
            Map<String, Object> data = (Map<String, Object>) transformedData;
            
            switch (entityType.toUpperCase()) {
                case "CLIENTE":
                    // Executar SQL para sincronizar cliente
                    executeOrmSync("clientes", entityId, data, getOperationFromEvent(entityId));
                    break;
                    
                case "PRODUTO":
                    // Executar SQL para sincronizar produto
                    executeOrmSync("produtos", entityId, data, getOperationFromEvent(entityId));
                    break;
                    
                case "VENDA":
                    // Executar SQL para sincronizar venda
                    executeOrmSync("vendas", entityId, data, getOperationFromEvent(entityId));
                    break;
                    
                default:
                    logger.warn("Tipo de entidade não suportado para sincronização: {}", entityType);
            }
            
            logger.info("Sincronização para ORM concluída: {} - {}", entityType, entityId);
            
        } catch (Exception e) {
            logger.error("Erro na sincronização para ORM: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    private void executeOrmSync(String table, String entityId, Map<String, Object> data, String operation) {
        // Implementar a lógica de sincronização com SQLite
        // Por exemplo:
        logger.info("Executando sincronização {} na tabela {} com ID {}", operation, table, entityId);
        
        // Aqui você usaria JDBC ou um ORM para executar as operações SQL
        // INSERT, UPDATE ou DELETE baseado na operação
        
        switch (operation.toUpperCase()) {
            case "CREATE":
                logger.info("INSERT INTO {} VALUES ({})", table, data);
                break;
            case "UPDATE":
                logger.info("UPDATE {} SET {} WHERE id = {}", table, data, entityId);
                break;
            case "DELETE":
                logger.info("DELETE FROM {} WHERE id = {}", table, entityId);
                break;
        }
    }
    
    private String getOperationFromEvent(String entityId) {
        // Implementar lógica para determinar a operação baseada no evento
        // Por simplicidade, retornamos "UPDATE" como padrão
        return "UPDATE";
    }
} 