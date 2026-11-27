package com.businessmanagement.payment.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "debt_ledger")
public class DebtLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "debt_id")
    private Long debtId;

    @Column(name = "party_type", nullable = false, length = 10)
    private String partyType;

    @Column(name = "party_id", nullable = false)
    private Long partyId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "debit_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal debitAmount = BigDecimal.ZERO;

    @Column(name = "credit_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal creditAmount = BigDecimal.ZERO;

    @Column(name = "balance_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal balanceAmount = BigDecimal.ZERO;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "note", length = 1000)
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); updatedAt = createdAt; }
    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getDebtId() { return debtId; }
    public void setDebtId(Long debtId) { this.debtId = debtId; }
    public String getPartyType() { return partyType; }
    public void setPartyType(String partyType) { this.partyType = partyType; }
    public Long getPartyId() { return partyId; }
    public void setPartyId(Long partyId) { this.partyId = partyId; }
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public BigDecimal getDebitAmount() { return debitAmount; }
    public void setDebitAmount(BigDecimal debitAmount) { this.debitAmount = debitAmount; }
    public BigDecimal getCreditAmount() { return creditAmount; }
    public void setCreditAmount(BigDecimal creditAmount) { this.creditAmount = creditAmount; }
    public BigDecimal getBalanceAmount() { return balanceAmount; }
    public void setBalanceAmount(BigDecimal balanceAmount) { this.balanceAmount = balanceAmount; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}