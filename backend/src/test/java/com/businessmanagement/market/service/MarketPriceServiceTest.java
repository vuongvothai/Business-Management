package com.businessmanagement.market.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.businessmanagement.market.repository.MarketPriceRepository;
import com.businessmanagement.master.entity.Item;
import com.businessmanagement.master.repository.ItemRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MarketPriceServiceTest {
    @Test
    void marksItemsMissingWhenNoPriceExistsToday() {
        MarketPriceRepository prices = Mockito.mock(MarketPriceRepository.class);
        ItemRepository items = Mockito.mock(ItemRepository.class);
        when(prices.findByPriceDate(LocalDate.now())).thenReturn(List.of());
        when(items.findAll()).thenReturn(List.of(new Item("Coffee", "Grade A", "KG")));
        var response = new MarketPriceService(prices, items).today();
        assertThat(response.prices()).singleElement().extracting("status").isEqualTo("MISSING");
    }
}
