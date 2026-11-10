package com.businessmanagement.imports.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ImportDeductionCalculatorTest {
    @Test void moistureBelow15HasNoDeduction() {
        assertThat(ImportDeductionCalculator.moistureDeductionPercent(new BigDecimal("14.9"))).isZero();
    }

    @Test void moisture150UsesFirstTableCell() {
        assertThat(ImportDeductionCalculator.moistureDeductionPercent(new BigDecimal("15.0")))
                .isEqualByComparingTo("0.00");
    }

    @Test void moisture151UsesApprovedTableCell() {
        assertThat(ImportDeductionCalculator.moistureDeductionPercent(new BigDecimal("15.1")))
                .isEqualByComparingTo("0.10");
    }

    @Test void moisture161UsesApprovedTableCell() {
        assertThat(ImportDeductionCalculator.moistureDeductionPercent(new BigDecimal("16.1")))
                .isEqualByComparingTo("1.24");
    }

    @Test void moisture250UsesLastTableCell() {
        assertThat(ImportDeductionCalculator.moistureDeductionPercent(new BigDecimal("25.0")))
                .isEqualByComparingTo("19.47");
    }

    @Test void nullMoistureIsInvalidWhenDeductionInputsAreUsed() {
        assertThatThrownBy(() -> ImportDeductionCalculator.totalDeductionKg(
                new BigDecimal("100"), 2, new BigDecimal("0.5"), null, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test void appliesExactFormulaWithoutDividingBy100OrAddingTare() {
        assertThat(ImportDeductionCalculator.totalDeductionKg(
                new BigDecimal("100"), 2, new BigDecimal("0.5"),
                new BigDecimal("15.1"), new BigDecimal("2")))
                .isEqualByComparingTo("310.00");
    }

    @Test void rejectsMoistureWithMoreThanOneDecimal() {
        assertThatThrownBy(() -> ImportDeductionCalculator.moistureDeductionPercent(new BigDecimal("15.00")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test void calculatesTotalAmount() {
        assertThat(ImportDeductionCalculator.totalAmount(new BigDecimal("12.345"), new BigDecimal("10.00")))
                .isEqualByComparingTo("123.45");
    }

    @Test void rejectsMoistureOutsideTable() {
        assertThatThrownBy(() -> ImportDeductionCalculator.moistureDeductionPercent(new BigDecimal("25.1")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
