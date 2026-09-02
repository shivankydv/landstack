package com.landstack.backend.repository;

import com.landstack.backend.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByEntityType(String entityType);

    List<AuditLog> findByEntityTypeAndEntityId(
            String entityType,
            String entityId
    );

    List<AuditLog> findByPerformedBy(String performedBy);
}