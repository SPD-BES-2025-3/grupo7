package com.grupo7.api.event;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BaseEvent {
    private String id;
    private String eventType;
    private LocalDateTime timestamp;
    private String source;

    public BaseEvent() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.eventType = this.getClass().getSimpleName();
        this.source = "grupo7-api";
    }

    public BaseEvent(String eventType) {
        this();
        this.eventType = eventType;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
} 