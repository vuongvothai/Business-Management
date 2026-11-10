package com.businessmanagement.pricelocks.dto;

import com.businessmanagement.pricelocks.entity.PriceLock;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PriceLockPrintData(Long id, Long supplierId, String supplierName,
                                 Long itemId, String itemName, String itemCategory,
                                 BigDecimal quantityKg, BigDecimal pricePerKg,
                                 BigDecimal amount, String status, LocalDate date,
                                 String note) {
    public static PriceLockPrintData from(PriceLock lock) {
        return new PriceLockPrintData(lock.getId(), lock.getSupplier().getId(),
                lock.getSupplier().getName(), lock.getItem().getId(), lock.getItem().getName(),
                lock.getItem().getCategory(), lock.getLockedVolume(), lock.getLockedPricePerKg(),
                lock.getAmount(), lock.getStatus().name(), lock.getLockDate(), lock.getNote());
    }
}
