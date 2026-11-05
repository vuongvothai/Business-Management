package com.businessmanagement.pricelocks.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PriceLockRequest(
        @NotNull Long supplierId,
        @NotNull Long itemId,
        @NotNull LocalDate lockDate,
        @NotNull @DecimalMin("0.001") BigDecimal lockedVolume,
        @NotNull @DecimalMin("0.01") BigDecimal lockedPricePerKg,
        @Size(max = 1000) String note,
        Long createdByUserId) {
}
