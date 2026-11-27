package com.businessmanagement.consignment.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AdvancePaymentRequest(
        @NotNull Long customerId,
        @NotNull Long consignmentId,
        @NotNull BigDecimal advanceAmount,
        @NotNull LocalDate advanceDate,
        @NotNull BigDecimal monthlyInterestRatePct,
        BigDecimal marketPriceAtAdvance,
        @NotNull Long createdBy
) {}