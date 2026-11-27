package com.businessmanagement.sales.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SalesContractRequest(
        @NotNull String contractCode,
        String contractType,
        @NotNull Long customerId,
        @NotNull Long itemId,
        @NotNull BigDecimal committedVolumeKg,
        @NotNull BigDecimal unitPricePerKg,
        BigDecimal marketPricePerKg,
        BigDecimal avgCostPerKg,
        BigDecimal depositAmount,
        @NotNull LocalDate contractDate,
        LocalDate deliveryDeadline,
        String note,
        @NotNull Long createdBy
) {}