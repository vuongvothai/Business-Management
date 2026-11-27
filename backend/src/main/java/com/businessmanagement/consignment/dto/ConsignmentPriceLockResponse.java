package com.businessmanagement.consignment.dto;

import com.businessmanagement.consignment.entity.ConsignmentPriceLock;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ConsignmentPriceLockResponse(
        Long cplId,
        Long customerId,
        Long consignmentId,
        Long itemId,
        BigDecimal lockedVolumeKg,
        LocalDateTime lockedAt,
        BigDecimal lockedPricePerKg,
        BigDecimal grossAmount,
        BigDecimal advanceDeductionAmount,
        BigDecimal netAmount,
        String status
) {
    public static ConsignmentPriceLockResponse from(ConsignmentPriceLock c) {
        return new ConsignmentPriceLockResponse(
                c.getCplId(), c.getCustomerId(), c.getConsignmentId(),
                c.getItemId(), c.getLockedVolumeKg(), c.getLockedAt(),
                c.getLockedPricePerKg(), c.getGrossAmount(),
                c.getAdvanceDeductionAmount(), c.getNetAmount(), c.getStatus());
    }
}