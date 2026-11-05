package com.businessmanagement.inventory.dto;

import java.math.BigDecimal;

public record InventoryCategorySummary(
        String category,
        BigDecimal totalKg,
        BigDecimal valuedKg,
        BigDecimal unpricedKg,
        BigDecimal inventoryValue) {
}
