package com.businessmanagement.expense.repository;

import com.businessmanagement.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByStatusOrderByPeriodStartDesc(String status);
}