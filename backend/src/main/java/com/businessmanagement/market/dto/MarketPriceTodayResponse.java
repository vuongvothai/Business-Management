package com.businessmanagement.market.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record MarketPriceTodayResponse(LocalDate priceDate, List<ItemPrice> prices) {
    public record ItemPrice(Long itemId, String itemName, String status, BigDecimal pricePerKg,
                            String source, LocalDateTime fetchedAt) { }
}
