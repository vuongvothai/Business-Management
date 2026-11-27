package com.businessmanagement.consignment.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ConsignmentPriceLockRequest(
        @NotNull Long customerId,
        @NotNull Long consignmentId,
        @NotNull Long itemId,
        @NotNull BigDecimal lockedVolumeKg,
        @NotNull LocalDateTime lockedAt,
        @NotNull BigDecimal lockedPricePerKg,
        BigDecimal advanceDeductionAmount,
        String note,
        Long createdBy
) {}