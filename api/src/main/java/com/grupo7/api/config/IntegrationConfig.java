package com.grupo7.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.grupo7.api.integration.IntegrationListener;
import com.grupo7.api.integration.transformer.OrmToOdmTransformer;
import com.grupo7.api.integration.transformer.OdmToOrmTransformer;

@Configuration
public class IntegrationConfig {

    @Bean
    public IntegrationListener integrationListener(
            OrmToOdmTransformer ormToOdmTransformer,
            OdmToOrmTransformer odmToOrmTransformer) {
        return new IntegrationListener(ormToOdmTransformer, odmToOrmTransformer);
    }

    @Bean
    public OrmToOdmTransformer ormToOdmTransformer() {
        return new OrmToOdmTransformer();
    }

    @Bean
    public OdmToOrmTransformer odmToOrmTransformer() {
        return new OdmToOrmTransformer();
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            IntegrationListener integrationListener) {
        
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        
        // Listener para eventos do Sistema ORM (SQLite)
        container.addMessageListener(
            new MessageListenerAdapter(integrationListener, "handleOrmEvent"),
            new org.springframework.data.redis.listener.ChannelTopic("orm-events")
        );
        
        // Listener para eventos do Sistema ODM (MongoDB)
        container.addMessageListener(
            new MessageListenerAdapter(integrationListener, "handleOdmEvent"),
            new org.springframework.data.redis.listener.ChannelTopic("odm-events")
        );
        
        return container;
    }
} 