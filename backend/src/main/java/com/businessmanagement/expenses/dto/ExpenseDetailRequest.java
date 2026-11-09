package com.businessmanagement.expenses.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ExpenseDetailRequest(
        @NotBlank @Size(max = 100) String categoryName,
        @NotNull @DecimalMin(value = "0.00") BigDecimal amount) {
}
