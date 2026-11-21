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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketPriceService {
    private final MarketPriceRepository prices;
    private final ItemRepository items;
    private final CoffeeMarketPriceFetcher marketPriceFetcher;
    @Autowired
    public MarketPriceService(MarketPriceRepository prices, ItemRepository items, CoffeeMarketPriceFetcher marketPriceFetcher) { this.prices = prices; this.items = items; this.marketPriceFetcher = marketPriceFetcher; }
    @Transactional
    public MarketPriceResponse create(Long itemId, LocalDate priceDate, MarketPriceRequest request) {
        if (priceDate == null || priceDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "priceDate must not be in the future");
        }
        if (request.pricePerKg().scale() > 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "pricePerKg supports at most 2 decimals");
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
        if (request.pricePerKg().scale() > 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "pricePerKg supports at most 2 decimals");
        }
        MarketPrice price = prices.findByItemIdAndPriceDate(itemId, priceDate)
                .orElseThrow(() -> notFound("MarketPrice", itemId + "/" + priceDate));
        price.update(request.pricePerKg(), request.source(), timestamp(request));
        return MarketPriceResponse.from(prices.save(price));
    }

    private java.time.LocalDateTime timestamp(MarketPriceRequest request) {
        java.time.LocalDateTime fetchedAt = request.fetchedAt() == null
                ? java.time.LocalDateTime.now() : request.fetchedAt();
        if (fetchedAt.isAfter(java.time.LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "fetchedAt must not be in the future");
        }
        return fetchedAt;
    }

    private ResponseStatusException notFound(String resource, Object id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, resource + " not found: " + id);
    }

    @Transactional
    public MarketPriceTodayResponse today() {
        LocalDate today = LocalDate.now();
        syncOnlinePrice(today);
        Map<Long, MarketPrice> byItem = new HashMap<>();
        for (MarketPrice price : prices.findByPriceDate(today)) byItem.put(price.getItem().getId(), price);
        var result = items.findAll().stream().map(item -> {
            MarketPrice price = byItem.get(item.getId());
            return new MarketPriceTodayResponse.ItemPrice(item.getId(), item.getName(), price == null ? "MISSING" : "AVAILABLE",
                    price == null ? null : price.getPricePerKg(), price == null ? null : price.getSource(), price == null ? null : price.getFetchedAt());
        }).toList();
        return new MarketPriceTodayResponse(today, result);
    }

    private void syncOnlinePrice(LocalDate priceDate) {
        if (marketPriceFetcher == null) {
            return;
        }
        BigDecimal onlinePrice = marketPriceFetcher.fetchAveragePrice();
        for (Item item : items.findAll()) {
            MarketPrice price = prices.findByItemIdAndPriceDate(item.getId(), priceDate)
                    .orElseGet(() -> new MarketPrice(item, priceDate, onlinePrice,
                            "giacaphe.com", java.time.LocalDateTime.now()));
            price.update(onlinePrice, "giacaphe.com", java.time.LocalDateTime.now());
            prices.save(price);
        }
    }
}
