package com.businessmanagement.inventory.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.businessmanagement.inventory.dto.*;
import com.businessmanagement.inventory.service.ReconciliationService;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReconciliationControllerTest {
    @Test
    void reportIsReadOnlyAndConfirmationDelegatesExplicitly() {
        ReconciliationService service = mock(ReconciliationService.class);
        ReconciliationController controller = new ReconciliationController(service);
        ReconciliationReportResponse report = new ReconciliationReportResponse(
                OffsetDateTime.now(), BigDecimal.ONE, BigDecimal.ZERO, List.of(), List.of());
        when(service.report()).thenReturn(report);
        assertThat(controller.report()).isSameAs(report);
        verify(service).report();
        ReconciliationConfirmationRequest request = new ReconciliationConfirmationRequest("reviewed");
        controller.confirm(request);
        verify(service).confirm(request);
        verifyNoMoreInteractions(service);
    }
}
