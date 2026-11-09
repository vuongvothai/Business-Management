package com.businessmanagement.expenses.dto;

import com.businessmanagement.expenses.entity.ExpenseDetail;
import java.math.BigDecimal;

public record ExpenseDetailResponse(Long id, String categoryName, BigDecimal amount) {
    public static ExpenseDetailResponse from(ExpenseDetail detail) {
        return new ExpenseDetailResponse(detail.getId(), detail.getCategoryName(), detail.getAmount());
    }
}
