package com.businessmanagement.consignment.dto;

import com.businessmanagement.consignment.entity.ConsignmentBalance;
import java.math.BigDecimal;

public record ConsignmentBalanceResponse(
        Long balanceId,
        Long customerId,
        Long itemId,
        BigDecimal totalSentKg,
        BigDecimal totalLockedKg,
        BigDecimal totalWithdrawnKg,
        BigDecimal remainingKg
) {
    public static ConsignmentBalanceResponse from(ConsignmentBalance b) {
        return new ConsignmentBalanceResponse(
                b.getBalanceId(), b.getCustomerId(), b.getItemId(),
                b.getTotalSentKg(), b.getTotalLockedKg(),
                b.getTotalWithdrawnKg(), b.getRemainingKg());
    }
}