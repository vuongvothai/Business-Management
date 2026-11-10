package com.businessmanagement.market.web;

import com.businessmanagement.market.dto.MarketPriceTodayResponse;
import com.businessmanagement.market.dto.MarketPriceRequest;
import com.businessmanagement.market.dto.MarketPriceResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.businessmanagement.market.service.MarketPriceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/market-prices", "/api/market-price"})
public class MarketPriceController {
    private final MarketPriceService service;
    public MarketPriceController(MarketPriceService service) { this.service = service; }
    @PostMapping("/{itemId}/{priceDate}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    @ResponseStatus(HttpStatus.CREATED)
    public MarketPriceResponse create(@PathVariable Long itemId, @PathVariable LocalDate priceDate,
                                       @Valid @RequestBody MarketPriceRequest request) {
        return service.create(itemId, priceDate, request);
    }

    @PutMapping("/{itemId}/{priceDate}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public MarketPriceResponse update(@PathVariable Long itemId, @PathVariable LocalDate priceDate,
                                      @Valid @RequestBody MarketPriceRequest request) {
        return service.update(itemId, priceDate, request);
    }

    @GetMapping("/today")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public MarketPriceTodayResponse today() { return service.today(); }
}
