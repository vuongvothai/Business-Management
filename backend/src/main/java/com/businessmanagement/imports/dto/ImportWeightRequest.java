package com.businessmanagement.imports.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ImportWeightRequest(
        @NotNull @DecimalMin(value = "0.001") BigDecimal weightKg,
        @Min(1) Integer bagCount) {

    public ImportWeightRequest {
        if (bagCount == null) {
            bagCount = 2;
        }
    }
}
