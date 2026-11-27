package com.businessmanagement.sales.web;

import com.businessmanagement.sales.dto.*;
import com.businessmanagement.sales.service.SalesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final SalesService service;

    public SalesController(SalesService service) {
        this.service = service;
    }

    @GetMapping("/contracts")
    public List<SalesContractResponse> listContracts(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String contractType,
            @RequestParam(required = false) String status) {
        return service.listContracts(customerId, contractType, status);
    }

    @PostMapping("/contracts")
    @ResponseStatus(HttpStatus.CREATED)
    public SalesContractResponse createContract(@Valid @RequestBody SalesContractRequest req) {
        return service.createContract(req);
    }

    @GetMapping("/deliveries")
    public List<SalesDeliveryResponse> listDeliveries(@RequestParam Long contractId) {
        return service.listDeliveries(contractId);
    }

    @PostMapping("/deliveries")
    @ResponseStatus(HttpStatus.CREATED)
    public SalesDeliveryResponse createDelivery(@Valid @RequestBody SalesDeliveryRequest req) {
        return service.createDelivery(req);
    }
}