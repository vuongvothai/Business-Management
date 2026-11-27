package com.businessmanagement.expense.dto;

import com.businessmanagement.expense.entity.Expense;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponse(
        Long expenseId,
        String paymentMethod,
        BigDecimal totalAmount,
        LocalDate periodStart,
        LocalDate periodEnd,
        String status
) {
    public static ExpenseResponse from(Expense e) {
        return new ExpenseResponse(
                e.getExpenseId(), e.getPaymentMethod(),
                e.getTotalAmount(), e.getPeriodStart(),
                e.getPeriodEnd(), e.getStatus());
    }
}