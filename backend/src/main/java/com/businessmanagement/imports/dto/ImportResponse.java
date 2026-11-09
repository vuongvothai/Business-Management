package com.businessmanagement.imports.dto;

import com.businessmanagement.imports.entity.ImportTicket;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ImportResponse(
        Long id,
        Long supplierId,
        String supplierName,
        Long itemId,
        String itemName,
        Long vehicleId,
        String truckLicensePlate,
        String loadingWarehouse,
        String importWarehouse,
        BigDecimal tareWeight,
        String sampleTestResult,
        LocalDate importDate,
        Integer numberOfBags,
        List<ImportWeightResponse> weights,
        BigDecimal totalKg,
        BigDecimal moisturePercent,
        BigDecimal impurityPercent,
        String photoUrl,
        String note,
        String status) {

    public ImportResponse(Long id, Long supplierId, String supplierName, Long itemId,
                          String itemName, LocalDate importDate, Integer numberOfBags,
                          List<ImportWeightResponse> weights, BigDecimal totalKg,
                          BigDecimal moisturePercent, BigDecimal impurityPercent,
                          String photoUrl, String note, String status) {
        this(id, supplierId, supplierName, itemId, itemName, null, null, null, null,
                null, null, importDate, numberOfBags, weights, totalKg, moisturePercent,
                impurityPercent, photoUrl, note, status);
    }

    public static ImportResponse from(ImportTicket ticket) {
        return new ImportResponse(
                ticket.getId(),
                ticket.getSupplier().getId(),
                ticket.getSupplier().getName(),
                ticket.getItem().getId(),
                ticket.getItem().getName(),
                ticket.getVehicle() == null ? null : ticket.getVehicle().getId(),
                ticket.getTruckLicensePlate(), ticket.getLoadingWarehouse(), ticket.getImportWarehouse(),
                ticket.getTareWeight(), ticket.getSampleTestResult(),
                ticket.getImportDate(),
                ticket.getNumberOfBags(),
                ticket.getWeights().stream().map(ImportWeightResponse::from).toList(),
                ticket.getTotalKg(),
                ticket.getMoisturePercent(),
                ticket.getImpurityPercent(),
                ticket.getPhotoUrl(),
                ticket.getNote(),
                ticket.getStatus().name());
    }
}
