package com.businessmanagement.sales.dto;

import com.businessmanagement.sales.entity.SalesDelivery;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SalesDeliveryResponse(
        Long deliveryId,
        Long contractId,
        Long weighTicketId,
        BigDecimal deliveredActualKg,
        BigDecimal deliveredStandardKg,
        BigDecimal customerStandardKg,
        BigDecimal discrepancyKg,
        LocalDate deliveryDate,
        String status
) {
    public static SalesDeliveryResponse from(SalesDelivery d) {
        return new SalesDeliveryResponse(
                d.getDeliveryId(), d.getContractId(), d.getWeighTicketId(),
                d.getDeliveredActualKg(), d.getDeliveredStandardKg(),
                d.getCustomerStandardKg(), d.getDiscrepancyKg(),
                d.getDeliveryDate(), d.getStatus());
    }
}