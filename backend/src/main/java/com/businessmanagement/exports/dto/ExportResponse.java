package com.businessmanagement.exports.dto;

import com.businessmanagement.exports.entity.ExportTicket;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ExportResponse(Long id, Long customerId, String customerName,
                             Long itemId, String itemName, BigDecimal committedVolume,
                             BigDecimal sellingPricePerKg, BigDecimal revenue,
                             LocalDate exportDate, LocalDate deliveryDueDate,
                             BigDecimal deliveredVolume, BigDecimal remainingVolume,
                             String note, String status, List<DeliveryResponse> deliveries) {
    public static ExportResponse from(ExportTicket ticket, BigDecimal deliveredVolume) {
        return new ExportResponse(ticket.getId(), ticket.getCustomer().getId(),
                ticket.getCustomer().getName(), ticket.getItem().getId(),
                ticket.getItem().getName(), ticket.getCommittedVolume(),
                ticket.getSellingPricePerKg(), ticket.getRevenue(), ticket.getExportDate(),
                ticket.getDeliveryDueDate(), deliveredVolume,
                ticket.getCommittedVolume().subtract(deliveredVolume), ticket.getNote(),
                ticket.getStatus().name(),
                ticket.getDeliveries().stream().map(DeliveryResponse::from).toList());
    }
}
