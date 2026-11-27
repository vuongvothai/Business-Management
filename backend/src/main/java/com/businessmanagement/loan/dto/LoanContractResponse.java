package com.businessmanagement.loan.dto;

import com.businessmanagement.loan.entity.LoanContract;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanContractResponse(
        Long loanId,
        String code,
        String loanType,
        String partyType,
        Long partyId,
        BigDecimal principalAmount,
        BigDecimal interestRate,
        LocalDate startDate,
        LocalDate dueDate,
        BigDecimal outstandingPrincipal,
        String status
) {
    public static LoanContractResponse from(LoanContract l) {
        return new LoanContractResponse(
                l.getLoanId(), l.getCode(), l.getLoanType(),
                l.getPartyType(), l.getPartyId(),
                l.getPrincipalAmount(), l.getInterestRate(),
                l.getStartDate(), l.getDueDate(),
                l.getOutstandingPrincipal(), l.getStatus());
    }
}