package com.landstack.backend.controller;

import com.landstack.backend.dto.AuditLogDto;
import com.landstack.backend.service.AuditLogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public List<AuditLogDto> getAllAuditLogs() {
        return auditLogService.getAllAuditLogs();
    }

    @GetMapping("/{id}")
    public AuditLogDto getAuditLog(@PathVariable Long id) {
        return auditLogService.getAuditLogById(id);
    }

    @GetMapping("/entity")
    public List<AuditLogDto> getByEntity(
            @RequestParam String entityType,
            @RequestParam String entityId) {

        return auditLogService.getByEntity(
                entityType,
                entityId
        );
    }

    @GetMapping("/type/{entityType}")
    public List<AuditLogDto> getByEntityType(
            @PathVariable String entityType) {

        return auditLogService.getByEntityType(entityType);
    }

    @GetMapping("/user/{performedBy}")
    public List<AuditLogDto> getByPerformedBy(
            @PathVariable String performedBy) {

        return auditLogService.getByPerformedBy(performedBy);
    }

    @PostMapping
    public AuditLogDto createAuditLog(
            @Valid @RequestBody AuditLogDto dto) {

        return auditLogService.createAuditLog(dto);
    }
}