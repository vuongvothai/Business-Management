package com.businessmanagement.consignment.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "advance_payment")
public class AdvancePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advance_id")
    private Long advanceId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "consignment_id", nullable = false)
    private Long consignmentId;

    @Column(name = "advance_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal advanceAmount;

    @Column(name = "advance_date", nullable = false)
    private LocalDate advanceDate;

    @Column(name = "monthly_interest_rate_pct", nullable = false, precision = 8, scale = 4)
    private BigDecimal monthlyInterestRatePct;

    @Column(name = "market_price_at_advance", precision = 15, scale = 2)
    private BigDecimal marketPriceAtAdvance;

    @Column(name = "interest_days", nullable = false)
    private int interestDays;

    @Column(name = "interest_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal interestAmount = BigDecimal.ZERO;

    @Column(name = "total_repayment", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalRepayment = BigDecimal.ZERO;

    @Column(name = "settled_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal settledAmount = BigDecimal.ZERO;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";

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

    public Long getAdvanceId() { return advanceId; }
    public void setAdvanceId(Long advanceId) { this.advanceId = advanceId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getConsignmentId() { return consignmentId; }
    public void setConsignmentId(Long consignmentId) { this.consignmentId = consignmentId; }
    public BigDecimal getAdvanceAmount() { return advanceAmount; }
    public void setAdvanceAmount(BigDecimal advanceAmount) { this.advanceAmount = advanceAmount; }
    public LocalDate getAdvanceDate() { return advanceDate; }
    public void setAdvanceDate(LocalDate advanceDate) { this.advanceDate = advanceDate; }
    public BigDecimal getMonthlyInterestRatePct() { return monthlyInterestRatePct; }
    public void setMonthlyInterestRatePct(BigDecimal monthlyInterestRatePct) { this.monthlyInterestRatePct = monthlyInterestRatePct; }
    public BigDecimal getMarketPriceAtAdvance() { return marketPriceAtAdvance; }
    public void setMarketPriceAtAdvance(BigDecimal marketPriceAtAdvance) { this.marketPriceAtAdvance = marketPriceAtAdvance; }
    public int getInterestDays() { return interestDays; }
    public void setInterestDays(int interestDays) { this.interestDays = interestDays; }
    public BigDecimal getInterestAmount() { return interestAmount; }
    public void setInterestAmount(BigDecimal interestAmount) { this.interestAmount = interestAmount; }
    public BigDecimal getTotalRepayment() { return totalRepayment; }
    public void setTotalRepayment(BigDecimal totalRepayment) { this.totalRepayment = totalRepayment; }
    public BigDecimal getSettledAmount() { return settledAmount; }
    public void setSettledAmount(BigDecimal settledAmount) { this.settledAmount = settledAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}