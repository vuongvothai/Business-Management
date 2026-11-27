package com.businessmanagement.loan.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_transaction")
public class LoanTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_txn_id")
    private Long loanTxnId;

    @Column(name = "loan_id", nullable = false)
    private Long loanId;

    @Column(name = "txn_type", nullable = false, length = 30)
    private String txnType;

    @Column(name = "amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "txn_date", nullable = false)
    private LocalDate txnDate;

    @Column(name = "interest_days")
    private int interestDays;

    @Column(name = "note", length = 1000)
    private String note;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getLoanTxnId() { return loanTxnId; }
    public void setLoanTxnId(Long loanTxnId) { this.loanTxnId = loanTxnId; }
    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public String getTxnType() { return txnType; }
    public void setTxnType(String txnType) { this.txnType = txnType; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getTxnDate() { return txnDate; }
    public void setTxnDate(LocalDate txnDate) { this.txnDate = txnDate; }
    public int getInterestDays() { return interestDays; }
    public void setInterestDays(int interestDays) { this.interestDays = interestDays; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}