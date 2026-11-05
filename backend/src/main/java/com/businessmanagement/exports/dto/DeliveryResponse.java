package com.businessmanagement.exports.dto;

import com.businessmanagement.exports.entity.ExportDelivery;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DeliveryResponse(Long id, BigDecimal deliveredVolume,
                                LocalDate deliveryDate, String note, String status) {
    public static DeliveryResponse from(ExportDelivery delivery) {
        return new DeliveryResponse(delivery.getId(), delivery.getDeliveredVolume(),
                delivery.getDeliveryDate(), delivery.getNote(), delivery.getStatus().name());
    }
}
