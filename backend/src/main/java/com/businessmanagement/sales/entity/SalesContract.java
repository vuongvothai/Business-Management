package com.businessmanagement.sales.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales_contract")
public class SalesContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private Long contractId;

    @Column(name = "contract_code", nullable = false, length = 50)
    private String contractCode;

    @Column(name = "contract_type", nullable = false, length = 20)
    private String contractType = "DIRECT";

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "committed_volume_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal committedVolumeKg;

    @Column(name = "delivered_volume_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal deliveredVolumeKg = BigDecimal.ZERO;

    @Column(name = "remaining_volume_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal remainingVolumeKg;

    @Column(name = "unit_price_per_kg", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPricePerKg;

    @Column(name = "market_price_per_kg", precision = 15, scale = 2)
    private BigDecimal marketPricePerKg;

    @Column(name = "avg_cost_per_kg", precision = 15, scale = 2)
    private BigDecimal avgCostPerKg;

    @Column(name = "deposit_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal depositAmount = BigDecimal.ZERO;

    @Column(name = "total_revenue", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "estimated_profit", precision = 18, scale = 2)
    private BigDecimal estimatedProfit;

    @Column(name = "contract_date", nullable = false)
    private LocalDate contractDate;

    @Column(name = "delivery_deadline")
    private LocalDate deliveryDeadline;

    @Column(name = "note", length = 1000)
    private String note;

    @Column(name = "status", nullable = false, length = 30)
    private String status = "DRAFT";

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

    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public String getContractCode() { return contractCode; }
    public void setContractCode(String contractCode) { this.contractCode = contractCode; }
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public BigDecimal getCommittedVolumeKg() { return committedVolumeKg; }
    public void setCommittedVolumeKg(BigDecimal committedVolumeKg) { this.committedVolumeKg = committedVolumeKg; }
    public BigDecimal getDeliveredVolumeKg() { return deliveredVolumeKg; }
    public void setDeliveredVolumeKg(BigDecimal deliveredVolumeKg) { this.deliveredVolumeKg = deliveredVolumeKg; }
    public BigDecimal getRemainingVolumeKg() { return remainingVolumeKg; }
    public void setRemainingVolumeKg(BigDecimal remainingVolumeKg) { this.remainingVolumeKg = remainingVolumeKg; }
    public BigDecimal getUnitPricePerKg() { return unitPricePerKg; }
    public void setUnitPricePerKg(BigDecimal unitPricePerKg) { this.unitPricePerKg = unitPricePerKg; }
    public BigDecimal getMarketPricePerKg() { return marketPricePerKg; }
    public void setMarketPricePerKg(BigDecimal marketPricePerKg) { this.marketPricePerKg = marketPricePerKg; }
    public BigDecimal getAvgCostPerKg() { return avgCostPerKg; }
    public void setAvgCostPerKg(BigDecimal avgCostPerKg) { this.avgCostPerKg = avgCostPerKg; }
    public BigDecimal getDepositAmount() { return depositAmount; }
    public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    public BigDecimal getEstimatedProfit() { return estimatedProfit; }
    public void setEstimatedProfit(BigDecimal estimatedProfit) { this.estimatedProfit = estimatedProfit; }
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