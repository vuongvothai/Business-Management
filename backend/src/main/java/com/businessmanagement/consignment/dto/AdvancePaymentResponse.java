package com.businessmanagement.consignment.dto;

import com.businessmanagement.consignment.entity.AdvancePayment;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AdvancePaymentResponse(
        Long advanceId,
        Long customerId,
        Long consignmentId,
        BigDecimal advanceAmount,
        LocalDate advanceDate,
        BigDecimal monthlyInterestRatePct,
        int interestDays,
        BigDecimal interestAmount,
        BigDecimal totalRepayment,
        BigDecimal settledAmount,
        String status
) {
    public static AdvancePaymentResponse from(AdvancePayment a) {
        return new AdvancePaymentResponse(
                a.getAdvanceId(), a.getCustomerId(), a.getConsignmentId(),
                a.getAdvanceAmount(), a.getAdvanceDate(),
                a.getMonthlyInterestRatePct(), a.getInterestDays(),
                a.getInterestAmount(), a.getTotalRepayment(),
                a.getSettledAmount(), a.getStatus());
    }
}