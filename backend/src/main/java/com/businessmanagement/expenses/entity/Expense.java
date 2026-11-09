package com.businessmanagement.expenses.entity;

import com.businessmanagement.master.entity.AppUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long id;

    @Column(name = "total_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(length = 1000)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private AppUser createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private AppUser updatedBy;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseDetail> details = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Expense() {
    }

    public Expense(LocalDate periodStart, LocalDate periodEnd, String note, AppUser createdBy) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.note = note;
        this.createdBy = createdBy;
    }

    public void addDetail(ExpenseDetail detail) {
        details.add(detail);
        detail.assignTo(this);
        recalculateTotal();
    }

    public void replaceDetails(LocalDate periodStart, LocalDate periodEnd, String note, AppUser updatedBy) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.note = note;
        this.updatedBy = updatedBy;
        details.clear();
        totalAmount = BigDecimal.ZERO;
    }

    private void recalculateTotal() {
        totalAmount = details.stream().map(ExpenseDetail::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
    public BigDecimal getTotalAmount() { return totalAmount; }
    public LocalDate getPeriodStart() { return periodStart; }
    public LocalDate getPeriodEnd() { return periodEnd; }
    public String getNote() { return note; }
    public AppUser getCreatedBy() { return createdBy; }
    public AppUser getUpdatedBy() { return updatedBy; }
    public List<ExpenseDetail> getDetails() { return List.copyOf(details); }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
