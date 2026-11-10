package com.businessmanagement.market.dto;

import com.businessmanagement.market.entity.MarketPrice;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record MarketPriceResponse(Long id, Long itemId, LocalDate priceDate,
                                   BigDecimal pricePerKg, String source,
                                   LocalDateTime fetchedAt) {
    public static MarketPriceResponse from(MarketPrice price) {
        return new MarketPriceResponse(price.getId(), price.getItem().getId(), price.getPriceDate(),
                price.getPricePerKg(), price.getSource(), price.getFetchedAt());
    }
}
