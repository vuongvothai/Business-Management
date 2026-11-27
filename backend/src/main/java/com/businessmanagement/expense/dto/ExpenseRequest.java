package com.businessmanagement.expense.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ExpenseRequest(
        String paymentMethod,
        @NotNull BigDecimal totalAmount,
        @NotNull LocalDate periodStart,
        @NotNull LocalDate periodEnd,
        String note,
        @NotNull Long createdBy,
        List<DetailRequest> details
) {
    public record DetailRequest(
            @NotNull Long categoryId,
            @NotNull BigDecimal amount,
            String note
    ) {}
}