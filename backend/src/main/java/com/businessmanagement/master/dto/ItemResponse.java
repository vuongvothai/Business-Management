package com.businessmanagement.master.dto;

import com.businessmanagement.master.entity.Item;
import java.math.BigDecimal;

public record ItemResponse(
        Long itemId,
        String name,
        String category,
        String unit,
        BigDecimal actualStockKg,
        BigDecimal standardStockKg
) {
    public static ItemResponse from(Item i) {
        return new ItemResponse(i.getItemId(), i.getName(), i.getCategory(),
                i.getUnit(), i.getActualStockKg(), i.getStandardStockKg());
    }
}