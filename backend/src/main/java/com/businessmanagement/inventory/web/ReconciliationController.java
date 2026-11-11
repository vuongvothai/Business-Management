package com.businessmanagement.inventory.web;

import com.businessmanagement.inventory.dto.*;
import com.businessmanagement.inventory.service.ReconciliationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory/reconciliation")
public class ReconciliationController {
    private final ReconciliationService service;
    public ReconciliationController(ReconciliationService service) { this.service = service; }

    @GetMapping
    public ReconciliationReportResponse report() { return service.report(); }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.CREATED)
    public ReconciliationConfirmationResponse confirm(@Valid @RequestBody ReconciliationConfirmationRequest request) {
        return service.confirm(request);
    }
}
