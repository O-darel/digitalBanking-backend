package com.bank.audit_logging_service.Logs.Repository;

import com.bank.audit_logging_service.Logs.Entity.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
    List<AuditLog> findByDetails(String details);
}
