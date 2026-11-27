package com.businessmanagement.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transaction")
public class InventoryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "transaction_type", nullable = false, length = 20)
    private String transactionType;

    @Column(name = "ref_type", nullable = false, length = 30)
    private String refType;

    @Column(name = "ref_id", nullable = false)
    private Long refId;

    @Column(name = "actual_in_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal actualInKg = BigDecimal.ZERO;

    @Column(name = "actual_out_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal actualOutKg = BigDecimal.ZERO;

    @Column(name = "standard_in_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal standardInKg = BigDecimal.ZERO;

    @Column(name = "standard_out_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal standardOutKg = BigDecimal.ZERO;

    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "CONFIRMED";

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public String getRefType() { return refType; }
    public void setRefType(String refType) { this.refType = refType; }
    public Long getRefId() { return refId; }
    public void setRefId(Long refId) { this.refId = refId; }
    public BigDecimal getActualInKg() { return actualInKg; }
    public void setActualInKg(BigDecimal actualInKg) { this.actualInKg = actualInKg; }
    public BigDecimal getActualOutKg() { return actualOutKg; }
    public void setActualOutKg(BigDecimal actualOutKg) { this.actualOutKg = actualOutKg; }
    public BigDecimal getStandardInKg() { return standardInKg; }
    public void setStandardInKg(BigDecimal standardInKg) { this.standardInKg = standardInKg; }
    public BigDecimal getStandardOutKg() { return standardOutKg; }
    public void setStandardOutKg(BigDecimal standardOutKg) { this.standardOutKg = standardOutKg; }
    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}