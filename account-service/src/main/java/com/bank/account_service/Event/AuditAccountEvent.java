package com.bank.account_service.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditAccountEvent {
    private String eventType;
    private String sourceService;
    private String details;
    private String userEmail;
}
