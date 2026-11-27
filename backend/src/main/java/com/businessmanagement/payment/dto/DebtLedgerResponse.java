package com.businessmanagement.payment.dto;

import com.businessmanagement.payment.entity.DebtLedger;
import java.math.BigDecimal;

public record DebtLedgerResponse(
        Long debtId,
        String partyType,
        Long partyId,
        Long itemId,
        BigDecimal debitAmount,
        BigDecimal creditAmount,
        BigDecimal balanceAmount
) {
    public static DebtLedgerResponse from(DebtLedger d) {
        return new DebtLedgerResponse(
                d.getDebtId(), d.getPartyType(), d.getPartyId(),
                d.getItemId(), d.getDebitAmount(),
                d.getCreditAmount(), d.getBalanceAmount());
    }
}