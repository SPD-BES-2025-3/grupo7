package com.grupo7.api.event;

import java.util.Map;

public class IntegrationEvent extends BaseEvent {
    private String sourceSystem; // "ORM" ou "ODM"
    private String entityType;   // "Venda", "Cliente", "Produto", etc.
    private String operation;    // "CREATE", "UPDATE", "DELETE"
    private String entityId;
    private Map<String, Object> data;
    private String targetSystem; // Sistema de destino para sincronização

    public IntegrationEvent() {
        super();
    }

    public IntegrationEvent(String sourceSystem, String entityType, String operation, 
                          String entityId, Map<String, Object> data) {
        super();
        this.sourceSystem = sourceSystem;
        this.entityType = entityType;
        this.operation = operation;
        this.entityId = entityId;
        this.data = data;
        this.targetSystem = sourceSystem.equals("ORM") ? "ODM" : "ORM";
    }

    // Getters e Setters
    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getTargetSystem() {
        return targetSystem;
    }

    public void setTargetSystem(String targetSystem) {
        this.targetSystem = targetSystem;
    }

    @Override
    public String toString() {
        return String.format("IntegrationEvent{sourceSystem='%s', entityType='%s', operation='%s', entityId='%s'}", 
                           sourceSystem, entityType, operation, entityId);
    }
} 