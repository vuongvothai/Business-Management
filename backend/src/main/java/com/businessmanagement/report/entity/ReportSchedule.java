package com.businessmanagement.report.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "report_schedule")
public class ReportSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "report_type", nullable = false, length = 30)
    private String reportType;

    @Column(name = "cron_expr", nullable = false, length = 50)
    private String cronExpr;

    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "last_sent_at")
    private LocalDateTime lastSentAt;

    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public String getCronExpr() { return cronExpr; }
    public void setCronExpr(String cronExpr) { this.cronExpr = cronExpr; }
    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getLastSentAt() { return lastSentAt; }
    public void setLastSentAt(LocalDateTime lastSentAt) { this.lastSentAt = lastSentAt; }
}