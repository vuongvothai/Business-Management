package com.businessmanagement.dashboard.service;

import com.businessmanagement.dashboard.dto.DashboardResponse;
import com.businessmanagement.dashboard.repository.DashboardDeliveryRepository;
import com.businessmanagement.exports.entity.DeliveryStatus;
import com.businessmanagement.exports.entity.ExportDelivery;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DashboardService {
    private final DashboardDeliveryRepository repository;

    public DashboardService(DashboardDeliveryRepository repository) { this.repository = repository; }

    @Transactional
    public DashboardResponse summarize(LocalDate fromDate, LocalDate toDate, Long itemId) {
        if (fromDate == null || toDate == null || toDate.isBefore(fromDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fromDate/toDate không hợp lệ");
        }
        Map<LocalDate, BigDecimal[]> totals = new HashMap<>();
        for (ExportDelivery delivery : repository.findConfirmedBetween(fromDate, toDate, DeliveryStatus.CONFIRMED)) {
            if (itemId != null && !itemId.equals(delivery.getExportTicket().getItem().getId())) continue;
            BigDecimal volume = delivery.getDeliveredVolume();
            BigDecimal revenue = volume.multiply(delivery.getExportTicket().getSellingPricePerKg());
            BigDecimal[] total = totals.computeIfAbsent(delivery.getDeliveryDate(), ignored -> new BigDecimal[] {BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO});
            total[0] = total[0].add(volume);
            total[1] = total[1].add(revenue);
            if (delivery.getExportTicket().getAvgCostPerKg() != null) {
                total[2] = total[2].add(revenue.subtract(volume.multiply(delivery.getExportTicket().getAvgCostPerKg())));
                total[3] = total[3].add(volume);
            }
        }
        var days = new ArrayList<DashboardResponse.DailySummary>();
        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            BigDecimal[] t = totals.getOrDefault(date, new BigDecimal[] {BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO});
            boolean available = t[3].compareTo(t[0]) == 0;
            days.add(new DashboardResponse.DailySummary(date, t[1], available ? t[2] : null, t[0], available));
        }
        return new DashboardResponse(days, fromDate, toDate, LocalDateTime.now());
    }
}
