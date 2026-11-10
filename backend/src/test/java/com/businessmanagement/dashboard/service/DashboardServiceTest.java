package com.businessmanagement.dashboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.businessmanagement.dashboard.repository.DashboardDeliveryRepository;
import com.businessmanagement.exports.entity.DeliveryStatus;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DashboardServiceTest {
    @Test
    void returnsEveryDateAndZeroForMissingDates() {
        DashboardDeliveryRepository repository = Mockito.mock(DashboardDeliveryRepository.class);
        when(repository.findConfirmedBetween(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 3), DeliveryStatus.CONFIRMED)).thenReturn(List.of());
        var response = new DashboardService(repository).summarize(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 3), null);
        assertThat(response.days()).hasSize(3);
        assertThat(response.days()).allSatisfy(day -> assertThat(day.revenue()).isZero());
    }
}
