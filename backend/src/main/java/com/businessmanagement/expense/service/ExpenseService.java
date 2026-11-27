package com.businessmanagement.expense.service;

import com.businessmanagement.expense.dto.*;
import com.businessmanagement.expense.entity.*;
import com.businessmanagement.expense.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseDetailRepository detailRepository;
    private final ExpenseCategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
                          ExpenseDetailRepository detailRepository,
                          ExpenseCategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.detailRepository = detailRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ExpenseResponse create(ExpenseRequest req) {
        Expense e = new Expense();
        e.setPaymentMethod(req.paymentMethod() != null ? req.paymentMethod() : "CASH");
        e.setTotalAmount(req.totalAmount());
        e.setPeriodStart(req.periodStart());
        e.setPeriodEnd(req.periodEnd());
        e.setNote(req.note());
        e.setCreatedBy(req.createdBy());
        Expense saved = expenseRepository.save(e);

        if (req.details() != null) {
            for (ExpenseRequest.DetailRequest dr : req.details()) {
                ExpenseDetail d = new ExpenseDetail();
                d.setExpenseId(saved.getExpenseId());
                d.setCategoryId(dr.categoryId());
                d.setAmount(dr.amount());
                d.setNote(dr.note());
                detailRepository.save(d);
            }
        }
        return ExpenseResponse.from(saved);
    }

    public List<ExpenseResponse> list(String status) {
        List<Expense> all = (status != null && !status.isBlank())
                ? expenseRepository.findByStatusOrderByPeriodStartDesc(status)
                : expenseRepository.findAll();
        return all.stream().map(ExpenseResponse::from).toList();
    }

    public List<ExpenseCategoryResponse> listCategories() {
        return categoryRepository.findByActiveTrueOrderByCategoryName()
                .stream().map(ExpenseCategoryResponse::from).toList();
    }
}