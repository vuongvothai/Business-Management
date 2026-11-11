package com.businessmanagement.inventory.entity;

import com.businessmanagement.master.entity.AppUser;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reconciliation_confirmation")
public class ReconciliationConfirmation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "confirmation_id") private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "confirmed_by", nullable = false) private AppUser confirmedBy;
    @Column(name = "confirmed_at", nullable = false, updatable = false) private LocalDateTime confirmedAt;
    @Column(length = 1000) private String note;
    @Column(name = "supplier_discrepancy_kg", nullable = false, precision = 18, scale = 3) private BigDecimal supplierDiscrepancyKg;
    @Column(name = "inventory_discrepancy_kg", nullable = false, precision = 18, scale = 3) private BigDecimal inventoryDiscrepancyKg;
    protected ReconciliationConfirmation() { }
    public ReconciliationConfirmation(AppUser confirmedBy, String note, BigDecimal supplierDiscrepancyKg, BigDecimal inventoryDiscrepancyKg) {
        this.confirmedBy = confirmedBy; this.note = note; this.supplierDiscrepancyKg = supplierDiscrepancyKg; this.inventoryDiscrepancyKg = inventoryDiscrepancyKg;
    }
    @PrePersist void onCreate() { confirmedAt = LocalDateTime.now(); }
    public Long getId() { return id; } public AppUser getConfirmedBy() { return confirmedBy; } public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public String getNote() { return note; } public BigDecimal getSupplierDiscrepancyKg() { return supplierDiscrepancyKg; } public BigDecimal getInventoryDiscrepancyKg() { return inventoryDiscrepancyKg; }
}
