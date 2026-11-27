package com.businessmanagement.weighticket.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class StandardWeightCalculator {

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    private StandardWeightCalculator() {}

    public static BigDecimal calculate(BigDecimal netWeightKg,
                                        BigDecimal moisturePct,
                                        BigDecimal impurityPct,
                                        BigDecimal moistureStd,
                                        BigDecimal impurityStd) {
        if (netWeightKg == null) return BigDecimal.ZERO;
        BigDecimal net = netWeightKg;
        if (moisturePct != null && moistureStd != null) {
            BigDecimal over = moisturePct.subtract(moistureStd).max(BigDecimal.ZERO);
            net = net.multiply(BigDecimal.ONE.subtract(over.divide(HUNDRED, 6, RoundingMode.HALF_UP)));
        }
        if (impurityPct != null && impurityStd != null) {
            BigDecimal over = impurityPct.subtract(impurityStd).max(BigDecimal.ZERO);
            net = net.multiply(BigDecimal.ONE.subtract(over.divide(HUNDRED, 6, RoundingMode.HALF_UP)));
        }
        return net.setScale(3, RoundingMode.HALF_UP);
    }
}