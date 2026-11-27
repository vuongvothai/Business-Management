package com.businessmanagement.weighticket.dto;

import com.businessmanagement.weighticket.entity.WeighTicket;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record WeighTicketResponse(
        Long weighTicketId,
        String code,
        String direction,
        LocalDateTime weighedAt,
        Long warehouseId,
        Long vehicleId,
        String truckLicensePlate,
        int totalBags,
        BigDecimal grossWeightKg,
        BigDecimal tareWeightKg,
        BigDecimal netWeightKg,
        BigDecimal moisturePct,
        BigDecimal impurityPct,
        BigDecimal standardWeightKg,
        String status,
        Long createdBy,
        List<LineResponse> lines
) {
    public record LineResponse(
            Long weighLineId,
            int lineNo,
            BigDecimal weightKg,
            int bagCount,
            String bagType,
            BigDecimal tarePerBagKg
    ) {}

    public static WeighTicketResponse from(WeighTicket t, List<LineResponse> lines) {
        return new WeighTicketResponse(
                t.getWeighTicketId(), t.getCode(), t.getDirection(), t.getWeighedAt(),
                t.getWarehouseId(), t.getVehicleId(), t.getTruckLicensePlate(),
                t.getTotalBags(), t.getGrossWeightKg(), t.getTareWeightKg(),
                t.getNetWeightKg(), t.getMoisturePct(), t.getImpurityPct(),
                t.getStandardWeightKg(), t.getStatus(), t.getCreatedBy(), lines
        );
    }
}