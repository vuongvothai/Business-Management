package com.businessmanagement.report.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "alert_rule")
public class AlertRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long alertId;

    @Column(name = "alert_type", nullable = false, length = 30)
    private String alertType;

    @Column(name = "party_type", length = 10)
    private String partyType;

    @Column(name = "party_id")
    private Long partyId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "threshold_value", nullable = false, precision = 18, scale = 3)
    private BigDecimal thresholdValue;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    public Long getAlertId() { return alertId; }
    public void setAlertId(Long alertId) { this.alertId = alertId; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getPartyType() { return partyType; }
    public void setPartyType(String partyType) { this.partyType = partyType; }
    public Long getPartyId() { return partyId; }
    public void setPartyId(Long partyId) { this.partyId = partyId; }
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public BigDecimal getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(BigDecimal thresholdValue) { this.thresholdValue = thresholdValue; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}