package com.businessmanagement.inventory.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.businessmanagement.master.entity.Item;
import com.businessmanagement.master.repository.ItemRepository;
import com.businessmanagement.master.repository.PriceLockRepository;
import com.businessmanagement.master.repository.PriceLockSummary;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventorySummaryServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private PriceLockRepository priceLockRepository;

    @Test
    void summarizesPricedAndUnpricedInventoryByItemCategory() {
        Item item = new Item("Green coffee", "Grade A", "KG");
        item.addAvailableKg(new BigDecimal("100"));
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(priceLockRepository.summarizeByItem(null))
                .thenReturn(new PriceLockSummary(new BigDecimal("40"), new BigDecimal("1600000")));

        var response = new InventorySummaryService(itemRepository, priceLockRepository).summarize();

        assertThat(response.totalKg()).isEqualByComparingTo("100");
        assertThat(response.valuedKg()).isEqualByComparingTo("40");
        assertThat(response.unpricedKg()).isEqualByComparingTo("60");
        assertThat(response.totalValue()).isEqualByComparingTo("1600000");
        assertThat(response.byCategory()).singleElement().satisfies(summary -> {
            assertThat(summary.category()).isEqualTo("Grade A");
            assertThat(summary.valuedKg()).isEqualByComparingTo("40");
        });
    }

    @Test
    void doesNotCreateNegativeUnpricedInventoryWhenAvailableStockIsNegative() {
        Item item = new Item("Green coffee", "Grade A", "KG");
        item.addAvailableKg(new BigDecimal("-10"));
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(priceLockRepository.summarizeByItem(null))
                .thenReturn(new PriceLockSummary(new BigDecimal("40"), new BigDecimal("1600000")));

        var response = new InventorySummaryService(itemRepository, priceLockRepository).summarize();

        assertThat(response.totalKg()).isEqualByComparingTo("-10");
        assertThat(response.valuedKg()).isZero();
        assertThat(response.unpricedKg()).isZero();
        assertThat(response.totalValue()).isZero();
    }
}
