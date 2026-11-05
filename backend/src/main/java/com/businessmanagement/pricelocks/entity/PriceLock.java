package com.businessmanagement.pricelocks.entity;

import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.entity.Item;
import com.businessmanagement.master.entity.Supplier;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_lock")
public class PriceLock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_lock_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "locked_volume", nullable = false, precision = 14, scale = 3)
    private BigDecimal lockedVolume;

    @Column(name = "lock_date", nullable = false)
    private LocalDate lockDate;

    @Column(name = "locked_price_per_kg", nullable = false, precision = 15, scale = 2)
    private BigDecimal lockedPricePerKg;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(length = 1000)
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PriceLockStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private AppUser createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected PriceLock() {
    }

    public PriceLock(Supplier supplier, Item item, LocalDate lockDate,
                     BigDecimal lockedVolume, BigDecimal lockedPricePerKg,
                     BigDecimal amount, String note, AppUser createdBy) {
        this.supplier = supplier;
        this.item = item;
        this.lockDate = lockDate;
        this.lockedVolume = lockedVolume;
        this.lockedPricePerKg = lockedPricePerKg;
        this.amount = amount;
        this.note = note;
        this.createdBy = createdBy;
        this.status = PriceLockStatus.CONFIRMED;
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Supplier getSupplier() { return supplier; }
    public Item getItem() { return item; }
    public LocalDate getLockDate() { return lockDate; }
    public BigDecimal getLockedVolume() { return lockedVolume; }
    public BigDecimal getLockedPricePerKg() { return lockedPricePerKg; }
    public BigDecimal getAmount() { return amount; }
    public String getNote() { return note; }
    public PriceLockStatus getStatus() { return status; }
}
