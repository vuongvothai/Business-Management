package com.businessmanagement.audit.entity;

import com.businessmanagement.master.entity.AppUser;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id") private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false) private AppUser user;
    @Column(nullable = false, length = 30) private String action;
    @Column(name = "entity_type", nullable = false, length = 50) private String entityType;
    @Column(name = "entity_id", nullable = false, length = 100) private String entityId;
    @Column(length = 1000) private String details;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    protected AuditLog() { }
    public AuditLog(AppUser user, String action, String entityType, String entityId, String details) {
        this.user = user; this.action = action; this.entityType = entityType;
        this.entityId = entityId; this.details = details;
    }
    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public AppUser getUser() { return user; }
    public String getAction() { return action; }
    public String getEntityType() { return entityType; }
    public String getEntityId() { return entityId; }
    public String getDetails() { return details; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}