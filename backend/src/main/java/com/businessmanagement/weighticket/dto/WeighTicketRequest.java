package com.businessmanagement.weighticket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record WeighTicketRequest(
        @NotBlank String code,
        @NotBlank String direction,
        @NotNull LocalDateTime weighedAt,
        Long warehouseId,
        Long vehicleId,
        String truckLicensePlate,
        BigDecimal moisturePct,
        BigDecimal impurityPct,
        BigDecimal moistureStd,
        BigDecimal impurityStd,
        String sampleTestResult,
        String photoUrl,
        String note,
        @NotNull Long createdBy,
        List<LineRequest> lines
) {
    public record LineRequest(
            int lineNo,
            @NotNull BigDecimal weightKg,
            int bagCount,
            String bagType,
            BigDecimal tarePerBagKg
    ) {}
}