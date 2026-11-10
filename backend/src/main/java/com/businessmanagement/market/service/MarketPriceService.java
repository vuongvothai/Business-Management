package com.businessmanagement.market.service;

import com.businessmanagement.market.dto.MarketPriceTodayResponse;
import com.businessmanagement.market.entity.MarketPrice;
import com.businessmanagement.market.repository.MarketPriceRepository;
import com.businessmanagement.master.repository.ItemRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class MarketPriceService {
    private final MarketPriceRepository prices;
    private final ItemRepository items;
    public MarketPriceService(MarketPriceRepository prices, ItemRepository items) { this.prices = prices; this.items = items; }
    @Transactional
    public MarketPriceTodayResponse today() {
        LocalDate today = LocalDate.now();
        Map<Long, MarketPrice> byItem = new HashMap<>();
        for (MarketPrice price : prices.findByPriceDate(today)) byItem.put(price.getItem().getId(), price);
        var result = items.findAll().stream().map(item -> {
            MarketPrice price = byItem.get(item.getId());
            return new MarketPriceTodayResponse.ItemPrice(item.getId(), item.getName(), price == null ? "MISSING" : "AVAILABLE",
                    price == null ? null : price.getPricePerKg(), price == null ? null : price.getSource(), price == null ? null : price.getFetchedAt());
        }).toList();
        return new MarketPriceTodayResponse(today, result);
    }
}
