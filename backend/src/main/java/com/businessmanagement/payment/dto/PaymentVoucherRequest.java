package com.businessmanagement.payment.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PaymentVoucherRequest(
        @NotNull String code,
        @NotNull String voucherType,
        @NotNull String partyType,
        @NotNull Long partyId,
        @NotNull String paymentMethod,
        @NotNull BigDecimal amount,
        @NotNull LocalDate voucherDate,
        String note,
        @NotNull Long createdBy,
        List<AllocationRequest> allocations
) {
    public record AllocationRequest(
            @NotNull String refType,
            @NotNull Long refId,
            @NotNull BigDecimal allocatedAmount
    ) {}
}