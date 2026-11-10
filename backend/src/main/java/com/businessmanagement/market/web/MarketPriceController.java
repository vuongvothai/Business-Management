package com.businessmanagement.market.web;

import com.businessmanagement.market.dto.MarketPriceTodayResponse;
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
    @GetMapping("/today")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public MarketPriceTodayResponse today() { return service.today(); }
}
