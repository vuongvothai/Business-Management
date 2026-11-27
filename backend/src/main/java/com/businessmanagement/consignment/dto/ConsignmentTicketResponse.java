package com.businessmanagement.consignment.dto;

import com.businessmanagement.consignment.entity.ConsignmentTicket;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsignmentTicketResponse(
        Long consignmentId,
        Long customerId,
        Long itemId,
        Long warehouseId,
        int numberOfBags,
        BigDecimal actualWeightKg,
        BigDecimal standardWeightKg,
        LocalDate consignmentDate,
        String status
) {
    public static ConsignmentTicketResponse from(ConsignmentTicket t) {
        return new ConsignmentTicketResponse(
                t.getConsignmentId(), t.getCustomerId(), t.getItemId(),
                t.getWarehouseId(), t.getNumberOfBags(), t.getActualWeightKg(),
                t.getStandardWeightKg(), t.getConsignmentDate(), t.getStatus());
    }
}