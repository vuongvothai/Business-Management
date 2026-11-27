package com.businessmanagement.sales.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SalesDeliveryRequest(
        @NotNull Long contractId,
        Long weighTicketId,
        Long warehouseId,
        Long vehicleId,
        String truckLicensePlate,
        @NotNull BigDecimal deliveredActualKg,
        @NotNull BigDecimal deliveredStandardKg,
        BigDecimal customerStandardKg,
        String excessHandling,
        @NotNull LocalDate deliveryDate,
        String note,
        @NotNull Long createdBy
) {}