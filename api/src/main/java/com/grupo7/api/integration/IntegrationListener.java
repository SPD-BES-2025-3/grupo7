package com.grupo7.api.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import com.grupo7.api.event.IntegrationEvent;
import com.grupo7.api.integration.transformer.OdmToOrmTransformer;
import com.grupo7.api.integration.transformer.OrmToOdmTransformer;

@Component
public class IntegrationListener implements MessageListener {
    
    private static final Logger logger = LoggerFactory.getLogger(IntegrationListener.class);
    
    private final OrmToOdmTransformer ormToOdmTransformer;
    private final OdmToOrmTransformer odmToOrmTransformer;
    private RedisTemplate<String, Object> redisTemplate;
    
    public IntegrationListener(OrmToOdmTransformer ormToOdmTransformer, 
                             OdmToOrmTransformer odmToOrmTransformer) {
        this.ormToOdmTransformer = ormToOdmTransformer;
        this.odmToOrmTransformer = odmToOrmTransformer;
    }
    
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RedisSerializer<String> keySerializer = redisTemplate.getStringSerializer();
            RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) redisTemplate.getValueSerializer();
            
            String channel = keySerializer.deserialize(message.getChannel());
            IntegrationEvent event = (IntegrationEvent) valueSerializer.deserialize(message.getBody());
            
            logger.info("Evento de integração recebido: {} - Canal: {}", event, channel);
            
            if ("orm-events".equals(channel)) {
                handleOrmEvent(event);
            } else if ("odm-events".equals(channel)) {
                handleOdmEvent(event);
            }
            
        } catch (Exception e) {
            logger.error("Erro ao processar evento de integração: {}", e.getMessage(), e);
        }
    }
    
    public void handleOrmEvent(IntegrationEvent event) {
        logger.info("Processando evento ORM → ODM: {}", event);
        
        try {
            // Transformar dados do formato relacional para documento
            Object transformedData = ormToOdmTransformer.transform(event);
            
            // Sincronizar com MongoDB (ODM)
            ormToOdmTransformer.synchronizeToOdm(event.getEntityType(), event.getEntityId(), transformedData);
            
            logger.info("Sincronização ORM → ODM concluída para entidade: {}", event.getEntityId());
            
        } catch (Exception e) {
            logger.error("Erro na sincronização ORM → ODM: {}", e.getMessage(), e);
        }
    }
    
    public void handleOdmEvent(IntegrationEvent event) {
        logger.info("Processando evento ODM → ORM: {}", event);
        
        try {
            // Transformar dados do formato documento para relacional
            Object transformedData = odmToOrmTransformer.transform(event);
            
            // Sincronizar com SQLite (ORM)
            odmToOrmTransformer.synchronizeToOrm(event.getEntityType(), event.getEntityId(), transformedData);
            
            logger.info("Sincronização ODM → ORM concluída para entidade: {}", event.getEntityId());
            
        } catch (Exception e) {
            logger.error("Erro na sincronização ODM → ORM: {}", e.getMessage(), e);
        }
    }
} 