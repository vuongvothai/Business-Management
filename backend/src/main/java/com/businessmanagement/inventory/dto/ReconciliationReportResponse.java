package com.businessmanagement.inventory.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record ReconciliationReportResponse(
        OffsetDateTime generatedAt,
        BigDecimal supplierDiscrepancyKg,
        BigDecimal inventoryDiscrepancyKg,
        List<ReconciliationSupplierRow> suppliers,
        List<ReconciliationInventoryRow> inventory) { }
