package com.bank.account_service.Serializer;

import org.apache.kafka.common.serialization.Serializer;
import com.bank.account_service.Event.AccountEvent;
import com.bank.account_service.Event.AuditAccountEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MultiEventSerializer implements Serializer<Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Object data) {
        try {
            if (data instanceof AccountEvent) {
                return objectMapper.writeValueAsBytes((AccountEvent) data);
            } else if (data instanceof AuditAccountEvent) {
                return objectMapper.writeValueAsBytes((AuditAccountEvent) data);
            } else {
                throw new IllegalArgumentException("Unknown event type for serialization: " + data.getClass());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error serializing event", e);
        }
    }

    @Override
    public void close() {
        // No resources to clean up in this example
    }
}
