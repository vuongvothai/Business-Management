package com.businessmanagement.imports.entity;

import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.entity.Item;
import com.businessmanagement.master.entity.Supplier;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "import_ticket")
public class ImportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "import_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "number_of_bags", nullable = false)
    private Integer numberOfBags;

    @Column(name = "total_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal totalKg;

    @Column(name = "moisture_percent", precision = 5, scale = 2)
    private BigDecimal moisturePercent;

    @Column(name = "impurity_percent", precision = 5, scale = 2)
    private BigDecimal impurityPercent;

    @Column(name = "purchase_price_per_kg", precision = 15, scale = 2)
    private BigDecimal purchasePricePerKg;

    @Column(name = "market_price_per_kg", precision = 15, scale = 2)
    private BigDecimal marketPricePerKg;

    @Column(name = "market_difference_per_kg", precision = 15, scale = 2)
    private BigDecimal marketDifferencePerKg;

    @Column(name = "photo_url", length = 1000)
    private String photoUrl;

    @Column(name = "import_date", nullable = false)
    private LocalDate importDate;

    @Column(length = 1000)
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ImportStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private AppUser createdBy;

    @OneToMany(mappedBy = "importTicket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImportWeight> weights = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected ImportTicket() {
    }

    public ImportTicket(Supplier supplier, Item item, LocalDate importDate,
                        BigDecimal moisturePercent, BigDecimal impurityPercent,
                        String photoUrl, String note, AppUser createdBy) {
        this.supplier = supplier;
        this.item = item;
        this.importDate = importDate;
        this.moisturePercent = moisturePercent;
        this.impurityPercent = impurityPercent;
        this.photoUrl = photoUrl;
        this.note = note;
        this.createdBy = createdBy;
        this.status = ImportStatus.CONFIRMED;
    }

    public void addWeight(ImportWeight weight) {
        weights.add(weight);
        weight.assignTo(this);
    }

    public void setTotals(int numberOfBags, BigDecimal totalKg) {
        this.numberOfBags = numberOfBags;
        this.totalKg = totalKg;
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
    public Integer getNumberOfBags() { return numberOfBags; }
    public BigDecimal getTotalKg() { return totalKg; }
    public BigDecimal getMoisturePercent() { return moisturePercent; }
    public BigDecimal getImpurityPercent() { return impurityPercent; }
    public String getPhotoUrl() { return photoUrl; }
    public LocalDate getImportDate() { return importDate; }
    public String getNote() { return note; }
    public ImportStatus getStatus() { return status; }
    public List<ImportWeight> getWeights() { return weights; }
}
