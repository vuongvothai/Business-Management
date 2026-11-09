package com.businessmanagement.expenses.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record ExpenseRequest(
        @NotNull LocalDate periodStart,
        @NotNull LocalDate periodEnd,
        @Size(max = 1000) String note,
        Long createdByUserId,
        @NotEmpty List<@Valid ExpenseDetailRequest> details) {
    @AssertTrue(message = "periodEnd must not be before periodStart")
    public boolean isPeriodValid() {
        return periodStart == null || periodEnd == null || !periodEnd.isBefore(periodStart);
    }
}
