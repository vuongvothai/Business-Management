package com.businessmanagement.imports.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Applies the approved moisture/impurity/tare policy for an import ticket. */
public final class ImportDeductionCalculator {
    private static final BigDecimal FIFTEEN = new BigDecimal("15.00");
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private static final BigDecimal[][] MOISTURE_TABLE = {
            {d("0.00"), d("0.10"), d("0.20"), d("0.37"), d("0.47"), d("0.57"), d("0.67"), d("0.77"), d("0.88"), d("0.99")},
            {d("1.13"), d("1.24"), d("1.35"), d("1.46"), d("1.57"), d("1.68"), d("1.80"), d("1.92"), d("2.04"), d("2.16")},
            {d("2.31"), d("2.44"), d("2.57"), d("2.70"), d("2.83"), d("2.96"), d("3.10"), d("3.24"), d("3.38"), d("3.52")},
            {d("3.67"), d("3.81"), d("3.96"), d("4.11"), d("4.26"), d("4.41"), d("4.56"), d("4.71"), d("4.87"), d("5.03")},
            {d("5.20"), d("5.37"), d("5.54"), d("5.71"), d("5.83"), d("6.05"), d("6.23"), d("6.41"), d("6.59"), d("6.77")},
            {d("6.95"), d("7.13"), d("7.32"), d("7.51"), d("7.71"), d("7.91"), d("8.11"), d("8.31"), d("8.51"), d("8.71")},
            {d("8.91"), d("9.12"), d("9.34"), d("9.56"), d("9.73"), d("10.00"), d("10.22"), d("10.44"), d("10.67"), d("10.90")},
            {d("11.13"), d("11.36"), d("11.60"), d("11.85"), d("12.10"), d("12.35"), d("12.60"), d("12.85"), d("13.10"), d("13.36")},
            {d("13.62"), d("13.88"), d("14.15"), d("14.43"), d("14.71"), d("14.99"), d("15.27"), d("15.55"), d("15.84"), d("16.13")},
            {d("16.42"), d("16.70"), d("17.01"), d("17.31"), d("17.61"), d("17.91"), d("18.22"), d("18.53"), d("18.84"), d("19.15")},
            {d("19.47"), null, null, null, null, null, null, null, null, null}
    };

    private ImportDeductionCalculator() { }

    private static BigDecimal d(String value) {
        return new BigDecimal(value);
    }

    public static BigDecimal moistureDeductionPercent(BigDecimal moisture) {
        if (moisture == null) {
            throw new IllegalArgumentException("moisturePercent is required for deduction");
        }
        if (moisture.scale() != 1) {
            throw new IllegalArgumentException("moisturePercent must have exactly one decimal place");
        }
        if (moisture.compareTo(BigDecimal.ZERO) < 0 || moisture.compareTo(new BigDecimal("25.0")) > 0) {
            throw new IllegalArgumentException("moisturePercent is outside the deduction table");
        }
        if (moisture.compareTo(FIFTEEN) < 0) return BigDecimal.ZERO.setScale(2);
        int row = moisture.setScale(0, RoundingMode.FLOOR).intValue() - 15;
        int col = moisture.remainder(BigDecimal.ONE).movePointRight(1).intValue();
        if (row < 0 || row >= MOISTURE_TABLE.length || col < 0 || col >= 10) {
            throw new IllegalArgumentException("moisturePercent is outside the deduction table");
        }
        return MOISTURE_TABLE[row][col];
    }

    public static BigDecimal totalDeductionKg(BigDecimal totalKg, int numberOfBags,
                                               BigDecimal tareWeight, BigDecimal moisture,
                                               BigDecimal impurity) {
        if (totalKg == null || totalKg.compareTo(BigDecimal.ZERO) <= 0 || numberOfBags < 0) {
            throw new IllegalArgumentException("totalKg and numberOfBags must be valid");
        }
        if (tareWeight == null || tareWeight.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("tareWeight is required and cannot be negative");
        }
        if (impurity == null || impurity.compareTo(BigDecimal.ZERO) < 0 || impurity.compareTo(ONE_HUNDRED) > 0) {
            throw new IllegalArgumentException("impurityPercent is required and must be between 0 and 100");
        }
        BigDecimal percent = moistureDeductionPercent(moisture).add(impurity);
        BigDecimal tareDeduction = BigDecimal.valueOf(numberOfBags).multiply(tareWeight);
        return totalKg.multiply(percent.add(tareDeduction)).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal totalAmount(BigDecimal totalKg, BigDecimal purchasePricePerKg) {
        return purchasePricePerKg == null ? null : totalKg.multiply(purchasePricePerKg).setScale(2, RoundingMode.HALF_UP);
    }
}
