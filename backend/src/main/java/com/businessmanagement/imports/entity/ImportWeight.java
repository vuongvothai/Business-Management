package com.businessmanagement.imports.entity;

import com.businessmanagement.master.entity.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "import_weight")
public class ImportWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weight_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "import_id", nullable = false)
    private ImportTicket importTicket;

    @Column(name = "weight_kg", nullable = false, precision = 12, scale = 3)
    private BigDecimal weightKg;

    @Column(name = "bag_count", nullable = false, columnDefinition = "TINYINT")
    private Integer bagCount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private AppUser createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected ImportWeight() {
    }

    public ImportWeight(BigDecimal weightKg, Integer bagCount, AppUser createdBy) {
        this.weightKg = weightKg;
        this.bagCount = bagCount;
        this.createdBy = createdBy;
    }

    void assignTo(ImportTicket importTicket) {
        this.importTicket = importTicket;
    }

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public BigDecimal getWeightKg() { return weightKg; }
    public Integer getBagCount() { return bagCount; }
}
