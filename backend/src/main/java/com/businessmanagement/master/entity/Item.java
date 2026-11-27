package com.businessmanagement.master.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "unit", nullable = false, length = 20)
    private String unit = "KG";

    @Column(name = "actual_stock_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal actualStockKg = BigDecimal.ZERO;

    @Column(name = "standard_stock_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal standardStockKg = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getActualStockKg() { return actualStockKg; }
    public void setActualStockKg(BigDecimal actualStockKg) { this.actualStockKg = actualStockKg; }
    public BigDecimal getStandardStockKg() { return standardStockKg; }
    public void setStandardStockKg(BigDecimal standardStockKg) { this.standardStockKg = standardStockKg; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}