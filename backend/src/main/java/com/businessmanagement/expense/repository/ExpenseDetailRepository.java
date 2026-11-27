package com.businessmanagement.expense.repository;

import com.businessmanagement.expense.entity.ExpenseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseDetailRepository extends JpaRepository<ExpenseDetail, Long> {
    List<ExpenseDetail> findByExpenseId(Long expenseId);
}