package com.grupo7.api.integration.transformer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.grupo7.api.event.IntegrationEvent;
import com.grupo7.api.model.Cliente;
import com.grupo7.api.model.Produto;
import com.grupo7.api.model.Venda;
import com.grupo7.api.repository.ClienteRepository;
import com.grupo7.api.repository.ProdutoRepository;
import com.grupo7.api.repository.VendaRepository;

@Component
public class OrmToOdmTransformer {
    
    private static final Logger logger = LoggerFactory.getLogger(OrmToOdmTransformer.class);
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private VendaRepository vendaRepository;
    
    public Object transform(IntegrationEvent event) {
        logger.info("Transformando dados ORM → ODM para entidade: {}", event.getEntityType());
        
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
    
    private Cliente transformCliente(IntegrationEvent event) {
        Map<String, Object> data = event.getData();
        
        Cliente cliente = new Cliente();
        cliente.setId((String) data.get("id"));
        cliente.setNome((String) data.get("nome"));
        cliente.setEmail((String) data.get("email"));
        cliente.setTelefone((String) data.get("telefone"));
        cliente.setEndereco((String) data.get("endereco"));
        cliente.setCpf((String) data.get("cpf"));
        
        // Converter campos específicos do ORM para ODM
        if (data.get("ativo") instanceof Boolean) {
            cliente.setAtivo((Boolean) data.get("ativo"));
        } else if (data.get("ativo") instanceof Integer) {
            cliente.setAtivo(((Integer) data.get("ativo")) == 1);
        }
        
        return cliente;
    }
    
    private Produto transformProduto(IntegrationEvent event) {
        Map<String, Object> data = event.getData();
        
        Produto produto = new Produto();
        produto.setId((String) data.get("id"));
        produto.setNome((String) data.get("nome"));
        produto.setDescricao((String) data.get("descricao"));
        produto.setCategoria((String) data.get("categoria"));
        produto.setCodigo((String) data.get("codigo"));
        produto.setMarca((String) data.get("marca"));
        produto.setTamanho((String) data.get("tamanho"));
        
        // Converter tipos numéricos
        if (data.get("preco") instanceof Number) {
            produto.setPreco(new java.math.BigDecimal(data.get("preco").toString()));
        }
        
        if (data.get("estoque") instanceof Number) {
            produto.setEstoque(((Number) data.get("estoque")).intValue());
        }
        
        if (data.get("peso") instanceof String) {
            produto.setPeso((String) data.get("peso"));
        } else if (data.get("peso") instanceof Number) {
            produto.setPeso(data.get("peso").toString());
        }
        
        // Converter boolean
        if (data.get("ativo") instanceof Boolean) {
            produto.setAtivo((Boolean) data.get("ativo"));
        } else if (data.get("ativo") instanceof Integer) {
            produto.setAtivo(((Integer) data.get("ativo")) == 1);
        }
        
        return produto;
    }
    
    private Venda transformVenda(IntegrationEvent event) {
        Map<String, Object> data = event.getData();
        
        Venda venda = new Venda();
        venda.setId((String) data.get("id"));
        venda.setClienteId((String) data.get("clienteId"));
        venda.setStatus((String) data.get("status"));
        venda.setFormaPagamento((String) data.get("formaPagamento"));
        venda.setObservacoes((String) data.get("observacoes"));
        
        // Converter tipos numéricos
        if (data.get("total") instanceof Number) {
            venda.setTotal(new java.math.BigDecimal(data.get("total").toString()));
        }
        
        // Converter datas
        if (data.get("dataPagamento") instanceof String) {
            venda.setDataPagamento(java.time.LocalDateTime.parse((String) data.get("dataPagamento")));
        }
        
        // Transformar itens da venda se existirem
        if (data.get("itens") instanceof java.util.List) {
            // Implementar transformação dos itens
            logger.info("Transformando itens da venda");
        }
        
        return venda;
    }
    
    public void synchronizeToOdm(String entityType, String entityId, Object transformedData) {
        logger.info("Sincronizando {} com ID {} para ODM", entityType, entityId);
        
        try {
            switch (entityType.toUpperCase()) {
                case "CLIENTE":
                    Cliente cliente = (Cliente) transformedData;
                    if ("DELETE".equals(getOperationFromEvent(entityId))) {
                        clienteRepository.deleteById(entityId);
                    } else {
                        clienteRepository.save(cliente);
                    }
                    break;
                    
                case "PRODUTO":
                    Produto produto = (Produto) transformedData;
                    if ("DELETE".equals(getOperationFromEvent(entityId))) {
                        produtoRepository.deleteById(entityId);
                    } else {
                        produtoRepository.save(produto);
                    }
                    break;
                    
                case "VENDA":
                    Venda venda = (Venda) transformedData;
                    if ("DELETE".equals(getOperationFromEvent(entityId))) {
                        vendaRepository.deleteById(entityId);
                    } else {
                        vendaRepository.save(venda);
                    }
                    break;
                    
                default:
                    logger.warn("Tipo de entidade não suportado para sincronização: {}", entityType);
            }
            
            logger.info("Sincronização para ODM concluída: {} - {}", entityType, entityId);
            
        } catch (Exception e) {
            logger.error("Erro na sincronização para ODM: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    private String getOperationFromEvent(String entityId) {
        // Implementar lógica para determinar a operação baseada no evento
        // Por simplicidade, retornamos "UPDATE" como padrão
        return "UPDATE";
    }
} 