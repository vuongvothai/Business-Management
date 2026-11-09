package com.businessmanagement.expenses.repository;

import com.businessmanagement.expenses.entity.Expense;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Override
    @EntityGraph(attributePaths = "details")
    List<Expense> findAll();

    @Override
    @EntityGraph(attributePaths = "details")
    Optional<Expense> findById(Long id);
}
