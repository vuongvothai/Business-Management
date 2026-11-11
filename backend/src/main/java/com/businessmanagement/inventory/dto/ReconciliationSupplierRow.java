package com.businessmanagement.inventory.dto;

import java.math.BigDecimal;

public record ReconciliationSupplierRow(Long supplierId, String supplierName,
                                        BigDecimal importedKg, BigDecimal lockedKg,
                                        BigDecimal expectedUsableKg, BigDecimal actualUsableKg,
                                        BigDecimal discrepancyKg) { }
