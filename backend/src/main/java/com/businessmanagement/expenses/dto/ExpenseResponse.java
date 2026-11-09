package com.businessmanagement.expenses.dto;

import com.businessmanagement.expenses.entity.Expense;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ExpenseResponse(Long id, BigDecimal totalAmount, LocalDate periodStart,
                              LocalDate periodEnd, String note, Long createdByUserId,
                              Long updatedByUserId, List<ExpenseDetailResponse> details) {
    public static ExpenseResponse from(Expense expense) {
        return new ExpenseResponse(expense.getId(), expense.getTotalAmount(), expense.getPeriodStart(),
                expense.getPeriodEnd(), expense.getNote(),
                expense.getCreatedBy() == null ? null : expense.getCreatedBy().getId(),
                expense.getUpdatedBy() == null ? null : expense.getUpdatedBy().getId(),
                expense.getDetails().stream().map(ExpenseDetailResponse::from).toList());
    }
}
