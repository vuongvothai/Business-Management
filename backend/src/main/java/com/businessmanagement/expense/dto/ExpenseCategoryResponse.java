package com.businessmanagement.expense.dto;

import com.businessmanagement.expense.entity.ExpenseCategory;

public record ExpenseCategoryResponse(Long categoryId, String categoryName) {
    public static ExpenseCategoryResponse from(ExpenseCategory c) {
        return new ExpenseCategoryResponse(c.getCategoryId(), c.getCategoryName());
    }
}