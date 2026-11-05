package com.businessmanagement.imports.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ImportRequest(
        @NotNull Long supplierId,
        @NotNull Long itemId,
        @NotNull LocalDate importDate,
        @NotEmpty List<@Valid ImportWeightRequest> weights,
        @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal moisturePercent,
        @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal impurityPercent,
        @DecimalMin("0.01") BigDecimal purchasePricePerKg,
        @DecimalMin("0.01") BigDecimal marketPricePerKg,
        @Size(max = 1000) String photoUrl,
        @Size(max = 1000) String note,
        Long createdByUserId) {
}
