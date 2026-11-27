package com.businessmanagement.loan.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_contract")
public class LoanContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long loanId;

    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "loan_type", nullable = false, length = 10)
    private String loanType;

    @Column(name = "party_type", nullable = false, length = 10)
    private String partyType;

    @Column(name = "party_id", nullable = false)
    private Long partyId;

    @Column(name = "principal_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal principalAmount;

    @Column(name = "interest_rate", nullable = false, precision = 8, scale = 4)
    private BigDecimal interestRate;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "outstanding_principal", nullable = false, precision = 18, scale = 2)
    private BigDecimal outstandingPrincipal;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (outstandingPrincipal == null) outstandingPrincipal = principalAmount;
    }
    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) { this.loanType = loanType; }
    public String getPartyType() { return partyType; }
    public void setPartyType(String partyType) { this.partyType = partyType; }
    public Long getPartyId() { return partyId; }
    public void setPartyId(Long partyId) { this.partyId = partyId; }
    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public BigDecimal getOutstandingPrincipal() { return outstandingPrincipal; }
    public void setOutstandingPrincipal(BigDecimal outstandingPrincipal) { this.outstandingPrincipal = outstandingPrincipal; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}