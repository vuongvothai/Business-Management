package com.businessmanagement.sales.dto;

import com.businessmanagement.sales.entity.SalesContract;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SalesContractResponse(
        Long contractId,
        String contractCode,
        String contractType,
        Long customerId,
        Long itemId,
        BigDecimal committedVolumeKg,
        BigDecimal deliveredVolumeKg,
        BigDecimal remainingVolumeKg,
        BigDecimal unitPricePerKg,
        BigDecimal depositAmount,
        BigDecimal totalRevenue,
        BigDecimal estimatedProfit,
        LocalDate contractDate,
        LocalDate deliveryDeadline,
        String status
) {
    public static SalesContractResponse from(SalesContract c) {
        return new SalesContractResponse(
                c.getContractId(), c.getContractCode(), c.getContractType(),
                c.getCustomerId(), c.getItemId(), c.getCommittedVolumeKg(),
                c.getDeliveredVolumeKg(), c.getRemainingVolumeKg(),
                c.getUnitPricePerKg(), c.getDepositAmount(),
                c.getTotalRevenue(), c.getEstimatedProfit(),
                c.getContractDate(), c.getDeliveryDeadline(), c.getStatus());
    }
}