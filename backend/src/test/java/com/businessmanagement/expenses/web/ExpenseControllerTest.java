package com.businessmanagement.expenses.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.businessmanagement.expenses.dto.ExpenseDetailRequest;
import com.businessmanagement.expenses.dto.ExpenseRequest;
import com.businessmanagement.expenses.dto.ExpenseResponse;
import com.businessmanagement.expenses.service.ExpenseService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExpenseControllerTest {
    @Mock ExpenseService service;
    @InjectMocks ExpenseController controller;

    @Test
    void delegatesExpenseCrud() {
        ExpenseRequest request = new ExpenseRequest(LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 11, 30), "note", null,
                List.of(new ExpenseDetailRequest("Utilities", new BigDecimal("10.00"))));
        ExpenseResponse response = new ExpenseResponse(1L, new BigDecimal("10.00"),
                request.periodStart(), request.periodEnd(), "note", 1L, null, List.of());
        when(service.create(any())).thenReturn(response);
        when(service.get(1L)).thenReturn(response);
        when(service.list()).thenReturn(List.of(response));
        when(service.update(any(), any())).thenReturn(response);

        assertThat(controller.create(request)).isSameAs(response);
        assertThat(controller.get(1L)).isSameAs(response);
        assertThat(controller.list()).containsExactly(response);
        assertThat(controller.update(1L, request)).isSameAs(response);
        controller.delete(1L);
    }
}
