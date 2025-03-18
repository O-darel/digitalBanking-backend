package com.bank.audit_logging_service.Logs.Controller;

import com.bank.audit_logging_service.Logs.Entity.AuditLog;
import com.bank.audit_logging_service.Logs.Service.AuditLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public List<AuditLog> getAllAuditLogs() {
        return auditLogService.getAllAuditLogs();
    }

    @GetMapping("/eventDetail/{details}")
    public List<AuditLog> getAuditLogsByEventType(@PathVariable String details) {
        return auditLogService.getAuditLogsByEventType(details);
    }
}
