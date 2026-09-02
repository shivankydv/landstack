package com.landstack.backend.service;

import com.landstack.backend.dto.AuditLogDto;
import com.landstack.backend.entity.AuditLog;
import com.landstack.backend.exception.ResourceNotFoundException;
import com.landstack.backend.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    // =========================
    // Get all audit logs
    // =========================

    public List<AuditLogDto> getAllAuditLogs() {

        return auditLogRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // =========================
    // Get audit log by ID
    // =========================

    public AuditLogDto getAuditLogById(Long id) {

        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Audit log not found: " + id
                        )
                );

        return toDto(auditLog);
    }

    // =========================
    // Get logs by entity
    // =========================

    public List<AuditLogDto> getByEntity(
            String entityType,
            String entityId) {

        return auditLogRepository
                .findByEntityTypeAndEntityId(
                        entityType,
                        entityId
                )
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // =========================
    // Get logs by entity type
    // =========================

    public List<AuditLogDto> getByEntityType(
            String entityType) {

        return auditLogRepository
                .findByEntityType(entityType)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // =========================
    // Get logs by user
    // =========================

    public List<AuditLogDto> getByPerformedBy(
            String performedBy) {

        return auditLogRepository
                .findByPerformedBy(performedBy)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // =========================
    // Create audit log through API
    // =========================

    public AuditLogDto createAuditLog(AuditLogDto dto) {

        AuditLog auditLog = new AuditLog();

        auditLog.setEntityType(dto.getEntityType());
        auditLog.setEntityId(dto.getEntityId());
        auditLog.setAction(dto.getAction());
        auditLog.setPerformedBy(dto.getPerformedBy());
        auditLog.setDetails(dto.getDetails());

        AuditLog savedAuditLog =
                auditLogRepository.save(auditLog);

        return toDto(savedAuditLog);
    }

    // =========================
    // Internal automatic logging
    // =========================

    public void log(
            String entityType,
            String entityId,
            String action,
            String performedBy,
            String details) {

        AuditLog auditLog = new AuditLog();

        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setAction(action);
        auditLog.setPerformedBy(performedBy);
        auditLog.setDetails(details);

        auditLogRepository.save(auditLog);
    }

    // =========================
    // Entity → DTO
    // =========================

    private AuditLogDto toDto(AuditLog auditLog) {

        AuditLogDto dto = new AuditLogDto();

        dto.setId(auditLog.getId());
        dto.setEntityType(auditLog.getEntityType());
        dto.setEntityId(auditLog.getEntityId());
        dto.setAction(auditLog.getAction());
        dto.setPerformedBy(auditLog.getPerformedBy());
        dto.setDetails(auditLog.getDetails());
        dto.setCreatedAt(auditLog.getCreatedAt());

        return dto;
    }
}