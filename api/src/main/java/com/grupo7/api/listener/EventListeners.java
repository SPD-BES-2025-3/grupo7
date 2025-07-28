package com.grupo7.api.listener;

import com.grupo7.api.event.BaseEvent;
import com.grupo7.api.event.EstoqueEvent;
import com.grupo7.api.event.VendaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class EventListeners {
    
    private static final Logger logger = LoggerFactory.getLogger(EventListeners.class);
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Component
    public class VendaEventListener implements MessageListener {
        
        @Override
        public void onMessage(Message message, byte[] pattern) {
            try {
                RedisSerializer<String> keySerializer = redisTemplate.getStringSerializer();
                RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) redisTemplate.getValueSerializer();
                
                String channel = keySerializer.deserialize(message.getChannel());
                VendaEvent event = (VendaEvent) valueSerializer.deserialize(message.getBody());
                
                logger.info("Evento de venda recebido: {} - Ação: {}", event.getEventType(), event.getAction());
                
                // Processar evento de venda
                processVendaEvent(event);
                
            } catch (Exception e) {
                logger.error("Erro ao processar evento de venda: {}", e.getMessage(), e);
            }
        }
        
        private void processVendaEvent(VendaEvent event) {
            switch (event.getAction()) {
                case "CRIADA":
                    logger.info("Nova venda criada: {}", event.getVenda().getId());
                    // Implementar lógica específica para venda criada
                    break;
                case "FINALIZADA":
                    logger.info("Venda finalizada: {}", event.getVenda().getId());
                    // Implementar lógica específica para venda finalizada
                    break;
                case "CANCELADA":
                    logger.info("Venda cancelada: {}", event.getVenda().getId());
                    // Implementar lógica específica para venda cancelada
                    break;
                default:
                    logger.warn("Ação de venda não reconhecida: {}", event.getAction());
            }
        }
    }
    
    @Component
    public class EstoqueEventListener implements MessageListener {
        
        @Override
        public void onMessage(Message message, byte[] pattern) {
            try {
                RedisSerializer<String> keySerializer = redisTemplate.getStringSerializer();
                RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) redisTemplate.getValueSerializer();
                
                String channel = keySerializer.deserialize(message.getChannel());
                EstoqueEvent event = (EstoqueEvent) valueSerializer.deserialize(message.getBody());
                
                logger.info("Evento de estoque recebido: {} - Produto: {}", event.getEventType(), event.getProdutoNome());
                
                // Processar evento de estoque
                processEstoqueEvent(event);
                
            } catch (Exception e) {
                logger.error("Erro ao processar evento de estoque: {}", e.getMessage(), e);
            }
        }
        
        private void processEstoqueEvent(EstoqueEvent event) {
            logger.info("Estoque atualizado - Produto: {}, Quantidade anterior: {}, Quantidade atual: {}, Motivo: {}", 
                event.getProdutoNome(), event.getQuantidadeAnterior(), event.getQuantidadeAtual(), event.getMotivo());
            
            // Verificar se estoque está baixo
            if (event.getQuantidadeAtual() <= 5) {
                logger.warn("ALERTA: Estoque baixo para o produto {} - Quantidade: {}", 
                    event.getProdutoNome(), event.getQuantidadeAtual());
            }
        }
    }
} 