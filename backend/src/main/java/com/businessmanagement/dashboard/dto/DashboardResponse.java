package com.businessmanagement.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DashboardResponse(List<DailySummary> days, LocalDate fromDate, LocalDate toDate,
                                LocalDateTime generatedAt) {
    public record DailySummary(LocalDate date, BigDecimal revenue, BigDecimal grossProfit,
                               BigDecimal deliveredVolume, boolean grossProfitAvailable) { }
}
