package com.bank.audit_logging_service.Logs.Events;

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
    private String userEmail;
}
