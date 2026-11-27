package com.businessmanagement.expense.web;

import com.businessmanagement.expense.dto.*;
import com.businessmanagement.expense.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @GetMapping
    public List<ExpenseResponse> list(@RequestParam(required = false) String status) {
        return service.list(status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse create(@Valid @RequestBody ExpenseRequest req) {
        return service.create(req);
    }

    @GetMapping("/categories")
    public List<ExpenseCategoryResponse> listCategories() {
        return service.listCategories();
    }
}