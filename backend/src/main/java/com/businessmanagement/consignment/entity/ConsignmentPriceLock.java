package com.businessmanagement.consignment.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "consignment_price_lock")
public class ConsignmentPriceLock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cpl_id")
    private Long cplId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "consignment_id", nullable = false)
    private Long consignmentId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "locked_volume_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal lockedVolumeKg;

    @Column(name = "locked_at", nullable = false)
    private LocalDateTime lockedAt;

    @Column(name = "locked_price_per_kg", nullable = false, precision = 15, scale = 2)
    private BigDecimal lockedPricePerKg;

    @Column(name = "gross_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal grossAmount;

    @Column(name = "advance_deduction_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal advanceDeductionAmount = BigDecimal.ZERO;

    @Column(name = "net_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "note", length = 1000)
    private String note;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "DRAFT";

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); updatedAt = createdAt; }
    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getCplId() { return cplId; }
    public void setCplId(Long cplId) { this.cplId = cplId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getConsignmentId() { return consignmentId; }
    public void setConsignmentId(Long consignmentId) { this.consignmentId = consignmentId; }
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public BigDecimal getLockedVolumeKg() { return lockedVolumeKg; }
    public void setLockedVolumeKg(BigDecimal lockedVolumeKg) { this.lockedVolumeKg = lockedVolumeKg; }
    public LocalDateTime getLockedAt() { return lockedAt; }
    public void setLockedAt(LocalDateTime lockedAt) { this.lockedAt = lockedAt; }
    public BigDecimal getLockedPricePerKg() { return lockedPricePerKg; }
    public void setLockedPricePerKg(BigDecimal lockedPricePerKg) { this.lockedPricePerKg = lockedPricePerKg; }
    public BigDecimal getGrossAmount() { return grossAmount; }
    public void setGrossAmount(BigDecimal grossAmount) { this.grossAmount = grossAmount; }
    public BigDecimal getAdvanceDeductionAmount() { return advanceDeductionAmount; }
    public void setAdvanceDeductionAmount(BigDecimal advanceDeductionAmount) { this.advanceDeductionAmount = advanceDeductionAmount; }
    public BigDecimal getNetAmount() { return netAmount; }
    public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}