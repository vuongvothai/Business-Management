package com.businessmanagement.inventory.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record InventorySummaryResponse(
        BigDecimal totalKg,
        BigDecimal valuedKg,
        BigDecimal unpricedKg,
        BigDecimal totalValue,
        List<InventoryCategorySummary> byCategory,
        OffsetDateTime calculatedAt) {
}
