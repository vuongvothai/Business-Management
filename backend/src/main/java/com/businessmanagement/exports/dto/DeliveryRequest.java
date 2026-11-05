package com.businessmanagement.exports.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DeliveryRequest(
        @NotNull @DecimalMin("0.001") BigDecimal deliveredVolume,
        @NotNull LocalDate deliveryDate,
        @Size(max = 1000) String note,
        Long createdByUserId) {
}
