package com.businessmanagement.consignment.web;

import com.businessmanagement.consignment.dto.*;
import com.businessmanagement.consignment.service.ConsignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/consignment")
public class ConsignmentController {

    private final ConsignmentService service;

    public ConsignmentController(ConsignmentService service) {
        this.service = service;
    }

    @GetMapping("/tickets")
    public List<ConsignmentTicketResponse> listTickets(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String status) {
        return service.listTickets(customerId, status);
    }

    @PostMapping("/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    public ConsignmentTicketResponse createTicket(@Valid @RequestBody ConsignmentTicketRequest req) {
        return service.createTicket(req);
    }

    @PostMapping("/tickets/{id}/confirm")
    public ConsignmentTicketResponse confirmTicket(@PathVariable Long id) {
        return service.confirmTicket(id);
    }

    @GetMapping("/balances")
    public List<ConsignmentBalanceResponse> listBalances(@RequestParam Long customerId) {
        return service.listBalances(customerId);
    }

    @PostMapping("/price-locks")
    @ResponseStatus(HttpStatus.CREATED)
    public ConsignmentPriceLockResponse createPriceLock(@Valid @RequestBody ConsignmentPriceLockRequest req) {
        return service.createPriceLock(req);
    }

    @PostMapping("/advances")
    @ResponseStatus(HttpStatus.CREATED)
    public AdvancePaymentResponse createAdvance(@Valid @RequestBody AdvancePaymentRequest req) {
        return service.createAdvance(req);
    }

    @PostMapping("/advances/{id}/settle")
    public AdvancePaymentResponse settleAdvance(@PathVariable Long id,
                                                 @RequestParam LocalDate settleDate) {
        return service.settleAdvance(id, settleDate);
    }
}