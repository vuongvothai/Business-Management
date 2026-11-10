package com.businessmanagement.imports.dto;

import com.businessmanagement.imports.entity.ImportWeight;
import java.math.BigDecimal;

public record ImportWeightResponse(BigDecimal weightKg, Integer bagCount,
                                   Integer cumulativeBagCount, BigDecimal cumulativeWeightKg) {

    public ImportWeightResponse(BigDecimal weightKg, Integer bagCount) {
        this(weightKg, bagCount, null, null);
    }

    public static ImportWeightResponse from(ImportWeight weight, int cumulativeBagCount,
                                            BigDecimal cumulativeWeightKg) {
        return new ImportWeightResponse(weight.getWeightKg(), weight.getBagCount(),
                cumulativeBagCount, cumulativeWeightKg);
    }
}
