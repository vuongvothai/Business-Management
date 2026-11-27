package com.businessmanagement.expense.repository;

import com.businessmanagement.expense.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
    List<ExpenseCategory> findByActiveTrueOrderByCategoryName();
}