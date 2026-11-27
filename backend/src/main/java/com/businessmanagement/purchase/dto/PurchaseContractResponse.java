package com.businessmanagement.purchase.dto;

import com.businessmanagement.purchase.entity.PurchaseContract;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PurchaseContractResponse(
        Long purchaseContractId,
        String contractCode,
        Long supplierId,
        Long itemId,
        BigDecimal committedVolumeKg,
        BigDecimal deliveredVolumeKg,
        BigDecimal remainingVolumeKg,
        BigDecimal lockedPricePerKg,
        LocalDate contractDate,
        LocalDate deliveryDeadline,
        String status
) {
    public static PurchaseContractResponse from(PurchaseContract c) {
        return new PurchaseContractResponse(
                c.getPurchaseContractId(), c.getContractCode(), c.getSupplierId(),
                c.getItemId(), c.getCommittedVolumeKg(), c.getDeliveredVolumeKg(),
                c.getRemainingVolumeKg(), c.getLockedPricePerKg(),
                c.getContractDate(), c.getDeliveryDeadline(), c.getStatus());
    }
}