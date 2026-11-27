package com.businessmanagement.loan.web;

import com.businessmanagement.loan.dto.*;
import com.businessmanagement.loan.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loan")
public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @GetMapping("/contracts")
    public List<LoanContractResponse> listContracts(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String partyType,
            @RequestParam(required = false) Long partyId) {
        return service.listContracts(status, partyType, partyId);
    }

    @PostMapping("/contracts")
    @ResponseStatus(HttpStatus.CREATED)
    public LoanContractResponse createContract(@Valid @RequestBody LoanContractRequest req) {
        return service.createContract(req);
    }

    @GetMapping("/transactions")
    public List<LoanTransactionResponse> listTransactions(@RequestParam Long loanId) {
        return service.listTransactions(loanId);
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public LoanTransactionResponse createTransaction(@Valid @RequestBody LoanTransactionRequest req) {
        return service.createTransaction(req);
    }
}