package com.businessmanagement.purchase.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PurchaseContractRequest(
        @NotNull String contractCode,
        @NotNull Long supplierId,
        @NotNull Long itemId,
        @NotNull BigDecimal committedVolumeKg,
        @NotNull BigDecimal lockedPricePerKg,
        @NotNull LocalDate contractDate,
        LocalDate deliveryDeadline,
        String note,
        @NotNull Long createdBy
) {}