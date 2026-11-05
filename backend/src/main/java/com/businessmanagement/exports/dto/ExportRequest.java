package com.businessmanagement.exports.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ExportRequest(
        @NotNull Long customerId,
        @NotNull Long itemId,
        @NotNull @DecimalMin("0.001") BigDecimal committedVolume,
        @NotNull @DecimalMin("0.01") BigDecimal sellingPricePerKg,
        @NotNull LocalDate exportDate,
        LocalDate deliveryDueDate,
        @Size(max = 1000) String note,
        Long createdByUserId) {
}
