package com.businessmanagement.exports.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DeliveryRequest(
        @NotNull @DecimalMin("0.001") BigDecimal deliveredVolume,
        @NotNull LocalDate deliveryDate,
        Long vehicleId,
        @Size(max = 255) String loadingWarehouse,
        @Size(max = 255) String importWarehouse,
        @Size(max = 1000) String note,
        Long createdByUserId) {

    public DeliveryRequest(BigDecimal deliveredVolume, LocalDate deliveryDate,
                           String note, Long createdByUserId) {
        this(deliveredVolume, deliveryDate, null, null, null, note, createdByUserId);
    }

    public DeliveryRequest(BigDecimal deliveredVolume, LocalDate deliveryDate,
                           Long vehicleId, String note, Long createdByUserId) {
        this(deliveredVolume, deliveryDate, vehicleId, null, null, note, createdByUserId);
    }
}
