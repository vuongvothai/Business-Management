package com.businessmanagement.inventory.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventoryTransactionRequest(
        @NotNull Long warehouseId,
        @NotNull Long itemId,
        @NotNull String transactionType,
        @NotNull String refType,
        @NotNull Long refId,
        BigDecimal actualInKg,
        BigDecimal actualOutKg,
        BigDecimal standardInKg,
        BigDecimal standardOutKg,
        BigDecimal unitCost,
        @NotNull LocalDateTime transactionDate,
        @NotNull Long createdBy
) {}