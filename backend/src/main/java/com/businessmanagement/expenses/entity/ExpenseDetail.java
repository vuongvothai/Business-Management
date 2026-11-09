package com.businessmanagement.expenses.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "expense_detail")
public class ExpenseDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    protected ExpenseDetail() {
    }

    public ExpenseDetail(String categoryName, BigDecimal amount) {
        this.categoryName = categoryName;
        this.amount = amount;
    }

    void assignTo(Expense expense) {
        this.expense = expense;
    }

    public Long getId() { return id; }
    public Expense getExpense() { return expense; }
    public String getCategoryName() { return categoryName; }
    public BigDecimal getAmount() { return amount; }
}
