package com.businessmanagement.expenses.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.entity.Role;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ExpenseTest {

    @Test
    void addingDetailsAssignsParentAndRecalculatesTotal() {
        AppUser user = new AppUser("System administrator", new Role("ADMIN"));
        Expense expense = new Expense(
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30), "November", user);
        ExpenseDetail utilities = new ExpenseDetail("Utilities", new BigDecimal("125000.50"));
        ExpenseDetail transport = new ExpenseDetail("Transport", new BigDecimal("75000.25"));

        expense.addDetail(utilities);
        expense.addDetail(transport);

        assertThat(expense.getDetails()).containsExactly(utilities, transport);
        assertThat(utilities.getExpense()).isSameAs(expense);
        assertThat(transport.getExpense()).isSameAs(expense);
        assertThat(expense.getTotalAmount()).isEqualByComparingTo("200000.75");
    }

    @Test
    void lifecycleCallbacksPopulateAuditTimestamps() {
        Expense expense = new Expense(
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30), null,
                new AppUser("System administrator", new Role("ADMIN")));

        expense.onCreate();

        assertThat(expense.getCreatedAt()).isNotNull();
        assertThat(expense.getUpdatedAt()).isNotNull();
    }
}
