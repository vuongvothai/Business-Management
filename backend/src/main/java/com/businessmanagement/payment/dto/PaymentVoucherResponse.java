package com.businessmanagement.payment.dto;

import com.businessmanagement.payment.entity.PaymentVoucher;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentVoucherResponse(
        Long voucherId,
        String code,
        String voucherType,
        String partyType,
        Long partyId,
        String paymentMethod,
        BigDecimal amount,
        LocalDate voucherDate,
        String status
) {
    public static PaymentVoucherResponse from(PaymentVoucher v) {
        return new PaymentVoucherResponse(
                v.getVoucherId(), v.getCode(), v.getVoucherType(),
                v.getPartyType(), v.getPartyId(), v.getPaymentMethod(),
                v.getAmount(), v.getVoucherDate(), v.getStatus());
    }
}