package com.businessmanagement.weighticket.web;

import com.businessmanagement.weighticket.dto.WeighTicketRequest;
import com.businessmanagement.weighticket.dto.WeighTicketResponse;
import com.businessmanagement.weighticket.service.WeighTicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/weigh-tickets")
public class WeighTicketController {

    private final WeighTicketService service;

    public WeighTicketController(WeighTicketService service) {
        this.service = service;
    }

    @GetMapping
    public List<WeighTicketResponse> list(@RequestParam(required = false) String status,
                                          @RequestParam(required = false) String direction) {
        return service.list(status, direction);
    }

    @GetMapping("/{id}")
    public WeighTicketResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WeighTicketResponse create(@Valid @RequestBody WeighTicketRequest req) {
        return service.create(req);
    }

    @PostMapping("/{id}/confirm")
    public WeighTicketResponse confirm(@PathVariable Long id) {
        return service.confirm(id);
    }
}