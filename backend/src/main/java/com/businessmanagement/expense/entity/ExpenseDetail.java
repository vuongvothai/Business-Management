package com.businessmanagement.expense.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "expense_detail")
public class ExpenseDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_detail_id")
    private Long expenseDetailId;

    @Column(name = "expense_id", nullable = false)
    private Long expenseId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "note", length = 1000)
    private String note;

    public Long getExpenseDetailId() { return expenseDetailId; }
    public void setExpenseDetailId(Long expenseDetailId) { this.expenseDetailId = expenseDetailId; }
    public Long getExpenseId() { return expenseId; }
    public void setExpenseId(Long expenseId) { this.expenseId = expenseId; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}