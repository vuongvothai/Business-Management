package com.businessmanagement.purchase.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_delivery")
public class PurchaseDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_delivery_id")
    private Long purchaseDeliveryId;

    @Column(name = "purchase_contract_id", nullable = false)
    private Long purchaseContractId;

    @Column(name = "import_id", nullable = false)
    private Long importId;

    @Column(name = "delivered_actual_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal deliveredActualKg;

    @Column(name = "delivered_standard_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal deliveredStandardKg;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "CONFIRMED";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getPurchaseDeliveryId() { return purchaseDeliveryId; }
    public void setPurchaseDeliveryId(Long purchaseDeliveryId) { this.purchaseDeliveryId = purchaseDeliveryId; }
    public Long getPurchaseContractId() { return purchaseContractId; }
    public void setPurchaseContractId(Long purchaseContractId) { this.purchaseContractId = purchaseContractId; }
    public Long getImportId() { return importId; }
    public void setImportId(Long importId) { this.importId = importId; }
    public BigDecimal getDeliveredActualKg() { return deliveredActualKg; }
    public void setDeliveredActualKg(BigDecimal deliveredActualKg) { this.deliveredActualKg = deliveredActualKg; }
    public BigDecimal getDeliveredStandardKg() { return deliveredStandardKg; }
    public void setDeliveredStandardKg(BigDecimal deliveredStandardKg) { this.deliveredStandardKg = deliveredStandardKg; }
    public LocalDate getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}