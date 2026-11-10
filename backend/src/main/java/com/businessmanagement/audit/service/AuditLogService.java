package com.businessmanagement.audit.service;

import com.businessmanagement.audit.entity.AuditLog;
import com.businessmanagement.audit.repository.AuditLogRepository;
import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.repository.AppUserRepository;
import jakarta.transaction.Transactional;
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
}