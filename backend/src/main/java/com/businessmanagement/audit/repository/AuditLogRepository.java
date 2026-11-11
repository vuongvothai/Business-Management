package com.businessmanagement.audit.repository;

import com.businessmanagement.audit.entity.AuditLog;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findAllByOrderByCreatedAtDesc();
    List<AuditLog> findByUser_UsernameIgnoreCaseContainingAndEntityTypeIgnoreCaseContainingAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualOrderByCreatedAtDesc(
            String actor, String entityType, LocalDateTime from, LocalDateTime to);
}