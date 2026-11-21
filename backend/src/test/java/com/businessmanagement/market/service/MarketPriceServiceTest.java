package com.businessmanagement.market.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.businessmanagement.market.repository.MarketPriceRepository;
import com.businessmanagement.master.entity.Item;
import com.businessmanagement.master.repository.ItemRepository;
import org.mockito.Mock;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MarketPriceServiceTest {
    private CoffeeMarketPriceFetcher marketPriceFetcher = Mockito.mock(CoffeeMarketPriceFetcher.class);
    @Test
    void marksItemsMissingWhenNoPriceExistsToday() {
        MarketPriceRepository prices = Mockito.mock(MarketPriceRepository.class);
        ItemRepository items = Mockito.mock(ItemRepository.class);
        when(prices.findByPriceDate(LocalDate.now())).thenReturn(List.of());
        when(items.findAll()).thenReturn(List.of(new Item("Coffee", "Grade A", "KG")));
        var response = new MarketPriceService(prices, items, marketPriceFetcher).today();
        assertThat(response.prices()).singleElement().extracting("status").isEqualTo("MISSING");
    }

    @Test
    void createsManualPriceAndGeneratesTimestampWhenOmitted() {
        MarketPriceRepository prices = Mockito.mock(MarketPriceRepository.class);
        ItemRepository items = Mockito.mock(ItemRepository.class);
        Item item = new Item("Coffee", "Grade A", "KG");
        when(items.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(prices.findByItemIdAndPriceDate(1L, LocalDate.now())).thenReturn(java.util.Optional.empty());
        when(prices.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));
        var response = new MarketPriceService(prices, items, marketPriceFetcher).create(1L, LocalDate.now(),
                new com.businessmanagement.market.dto.MarketPriceRequest(new java.math.BigDecimal("42000"), "Manual", null));
        assertThat(response.fetchedAt()).isNotNull();
        assertThat(response.source()).isEqualTo("Manual");
    }

    @Test
    void rejectsDuplicateItemAndDate() {
        MarketPriceRepository prices = Mockito.mock(MarketPriceRepository.class);
        ItemRepository items = Mockito.mock(ItemRepository.class);
        when(items.findById(1L)).thenReturn(java.util.Optional.of(new Item("Coffee", "Grade A", "KG")));
        when(prices.findByItemIdAndPriceDate(1L, LocalDate.now())).thenReturn(java.util.Optional.of(Mockito.mock(com.businessmanagement.market.entity.MarketPrice.class)));
        assertThatThrownBy(() -> new MarketPriceService(prices, items, marketPriceFetcher).create(1L, LocalDate.now(),
                new com.businessmanagement.market.dto.MarketPriceRequest(new java.math.BigDecimal("42000"), "Manual", null)))
                .isInstanceOf(org.springframework.web.server.ResponseStatusException.class)
                .extracting("statusCode.value").isEqualTo(409);
    }
}
