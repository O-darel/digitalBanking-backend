package com.bank.audit_logging_service.Logs.Service;

import com.bank.audit_logging_service.Logs.Entity.AuditLog;
import com.bank.audit_logging_service.Logs.Events.AuditAccountEvent;
import com.bank.audit_logging_service.Logs.Events.TransactionEvent;
import com.bank.audit_logging_service.Logs.Repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {
    @Autowired
    private KafkaTemplate<String, AuditAccountEvent> kafkaTemplate;
    @Autowired
    private AuditLogRepository auditLogRepository;

    @KafkaListener(topics = "account-events")
    public void listenAccountEvents(AuditAccountEvent auditLogEvent) {
        AuditLog log = new AuditLog();
        System.out.println(log);
        log.setDetails(auditLogEvent.getDetails());
        log.setEventType(auditLogEvent.getEventType());
        log.setEmail(auditLogEvent.getUserEmail());
        auditLogRepository.save(log);
    }

    @KafkaListener(topics = "transaction-events")
    public void listenTransactionEvents(TransactionEvent transactionEvent) {
        // Process user events
        AuditLog log = new AuditLog();
        log.setDetails(transactionEvent.getDetails());
        log.setEventType(transactionEvent.getEventType());
        log.setEmail(transactionEvent.getUserEmail());
        auditLogRepository.save(log);
    }


    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    public List<AuditLog> getAuditLogsByEventType(String details) {
        return auditLogRepository.findByDetails(details);
    }
}