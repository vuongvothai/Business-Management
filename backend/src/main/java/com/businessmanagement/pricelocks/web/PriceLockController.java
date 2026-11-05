package com.businessmanagement.pricelocks.web;

import com.businessmanagement.pricelocks.dto.PriceLockRequest;
import com.businessmanagement.pricelocks.dto.PriceLockResponse;
import com.businessmanagement.pricelocks.service.PriceLockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/price-locks")
public class PriceLockController {

    private final PriceLockService service;

    public PriceLockController(PriceLockService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PriceLockResponse create(@Valid @RequestBody PriceLockRequest request) {
        return service.create(request);
    }
}
