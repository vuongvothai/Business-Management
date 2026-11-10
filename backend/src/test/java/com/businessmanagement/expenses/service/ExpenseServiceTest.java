package com.businessmanagement.expenses.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.businessmanagement.expenses.repository.ExpenseRepository;
import com.businessmanagement.master.repository.AppUserRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {
    @Mock ExpenseRepository expenseRepository;
    @Mock AppUserRepository appUserRepository;
    @InjectMocks ExpenseService service;

    @Test
    void rejectsPartiallySpecifiedPeriod() {
        assertThatThrownBy(() -> service.list(LocalDate.of(2026, 11, 1), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("fromDate and toDate must be provided together");
        verifyNoInteractions(expenseRepository);
    }

    @Test
    void rejectsPeriodEndingBeforeStarting() {
        assertThatThrownBy(() -> service.list(LocalDate.of(2026, 11, 30), LocalDate.of(2026, 11, 1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("toDate must not be before fromDate");
        verifyNoInteractions(expenseRepository);
    }

    @Test
    void queriesExpensesThatOverlapInclusivePeriod() {
        LocalDate from = LocalDate.of(2026, 11, 1);
        LocalDate to = LocalDate.of(2026, 11, 30);
        when(expenseRepository.findByPeriodStartLessThanEqualAndPeriodEndGreaterThanEqual(to, from))
                .thenReturn(List.of());

        service.list(from, to);

        verify(expenseRepository).findByPeriodStartLessThanEqualAndPeriodEndGreaterThanEqual(to, from);
    }
}
