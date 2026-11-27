package com.businessmanagement.loan.dto;

import com.businessmanagement.loan.entity.LoanTransaction;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanTransactionResponse(
        Long loanTxnId,
        Long loanId,
        String txnType,
        BigDecimal amount,
        LocalDate txnDate,
        int interestDays
) {
    public static LoanTransactionResponse from(LoanTransaction t) {
        return new LoanTransactionResponse(
                t.getLoanTxnId(), t.getLoanId(),
                t.getTxnType(), t.getAmount(),
                t.getTxnDate(), t.getInterestDays());
    }
}