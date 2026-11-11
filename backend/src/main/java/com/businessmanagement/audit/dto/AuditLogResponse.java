package com.businessmanagement.audit.dto;

import com.businessmanagement.audit.entity.AuditLog;
import java.time.LocalDateTime;

public record AuditLogResponse(Long id, Long userId, String username, String action,
        String entityType, String entityId, String details, LocalDateTime createdAt) {
    public static AuditLogResponse from(AuditLog log) {
        return new AuditLogResponse(log.getId(), log.getUser().getId(), log.getUser().getUsername(),
                log.getAction(), log.getEntityType(), log.getEntityId(), log.getDetails(), log.getCreatedAt());
    }
}

