package com.businessmanagement.audit.service;

import com.businessmanagement.audit.entity.AuditLog;
import com.businessmanagement.audit.repository.AuditLogRepository;
import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {
    private final AuditLogRepository repository;
    private final AppUserRepository users;
    public AuditLogService(AuditLogRepository repository, AppUserRepository users) {
        this.repository = repository; this.users = users;
    }
    @Transactional
    public void record(AppUser user, String action, String entityType, Object entityId, String details) {
        if (user == null) throw new IllegalArgumentException("Audit user is required");
        repository.save(new AuditLog(user, action, entityType, String.valueOf(entityId), details));
    }

    @Transactional
    public List<AuditLog> list(String actor, String entityType, LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null && to.isBefore(from)) {
            throw new IllegalArgumentException("to must not be before from");
        }
        if (actor == null && entityType == null && from == null && to == null) {
            return repository.findAllByOrderByCreatedAtDesc();
        }
        return repository.findByUser_UsernameIgnoreCaseContainingAndEntityTypeIgnoreCaseContainingAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualOrderByCreatedAtDesc(
                actor == null ? "" : actor, entityType == null ? "" : entityType,
                from == null ? LocalDateTime.MIN : from, to == null ? LocalDateTime.MAX : to);
    }
}