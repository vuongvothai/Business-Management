package com.businessmanagement.purchase.dto;

import com.businessmanagement.purchase.entity.ImportTicket;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ImportResponse(
        Long importId,
        Long weighTicketId,
        Long supplierId,
        Long itemId,
        Long warehouseId,
        Long purchaseContractId,
        int numberOfBags,
        BigDecimal actualWeightKg,
        BigDecimal standardWeightKg,
        BigDecimal purchasePricePerKg,
        BigDecimal deductionAmount,
        BigDecimal totalAmount,
        LocalDate importDate,
        String status
) {
    public static ImportResponse from(ImportTicket t) {
        return new ImportResponse(
                t.getImportId(), t.getWeighTicketId(), t.getSupplierId(),
                t.getItemId(), t.getWarehouseId(), t.getPurchaseContractId(),
                t.getNumberOfBags(), t.getActualWeightKg(), t.getStandardWeightKg(),
                t.getPurchasePricePerKg(), t.getDeductionAmount(),
                t.getTotalAmount(), t.getImportDate(), t.getStatus());
    }
}