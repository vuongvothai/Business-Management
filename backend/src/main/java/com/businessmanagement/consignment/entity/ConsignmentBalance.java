package com.businessmanagement.consignment.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "consignment_balance")
public class ConsignmentBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "balance_id")
    private Long balanceId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "total_sent_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal totalSentKg = BigDecimal.ZERO;

    @Column(name = "total_locked_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal totalLockedKg = BigDecimal.ZERO;

    @Column(name = "total_withdrawn_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal totalWithdrawnKg = BigDecimal.ZERO;

    @Column(name = "remaining_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal remainingKg = BigDecimal.ZERO;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getBalanceId() { return balanceId; }
    public void setBalanceId(Long balanceId) { this.balanceId = balanceId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public BigDecimal getTotalSentKg() { return totalSentKg; }
    public void setTotalSentKg(BigDecimal totalSentKg) { this.totalSentKg = totalSentKg; }
    public BigDecimal getTotalLockedKg() { return totalLockedKg; }
    public void setTotalLockedKg(BigDecimal totalLockedKg) { this.totalLockedKg = totalLockedKg; }
    public BigDecimal getTotalWithdrawnKg() { return totalWithdrawnKg; }
    public void setTotalWithdrawnKg(BigDecimal totalWithdrawnKg) { this.totalWithdrawnKg = totalWithdrawnKg; }
    public BigDecimal getRemainingKg() { return remainingKg; }
    public void setRemainingKg(BigDecimal remainingKg) { this.remainingKg = remainingKg; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}