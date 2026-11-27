package com.businessmanagement.consignment.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsignmentTicketRequest(
        Long weighTicketId,
        @NotNull Long customerId,
        @NotNull Long itemId,
        Long warehouseId,
        int numberOfBags,
        @NotNull BigDecimal actualWeightKg,
        @NotNull BigDecimal standardWeightKg,
        BigDecimal moisturePct,
        BigDecimal impurityPct,
        BigDecimal blackBrokenRatio,
        String screenSize,
        int freeStorageDays,
        BigDecimal storageFeePerTonMonth,
        BigDecimal naturalLossRatePct,
        String photoUrl,
        @NotNull LocalDate consignmentDate,
        String note,
        @NotNull Long createdBy
) {}