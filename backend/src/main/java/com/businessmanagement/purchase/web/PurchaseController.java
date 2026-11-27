package com.businessmanagement.purchase.web;

import com.businessmanagement.purchase.dto.*;
import com.businessmanagement.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    private final PurchaseService service;

    public PurchaseController(PurchaseService service) {
        this.service = service;
    }

    @GetMapping("/contracts")
    public List<PurchaseContractResponse> listContracts(
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) String status) {
        return service.listContracts(supplierId, status);
    }

    @PostMapping("/contracts")
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseContractResponse createContract(@Valid @RequestBody PurchaseContractRequest req) {
        return service.createContract(req);
    }

    @GetMapping("/imports")
    public List<ImportResponse> listImports(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long supplierId) {
        return service.listImports(status, supplierId);
    }

    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    public ImportResponse createImport(@Valid @RequestBody ImportRequest req) {
        return service.createImport(req);
    }

    @PostMapping("/imports/{id}/confirm")
    public ImportResponse confirmImport(@PathVariable Long id) {
        return service.confirmImport(id);
    }
}