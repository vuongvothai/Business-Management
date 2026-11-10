package com.businessmanagement.exports.dto;

import com.businessmanagement.exports.entity.ExportDelivery;
import com.businessmanagement.exports.entity.ExportTicket;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DeliveryPrintData(Long exportId, Long deliveryId, Long customerId,
                                String customerName, Long itemId, String itemName,
                                String itemCategory, BigDecimal committedVolumeKg,
                                BigDecimal deliveredVolumeKg, BigDecimal sellingPricePerKg,
                                BigDecimal amount, String exportStatus, String deliveryStatus,
                                LocalDate exportDate, LocalDate deliveryDate,
                                Long vehicleId, String truckLicensePlate,
                                String loadingWarehouse, String importWarehouse,
                                String note) {
    public static DeliveryPrintData from(ExportDelivery delivery) {
        ExportTicket ticket = delivery.getExportTicket();
        return new DeliveryPrintData(ticket.getId(), delivery.getId(),
                ticket.getCustomer().getId(), ticket.getCustomer().getName(),
                ticket.getItem().getId(), ticket.getItem().getName(), ticket.getItem().getCategory(),
                ticket.getCommittedVolume(), delivery.getDeliveredVolume(),
                ticket.getSellingPricePerKg(), delivery.getDeliveredVolume().multiply(ticket.getSellingPricePerKg()),
                ticket.getStatus().name(), delivery.getStatus().name(), ticket.getExportDate(),
                delivery.getDeliveryDate(), delivery.getVehicle() == null ? null : delivery.getVehicle().getId(),
                delivery.getTruckLicensePlate(), delivery.getLoadingWarehouse(),
                delivery.getImportWarehouse(), delivery.getNote());
    }
}
