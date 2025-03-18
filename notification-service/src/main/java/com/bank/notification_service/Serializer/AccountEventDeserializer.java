package com.bank.notification_service.Serializer;

import com.bank.notification_service.AccountEvent;
import com.bank.notification_service.AuditAccountEvent;
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AccountEventDeserializer implements Deserializer<AccountEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AccountEvent deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, AccountEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing AccountEvent", e);
        }
    }

    @Override
    public void close() {
        // No resources to close in this example
    }
}
