package com.bank.audit_logging_service.Logs.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "audit_logs")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class AuditLog {
    @Id
    private String id;
    private String eventType;
    private String details;
    private String email;
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
