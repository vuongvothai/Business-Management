package com.businessmanagement.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.businessmanagement.common.web.GlobalExceptionHandler;
import com.businessmanagement.auth.service.JwtService;
import com.businessmanagement.exports.service.ExportService;
import com.businessmanagement.imports.dto.ImportPageResponse;
import com.businessmanagement.imports.dto.ImportResponse;
import com.businessmanagement.imports.dto.ImportWeightResponse;
import com.businessmanagement.imports.service.ImportService;
import com.businessmanagement.inventory.dto.InventorySummaryResponse;
import com.businessmanagement.inventory.web.InventorySummaryController;
import com.businessmanagement.inventory.service.InventorySummaryService;
import com.businessmanagement.pricelocks.dto.PriceLockResponse;
import com.businessmanagement.pricelocks.service.PriceLockService;
import com.businessmanagement.exports.web.ExportController;
import com.businessmanagement.imports.web.ImportController;
import com.businessmanagement.pricelocks.web.PriceLockController;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
        ImportController.class,
        PriceLockController.class,
        ExportController.class,
        InventorySummaryController.class
})
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class CoreControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImportService importService;

    @MockBean
    private PriceLockService priceLockService;

    @MockBean
    private ExportService exportService;

    @MockBean
    private InventorySummaryService inventorySummaryService;

    @MockBean
    private JwtService jwtService;

    @Test
    void createsImportAndReturnsCreatedResponse() throws Exception {
        when(importService.create(any())).thenReturn(importResponse());

        mockMvc.perform(post("/api/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "supplierId": 1,
                                  "itemId": 2,
                                  "importDate": "2026-11-06",
                                  "weights": [{"weightKg": 120.3, "bagCount": 2}]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.totalKg").value(120.3));
    }

    @Test
    void rejectsInvalidImportRequestWithUnifiedError() throws Exception {
        mockMvc.perform(post("/api/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "supplierId": 1,
                                  "itemId": 2,
                                  "importDate": "2026-11-06",
                                  "weights": [{"weightKg": 120.3, "bagCount": 3}]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details[0].field").value("weights[0].bagCount"));
    }

    @Test
    void listsImportsWithDateSupplierAndPaginationFilters() throws Exception {
        when(importService.list(any(), any(), eq(7L), any(Pageable.class)))
                .thenReturn(new ImportPageResponse(List.of(importResponse()), 1, 5, 6, 2));

        mockMvc.perform(get("/api/imports")
                        .param("fromDate", "2026-11-01")
                        .param("toDate", "2026-11-06")
                        .param("supplierId", "7")
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.totalElements").value(6));

        ArgumentCaptor<Pageable> pageable = ArgumentCaptor.forClass(Pageable.class);
        verify(importService).list(
                eq(LocalDate.of(2026, 11, 1)),
                eq(LocalDate.of(2026, 11, 6)),
                eq(7L),
                pageable.capture());
        org.junit.jupiter.api.Assertions.assertEquals(1, pageable.getValue().getPageNumber());
        org.junit.jupiter.api.Assertions.assertEquals(5, pageable.getValue().getPageSize());
    }

    @Test
    void createsPriceLockAndReturnsCreatedResponse() throws Exception {
        when(priceLockService.create(any())).thenReturn(new PriceLockResponse(
                55L, 1L, 2L, LocalDate.of(2026, 11, 6),
                new BigDecimal("50.000"), new BigDecimal("42000.00"),
                new BigDecimal("2100000.00"), null, "CONFIRMED"));

        mockMvc.perform(post("/api/price-locks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "supplierId": 1,
                                  "itemId": 2,
                                  "lockDate": "2026-11-06",
                                  "lockedVolume": 50,
                                  "lockedPricePerKg": 42000
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(55))
                .andExpect(jsonPath("$.amount").value(2100000));
    }

    @Test
    void returnsInventorySummary() throws Exception {
        when(inventorySummaryService.summarize()).thenReturn(new InventorySummaryResponse(
                new BigDecimal("101.000"), new BigDecimal("50.000"),
                new BigDecimal("51.000"), new BigDecimal("2100000.00"),
                List.of(), OffsetDateTime.parse("2026-11-06T01:00:00+07:00")));

        mockMvc.perform(get("/api/inventory/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalKg").value(101))
                .andExpect(jsonPath("$.unpricedKg").value(51));
    }

    private ImportResponse importResponse() {
        return new ImportResponse(
                101L, 1L, "Supplier", 2L, "Coffee",
                LocalDate.of(2026, 11, 6), 2,
                List.of(new ImportWeightResponse(new BigDecimal("120.3"), 2)),
                new BigDecimal("120.3"), null, null, null, null, "CONFIRMED");
    }
}
