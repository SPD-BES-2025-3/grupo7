package com.grupo7.api.service;

import com.grupo7.api.event.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class EventPublisherService {
    
    private static final Logger logger = LoggerFactory.getLogger(EventPublisherService.class);
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    // Canais de eventos
    public static final String VENDA_CHANNEL = "venda-events";
    public static final String ESTOQUE_CHANNEL = "estoque-events";
    public static final String CLIENTE_CHANNEL = "cliente-events";
    public static final String PET_CHANNEL = "pet-events";
    public static final String AGENDAMENTO_CHANNEL = "agendamento-events";
    public static final String PRODUTO_CHANNEL = "produto-events";
    
    // Canais de integração
    public static final String ORM_CHANNEL = "orm-events";
    public static final String ODM_CHANNEL = "odm-events";
    
    public void publishEvent(String channel, BaseEvent event) {
        try {
            ChannelTopic topic = new ChannelTopic(channel);
            redisTemplate.convertAndSend(topic.getTopic(), event);
            logger.info("Evento publicado no canal {}: {}", channel, event.getEventType());
        } catch (Exception e) {
            logger.error("Erro ao publicar evento no canal {}: {}", channel, e.getMessage(), e);
        }
    }
    
    public void publishVendaEvent(BaseEvent event) {
        publishEvent(VENDA_CHANNEL, event);
    }
    
    public void publishEstoqueEvent(BaseEvent event) {
        publishEvent(ESTOQUE_CHANNEL, event);
    }
    
    public void publishClienteEvent(BaseEvent event) {
        publishEvent(CLIENTE_CHANNEL, event);
    }
    
    public void publishPetEvent(BaseEvent event) {
        publishEvent(PET_CHANNEL, event);
    }
    
    public void publishAgendamentoEvent(BaseEvent event) {
        publishEvent(AGENDAMENTO_CHANNEL, event);
    }
    
    public void publishProdutoEvent(BaseEvent event) {
        publishEvent(PRODUTO_CHANNEL, event);
    }
    
    public void publishOrmEvent(BaseEvent event) {
        publishEvent(ORM_CHANNEL, event);
    }
    
    public void publishOdmEvent(BaseEvent event) {
        publishEvent(ODM_CHANNEL, event);
    }
} 