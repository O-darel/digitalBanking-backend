package com.bank.audit_logging_service.serializer;

import com.bank.audit_logging_service.Logs.Events.AuditAccountEvent;
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;

import java.io.IOException;

public class AccountEventDeserializer implements Deserializer<AuditAccountEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AuditAccountEvent deserialize(String topic, byte[] data) {
        if (data == null) {
            throw new SerializationException("Error deserializing: Data is null");
        }

        try {
            return objectMapper.readValue(data, AuditAccountEvent.class);
        } catch (IOException e) {
            throw new SerializationException("Error deserializing message from topic: " + topic, e);
        }
    }

    @Override
    public void close() {
    }
}
