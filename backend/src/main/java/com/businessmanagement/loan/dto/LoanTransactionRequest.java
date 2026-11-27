package com.businessmanagement.loan.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanTransactionRequest(
        @NotNull Long loanId,
        @NotNull String txnType,
        @NotNull BigDecimal amount,
        @NotNull LocalDate txnDate,
        int interestDays,
        String note,
        @NotNull Long createdBy
) {}