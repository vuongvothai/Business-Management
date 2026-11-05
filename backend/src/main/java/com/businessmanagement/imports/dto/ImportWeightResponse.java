package com.businessmanagement.imports.dto;

import com.businessmanagement.imports.entity.ImportWeight;
import java.math.BigDecimal;

public record ImportWeightResponse(BigDecimal weightKg, Integer bagCount) {

    public static ImportWeightResponse from(ImportWeight weight) {
        return new ImportWeightResponse(weight.getWeightKg(), weight.getBagCount());
    }
}
