package com.businessmanagement.purchase.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_contract")
public class PurchaseContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_contract_id")
    private Long purchaseContractId;

    @Column(name = "contract_code", nullable = false, length = 50)
    private String contractCode;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "committed_volume_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal committedVolumeKg;

    @Column(name = "delivered_volume_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal deliveredVolumeKg = BigDecimal.ZERO;

    @Column(name = "remaining_volume_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal remainingVolumeKg;

    @Column(name = "locked_price_per_kg", nullable = false, precision = 15, scale = 2)
    private BigDecimal lockedPricePerKg;

    @Column(name = "contract_date", nullable = false)
    private LocalDate contractDate;

    @Column(name = "delivery_deadline")
    private LocalDate deliveryDeadline;

    @Column(name = "note", length = 1000)
    private String note;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (remainingVolumeKg == null) remainingVolumeKg = committedVolumeKg;
    }

    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getPurchaseContractId() { return purchaseContractId; }
    public void setPurchaseContractId(Long purchaseContractId) { this.purchaseContractId = purchaseContractId; }
    public String getContractCode() { return contractCode; }
    public void setContractCode(String contractCode) { this.contractCode = contractCode; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public BigDecimal getCommittedVolumeKg() { return committedVolumeKg; }
    public void setCommittedVolumeKg(BigDecimal committedVolumeKg) { this.committedVolumeKg = committedVolumeKg; }
    public BigDecimal getDeliveredVolumeKg() { return deliveredVolumeKg; }
    public void setDeliveredVolumeKg(BigDecimal deliveredVolumeKg) { this.deliveredVolumeKg = deliveredVolumeKg; }
    public BigDecimal getRemainingVolumeKg() { return remainingVolumeKg; }
    public void setRemainingVolumeKg(BigDecimal remainingVolumeKg) { this.remainingVolumeKg = remainingVolumeKg; }
    public BigDecimal getLockedPricePerKg() { return lockedPricePerKg; }
    public void setLockedPricePerKg(BigDecimal lockedPricePerKg) { this.lockedPricePerKg = lockedPricePerKg; }
    public LocalDate getContractDate() { return contractDate; }
    public void setContractDate(LocalDate contractDate) { this.contractDate = contractDate; }
    public LocalDate getDeliveryDeadline() { return deliveryDeadline; }
    public void setDeliveryDeadline(LocalDate deliveryDeadline) { this.deliveryDeadline = deliveryDeadline; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}