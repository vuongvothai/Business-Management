package com.businessmanagement.purchase.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "import_ticket")
public class ImportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "import_id")
    private Long importId;

    @Column(name = "weigh_ticket_id")
    private Long weighTicketId;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "purchase_contract_id")
    private Long purchaseContractId;

    @Column(name = "number_of_bags", nullable = false)
    private int numberOfBags;

    @Column(name = "actual_weight_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal actualWeightKg;

    @Column(name = "standard_weight_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal standardWeightKg;

    @Column(name = "purchase_price_per_kg", precision = 15, scale = 2)
    private BigDecimal purchasePricePerKg;

    @Column(name = "market_price_per_kg", precision = 15, scale = 2)
    private BigDecimal marketPricePerKg;

    @Column(name = "deduction_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal deductionAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "import_date", nullable = false)
    private LocalDate importDate;

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

    public Long getImportId() { return importId; }
    public void setImportId(Long importId) { this.importId = importId; }
    public Long getWeighTicketId() { return weighTicketId; }
    public void setWeighTicketId(Long weighTicketId) { this.weighTicketId = weighTicketId; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getPurchaseContractId() { return purchaseContractId; }
    public void setPurchaseContractId(Long purchaseContractId) { this.purchaseContractId = purchaseContractId; }
    public int getNumberOfBags() { return numberOfBags; }
    public void setNumberOfBags(int numberOfBags) { this.numberOfBags = numberOfBags; }
    public BigDecimal getActualWeightKg() { return actualWeightKg; }
    public void setActualWeightKg(BigDecimal actualWeightKg) { this.actualWeightKg = actualWeightKg; }
    public BigDecimal getStandardWeightKg() { return standardWeightKg; }
    public void setStandardWeightKg(BigDecimal standardWeightKg) { this.standardWeightKg = standardWeightKg; }
    public BigDecimal getPurchasePricePerKg() { return purchasePricePerKg; }
    public void setPurchasePricePerKg(BigDecimal purchasePricePerKg) { this.purchasePricePerKg = purchasePricePerKg; }
    public BigDecimal getMarketPricePerKg() { return marketPricePerKg; }
    public void setMarketPricePerKg(BigDecimal marketPricePerKg) { this.marketPricePerKg = marketPricePerKg; }
    public BigDecimal getDeductionAmount() { return deductionAmount; }
    public void setDeductionAmount(BigDecimal deductionAmount) { this.deductionAmount = deductionAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public LocalDate getImportDate() { return importDate; }
    public void setImportDate(LocalDate importDate) { this.importDate = importDate; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}