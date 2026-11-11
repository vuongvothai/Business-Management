package com.businessmanagement.inventory.dto;

import com.businessmanagement.inventory.entity.ReconciliationConfirmation;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReconciliationConfirmationResponse(Long id, String confirmedBy,
        LocalDateTime confirmedAt, String note, BigDecimal supplierDiscrepancyKg,
        BigDecimal inventoryDiscrepancyKg) {
    public static ReconciliationConfirmationResponse from(ReconciliationConfirmation confirmation) {
        return new ReconciliationConfirmationResponse(confirmation.getId(),
                confirmation.getConfirmedBy().getUsername(), confirmation.getConfirmedAt(),
                confirmation.getNote(), confirmation.getSupplierDiscrepancyKg(),
                confirmation.getInventoryDiscrepancyKg());
    }
}
