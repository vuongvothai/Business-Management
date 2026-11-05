package com.businessmanagement.master.dto;

import com.businessmanagement.master.entity.Item;
import java.math.BigDecimal;

public record ItemResponse(
        Long id,
        String name,
        String category,
        String unit,
        BigDecimal availableKg) {

    public static ItemResponse from(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getName(),
                item.getCategory(),
                item.getUnit(),
                item.getAvailableKg());
    }
}
