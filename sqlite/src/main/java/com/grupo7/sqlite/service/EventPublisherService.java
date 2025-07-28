package com.grupo7.sqlite.service;

import com.grupo7.sqlite.event.IntegrationEvent;
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
    public static final String ORM_CHANNEL = "orm-events";
    public static final String ODM_CHANNEL = "odm-events";
    
    public void publishEvent(String channel, IntegrationEvent event) {
        try {
            ChannelTopic topic = new ChannelTopic(channel);
            redisTemplate.convertAndSend(topic.getTopic(), event);
            logger.info("Evento publicado no canal {}: {}", channel, event.getEventType());
        } catch (Exception e) {
            logger.error("Erro ao publicar evento no canal {}: {}", channel, e.getMessage(), e);
        }
    }
    
    public void publishOrmEvent(IntegrationEvent event) {
        publishEvent(ORM_CHANNEL, event);
    }
    
    public void publishOdmEvent(IntegrationEvent event) {
        publishEvent(ODM_CHANNEL, event);
    }
} 