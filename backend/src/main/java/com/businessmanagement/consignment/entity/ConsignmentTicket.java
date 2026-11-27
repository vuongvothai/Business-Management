package com.businessmanagement.consignment.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "consignment_ticket")
public class ConsignmentTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consignment_id")
    private Long consignmentId;

    @Column(name = "weigh_ticket_id")
    private Long weighTicketId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "number_of_bags", nullable = false)
    private int numberOfBags;

    @Column(name = "actual_weight_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal actualWeightKg;

    @Column(name = "standard_weight_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal standardWeightKg;

    @Column(name = "moisture_pct", precision = 5, scale = 2)
    private BigDecimal moisturePct;

    @Column(name = "impurity_pct", precision = 5, scale = 2)
    private BigDecimal impurityPct;

    @Column(name = "black_broken_ratio", precision = 5, scale = 2)
    private BigDecimal blackBrokenRatio;

    @Column(name = "screen_size", length = 20)
    private String screenSize;

    @Column(name = "free_storage_days", nullable = false)
    private int freeStorageDays;

    @Column(name = "storage_fee_per_ton_month", precision = 12, scale = 2)
    private BigDecimal storageFeePerTonMonth;

    @Column(name = "natural_loss_rate_pct", precision = 5, scale = 2)
    private BigDecimal naturalLossRatePct;

    @Column(name = "photo_url", length = 1000)
    private String photoUrl;

    @Column(name = "consignment_date", nullable = false)
    private LocalDate consignmentDate;

    @Column(name = "note", length = 1000)
    private String note;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "DRAFT";

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); updatedAt = createdAt; }
    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getConsignmentId() { return consignmentId; }
    public void setConsignmentId(Long consignmentId) { this.consignmentId = consignmentId; }
    public Long getWeighTicketId() { return weighTicketId; }
    public void setWeighTicketId(Long weighTicketId) { this.weighTicketId = weighTicketId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public int getNumberOfBags() { return numberOfBags; }
    public void setNumberOfBags(int numberOfBags) { this.numberOfBags = numberOfBags; }
    public BigDecimal getActualWeightKg() { return actualWeightKg; }
    public void setActualWeightKg(BigDecimal actualWeightKg) { this.actualWeightKg = actualWeightKg; }
    public BigDecimal getStandardWeightKg() { return standardWeightKg; }
    public void setStandardWeightKg(BigDecimal standardWeightKg) { this.standardWeightKg = standardWeightKg; }
    public BigDecimal getMoisturePct() { return moisturePct; }
    public void setMoisturePct(BigDecimal moisturePct) { this.moisturePct = moisturePct; }
    public BigDecimal getImpurityPct() { return impurityPct; }
    public void setImpurityPct(BigDecimal impurityPct) { this.impurityPct = impurityPct; }
    public BigDecimal getBlackBrokenRatio() { return blackBrokenRatio; }
    public void setBlackBrokenRatio(BigDecimal blackBrokenRatio) { this.blackBrokenRatio = blackBrokenRatio; }
    public String getScreenSize() { return screenSize; }
    public void setScreenSize(String screenSize) { this.screenSize = screenSize; }
    public int getFreeStorageDays() { return freeStorageDays; }
    public void setFreeStorageDays(int freeStorageDays) { this.freeStorageDays = freeStorageDays; }
    public BigDecimal getStorageFeePerTonMonth() { return storageFeePerTonMonth; }
    public void setStorageFeePerTonMonth(BigDecimal storageFeePerTonMonth) { this.storageFeePerTonMonth = storageFeePerTonMonth; }
    public BigDecimal getNaturalLossRatePct() { return naturalLossRatePct; }
    public void setNaturalLossRatePct(BigDecimal naturalLossRatePct) { this.naturalLossRatePct = naturalLossRatePct; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public LocalDate getConsignmentDate() { return consignmentDate; }
    public void setConsignmentDate(LocalDate consignmentDate) { this.consignmentDate = consignmentDate; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}