package com.businessmanagement.market.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.businessmanagement.master.entity.Item;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class MarketPriceTest {

    @Test
    void storesItemPriceDateValueAndSource() {
        Item item = new Item("Green coffee", "Grade A", "KG");
        LocalDate date = LocalDate.of(2026, 11, 9);
        LocalDateTime fetchedAt = LocalDateTime.of(2026, 11, 9, 8, 30);

        MarketPrice price = new MarketPrice(
                item, date, new BigDecimal("42000.00"), "Manual", fetchedAt);

        assertThat(price.getItem()).isSameAs(item);
        assertThat(price.getPriceDate()).isEqualTo(date);
        assertThat(price.getPricePerKg()).isEqualByComparingTo("42000");
        assertThat(price.getSource()).isEqualTo("Manual");
        assertThat(price.getFetchedAt()).isEqualTo(fetchedAt);
    }
}
