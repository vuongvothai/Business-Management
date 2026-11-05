package com.businessmanagement.inventory.service;

import com.businessmanagement.inventory.dto.InventoryCategorySummary;
import com.businessmanagement.inventory.dto.InventorySummaryResponse;
import com.businessmanagement.master.entity.Item;
import com.businessmanagement.master.repository.ItemRepository;
import com.businessmanagement.master.repository.PriceLockRepository;
import com.businessmanagement.master.repository.PriceLockSummary;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InventorySummaryService {

    private final ItemRepository itemRepository;
    private final PriceLockRepository priceLockRepository;

    public InventorySummaryService(ItemRepository itemRepository,
                                   PriceLockRepository priceLockRepository) {
        this.itemRepository = itemRepository;
        this.priceLockRepository = priceLockRepository;
    }

    public InventorySummaryResponse summarize() {
        List<InventoryCategorySummary> byCategory = itemRepository.findAll().stream()
                .map(this::summarizeItem)
                .toList();
        BigDecimal totalKg = sum(byCategory.stream().map(InventoryCategorySummary::totalKg).toList());
        BigDecimal valuedKg = sum(byCategory.stream().map(InventoryCategorySummary::valuedKg).toList());
        BigDecimal unpricedKg = sum(byCategory.stream().map(InventoryCategorySummary::unpricedKg).toList());
        BigDecimal totalValue = sum(byCategory.stream()
                .map(InventoryCategorySummary::inventoryValue).toList());
        return new InventorySummaryResponse(
                totalKg, valuedKg, unpricedKg, totalValue, byCategory,
                OffsetDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
    }

    private InventoryCategorySummary summarizeItem(Item item) {
        BigDecimal totalKg = item.getAvailableKg();
        PriceLockSummary locked = priceLockRepository.summarizeByItem(item.getId());
        BigDecimal pricedVolume = locked.volume();
        BigDecimal valuedKg = totalKg.max(BigDecimal.ZERO).min(pricedVolume);
        BigDecimal unpricedKg = totalKg.subtract(valuedKg).max(BigDecimal.ZERO);
        BigDecimal inventoryValue = valuedKg.signum() == 0 || pricedVolume.signum() == 0
                ? BigDecimal.ZERO
                : valuedKg.multiply(locked.amount())
                        .divide(pricedVolume, 2, RoundingMode.HALF_UP);
        return new InventoryCategorySummary(
                item.getCategory(), totalKg, valuedKg, unpricedKg, inventoryValue);
    }

    private BigDecimal sum(List<BigDecimal> values) {
        return values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
