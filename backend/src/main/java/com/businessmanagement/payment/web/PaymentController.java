package com.businessmanagement.payment.web;

import com.businessmanagement.payment.dto.*;
import com.businessmanagement.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping("/vouchers")
    public List<PaymentVoucherResponse> list(
            @RequestParam(required = false) String voucherType,
            @RequestParam(required = false) String partyType,
            @RequestParam(required = false) Long partyId) {
        return service.list(voucherType, partyType, partyId);
    }

    @PostMapping("/vouchers")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentVoucherResponse create(@Valid @RequestBody PaymentVoucherRequest req) {
        return service.create(req);
    }

    @PostMapping("/vouchers/{id}/confirm")
    public PaymentVoucherResponse confirm(@PathVariable Long id) {
        return service.confirm(id);
    }

    @GetMapping("/debts")
    public List<DebtLedgerResponse> listDebt(
            @RequestParam String partyType,
            @RequestParam Long partyId) {
        return service.listDebt(partyType, partyId);
    }
}