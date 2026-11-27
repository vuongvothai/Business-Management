package com.businessmanagement.purchase.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ImportRequest(
        Long weighTicketId,
        @NotNull Long supplierId,
        @NotNull Long itemId,
        Long warehouseId,
        Long purchaseContractId,
        int numberOfBags,
        @NotNull BigDecimal actualWeightKg,
        @NotNull BigDecimal standardWeightKg,
        BigDecimal purchasePricePerKg,
        BigDecimal marketPricePerKg,
        BigDecimal deductionAmount,
        @NotNull LocalDate importDate,
        String note,
        @NotNull Long createdBy
) {}