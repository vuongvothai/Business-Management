package com.businessmanagement.loan.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanContractRequest(
        @NotNull String code,
        @NotNull String loanType,
        @NotNull String partyType,
        @NotNull Long partyId,
        @NotNull BigDecimal principalAmount,
        @NotNull BigDecimal interestRate,
        @NotNull LocalDate startDate,
        LocalDate dueDate,
        @NotNull Long createdBy
) {}