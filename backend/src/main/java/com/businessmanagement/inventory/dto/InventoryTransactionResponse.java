package com.businessmanagement.inventory.dto;

import com.businessmanagement.inventory.entity.InventoryTransaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventoryTransactionResponse(
        Long transactionId,
        Long warehouseId,
        Long itemId,
        String transactionType,
        String refType,
        Long refId,
        BigDecimal actualInKg,
        BigDecimal actualOutKg,
        BigDecimal standardInKg,
        BigDecimal standardOutKg,
        LocalDateTime transactionDate,
        String status
) {
    public static InventoryTransactionResponse from(InventoryTransaction t) {
        return new InventoryTransactionResponse(
                t.getTransactionId(), t.getWarehouseId(), t.getItemId(),
                t.getTransactionType(), t.getRefType(), t.getRefId(),
                t.getActualInKg(), t.getActualOutKg(),
                t.getStandardInKg(), t.getStandardOutKg(),
                t.getTransactionDate(), t.getStatus());
    }
}