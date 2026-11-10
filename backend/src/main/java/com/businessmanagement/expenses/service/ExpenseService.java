package com.businessmanagement.expenses.service;

import com.businessmanagement.expenses.dto.ExpenseDetailRequest;
import com.businessmanagement.expenses.dto.ExpenseRequest;
import com.businessmanagement.expenses.dto.ExpenseResponse;
import com.businessmanagement.expenses.entity.Expense;
import com.businessmanagement.expenses.entity.ExpenseDetail;
import com.businessmanagement.expenses.repository.ExpenseRepository;
import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final AppUserRepository appUserRepository;

    public ExpenseService(ExpenseRepository expenseRepository, AppUserRepository appUserRepository) {
        this.expenseRepository = expenseRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public ExpenseResponse create(ExpenseRequest request) {
        AppUser user = resolveUser(request.createdByUserId());
        validateCategories(request.details());
        Expense expense = new Expense(request.periodStart(), request.periodEnd(), request.note(), user);
        request.details().forEach(detail -> expense.addDetail(new ExpenseDetail(detail.categoryName().trim(), detail.amount())));
        return ExpenseResponse.from(expenseRepository.save(expense));
    }

    @Transactional
    public ExpenseResponse get(Long id) {
        return ExpenseResponse.from(find(id));
    }

    @Transactional
    public List<ExpenseResponse> list() {
        return list(null, null);
    }

    @Transactional
    public List<ExpenseResponse> list(java.time.LocalDate fromDate, java.time.LocalDate toDate) {
        if ((fromDate == null) != (toDate == null)) {
            throw new IllegalArgumentException("fromDate and toDate must be provided together");
        }
        if (fromDate != null && toDate.isBefore(fromDate)) {
            throw new IllegalArgumentException("toDate must not be before fromDate");
        }
        List<Expense> expenses = fromDate == null
                ? expenseRepository.findAll()
                : expenseRepository.findByPeriodStartLessThanEqualAndPeriodEndGreaterThanEqual(toDate, fromDate);
        return expenses.stream().map(ExpenseResponse::from).toList();
    }

    @Transactional
    public ExpenseResponse update(Long id, ExpenseRequest request) {
        validateCategories(request.details());
        Expense expense = find(id);
        AppUser updater = resolveUser(request.createdByUserId());
        expense.replaceDetails(request.periodStart(), request.periodEnd(), request.note(), updater);
        request.details().forEach(detail -> expense.addDetail(new ExpenseDetail(detail.categoryName().trim(), detail.amount())));
        return ExpenseResponse.from(expenseRepository.save(expense));
    }

    @Transactional
    public void delete(Long id) {
        if (!expenseRepository.existsById(id)) throw notFound(id);
        expenseRepository.deleteById(id);
    }

    private void validateCategories(List<ExpenseDetailRequest> details) {
        Set<String> categories = new HashSet<>();
        for (ExpenseDetailRequest detail : details) {
            if (!categories.add(detail.categoryName().trim().toLowerCase())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Duplicate expense category: " + detail.categoryName());
            }
        }
    }

    private Expense find(Long id) {
        return expenseRepository.findById(id).orElseThrow(() -> notFound(id));
    }

    private AppUser resolveUser(Long id) {
        if (id != null) return appUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
        return appUserRepository.findTopByOrderByIdAsc().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No application user is configured"));
    }

    private ResponseStatusException notFound(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found: " + id);
    }
}
