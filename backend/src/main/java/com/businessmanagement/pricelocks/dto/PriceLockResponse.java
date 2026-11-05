package com.businessmanagement.pricelocks.dto;

import com.businessmanagement.pricelocks.entity.PriceLock;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PriceLockResponse(
        Long id,
        Long supplierId,
        Long itemId,
        LocalDate lockDate,
        BigDecimal lockedVolume,
        BigDecimal lockedPricePerKg,
        BigDecimal amount,
        String note,
        String status) {

    public static PriceLockResponse from(PriceLock lock) {
        return new PriceLockResponse(
                lock.getId(),
                lock.getSupplier().getId(),
                lock.getItem().getId(),
                lock.getLockDate(),
                lock.getLockedVolume(),
                lock.getLockedPricePerKg(),
                lock.getAmount(),
                lock.getNote(),
                lock.getStatus().name());
    }
}
