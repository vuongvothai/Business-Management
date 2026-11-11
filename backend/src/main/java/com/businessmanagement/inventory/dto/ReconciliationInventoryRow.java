package com.businessmanagement.inventory.dto;

import java.math.BigDecimal;

public record ReconciliationInventoryRow(Long itemId, String itemName,
                                         BigDecimal importedKg, BigDecimal deliveredKg,
                                         BigDecimal expectedAvailableKg, BigDecimal actualAvailableKg,
                                         BigDecimal discrepancyKg) { }
