package com.bank.notification_service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditAccountEvent {
    private String eventType;
    private String sourceService;
    private String details;
}
