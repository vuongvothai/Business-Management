package com.businessmanagement.imports.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ImportWeightRequest(
        @NotNull @DecimalMin(value = "0.001") BigDecimal weightKg,
        @NotNull @Min(1) @Max(2) Integer bagCount) {
}
