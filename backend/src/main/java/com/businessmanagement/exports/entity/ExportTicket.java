package com.businessmanagement.exports.entity;

import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.entity.Customer;
import com.businessmanagement.master.entity.Item;
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
@Table(name = "export_ticket")
public class ExportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "export_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "committed_volume", nullable = false, precision = 14, scale = 3)
    private BigDecimal committedVolume;

    @Column(name = "selling_price_per_kg", nullable = false, precision = 15, scale = 2)
    private BigDecimal sellingPricePerKg;

    @Column(name = "market_price_per_kg", precision = 15, scale = 2)
    private BigDecimal marketPricePerKg;

    @Column(name = "market_difference_per_kg", precision = 15, scale = 2)
    private BigDecimal marketDifferencePerKg;

    @Column(name = "avg_cost_per_kg", precision = 15, scale = 2)
    private BigDecimal avgCostPerKg;

    @Column(precision = 18, scale = 2)
    private BigDecimal revenue;

    @Column(name = "estimated_profit", precision = 18, scale = 2)
    private BigDecimal estimatedProfit;

    @Column(name = "export_date", nullable = false)
    private LocalDate exportDate;

    @Column(name = "delivery_due_date")
    private LocalDate deliveryDueDate;

    @Column(length = 1000)
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ExportStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private AppUser createdBy;

    @OneToMany(mappedBy = "exportTicket")
    private List<ExportDelivery> deliveries = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected ExportTicket() {
    }

    public ExportTicket(Customer customer, Item item, BigDecimal committedVolume,
                         BigDecimal sellingPricePerKg, LocalDate exportDate,
                         LocalDate deliveryDueDate, String note, AppUser createdBy) {
        this.customer = customer;
        this.item = item;
        this.committedVolume = committedVolume;
        this.sellingPricePerKg = sellingPricePerKg;
        this.exportDate = exportDate;
        this.deliveryDueDate = deliveryDueDate;
        this.note = note;
        this.createdBy = createdBy;
        this.status = ExportStatus.CONFIRMED;
        this.revenue = committedVolume.multiply(sellingPricePerKg);
    }

    public void updateDeliveryStatus(BigDecimal deliveredVolume) {
        status = deliveredVolume.compareTo(committedVolume) >= 0
                ? ExportStatus.COMPLETED : ExportStatus.PARTIALLY_DELIVERED;
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
    public Customer getCustomer() { return customer; }
    public Item getItem() { return item; }
    public BigDecimal getCommittedVolume() { return committedVolume; }
    public BigDecimal getSellingPricePerKg() { return sellingPricePerKg; }
    public BigDecimal getRevenue() { return revenue; }
    public LocalDate getExportDate() { return exportDate; }
    public LocalDate getDeliveryDueDate() { return deliveryDueDate; }
    public String getNote() { return note; }
    public ExportStatus getStatus() { return status; }
    public List<ExportDelivery> getDeliveries() { return deliveries; }
}
