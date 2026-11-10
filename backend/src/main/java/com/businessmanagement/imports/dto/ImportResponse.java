package com.businessmanagement.imports.dto;

import com.businessmanagement.imports.entity.ImportTicket;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
        BigDecimal totalAmount,
        BigDecimal totalDeductionAmount,
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
                null, null, importDate, numberOfBags, weights, totalKg, null, null, moisturePercent,
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
                cumulativeWeights(ticket),
                ticket.getTotalKg(),
                ticket.getTotalAmount(),
                ticket.getTotalDeductionAmount(),
                ticket.getMoisturePercent(),
                ticket.getImpurityPercent(),
                ticket.getPhotoUrl(),
                ticket.getNote(),
                ticket.getStatus().name());
    }

    private static List<ImportWeightResponse> cumulativeWeights(ImportTicket ticket) {
        List<ImportWeightResponse> result = new ArrayList<>();
        int bags = 0;
        BigDecimal weightKg = BigDecimal.ZERO;
        for (var weight : ticket.getWeights()) {
            bags += weight.getBagCount();
            weightKg = weightKg.add(weight.getWeightKg());
            result.add(ImportWeightResponse.from(weight, bags, weightKg));
        }
        return result;
    }
}
