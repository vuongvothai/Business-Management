package com.businessmanagement.market.service;

import com.businessmanagement.market.dto.MarketPriceTodayResponse;
import com.businessmanagement.market.dto.MarketPriceRequest;
import com.businessmanagement.market.dto.MarketPriceResponse;
import com.businessmanagement.master.entity.Item;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
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
    public MarketPriceResponse create(Long itemId, LocalDate priceDate, MarketPriceRequest request) {
        if (priceDate == null || priceDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "priceDate must not be in the future");
        }
        Item item = items.findById(itemId).orElseThrow(() -> notFound("Item", itemId));
        if (prices.findByItemIdAndPriceDate(itemId, priceDate).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "MarketPrice already exists for item and date");
        }
        MarketPrice price = new MarketPrice(item, priceDate, request.pricePerKg(),
                request.source(), timestamp(request));
        return MarketPriceResponse.from(prices.save(price));
    }

    @Transactional
    public MarketPriceResponse update(Long itemId, LocalDate priceDate, MarketPriceRequest request) {
        if (priceDate == null || priceDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "priceDate must not be in the future");
        }
        MarketPrice price = prices.findByItemIdAndPriceDate(itemId, priceDate)
                .orElseThrow(() -> notFound("MarketPrice", itemId + "/" + priceDate));
        price.update(request.pricePerKg(), request.source(), timestamp(request));
        return MarketPriceResponse.from(prices.save(price));
    }

    private java.time.LocalDateTime timestamp(MarketPriceRequest request) {
        return request.fetchedAt() == null ? java.time.LocalDateTime.now() : request.fetchedAt();
    }

    private ResponseStatusException notFound(String resource, Object id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, resource + " not found: " + id);
    }

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
