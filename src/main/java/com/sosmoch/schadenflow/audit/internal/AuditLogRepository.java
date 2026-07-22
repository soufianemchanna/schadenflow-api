package com.sosmoch.schadenflow.audit.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByEntityTypeAndEntityIdOrderByPerformedAtDesc(String entityType, UUID entityId);

    List<AuditLog> findByPerformedByOrderByPerformedAtDesc(String performedBy);

}
