package com.businessmanagement.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.businessmanagement.common.web.GlobalExceptionHandler;
import com.businessmanagement.auth.service.JwtService;
import com.businessmanagement.dashboard.dto.DashboardResponse;
import com.businessmanagement.dashboard.service.DashboardService;
import com.businessmanagement.dashboard.web.DashboardController;
import com.businessmanagement.exports.dto.DeliveryPrintData;
import com.businessmanagement.exports.dto.DeliveryRequest;
import com.businessmanagement.exports.service.ExportService;
import com.businessmanagement.exports.web.ExportController;
import com.businessmanagement.market.dto.MarketPriceTodayResponse;
import com.businessmanagement.market.service.MarketPriceService;
import com.businessmanagement.market.web.MarketPriceController;
import com.businessmanagement.master.entity.Supplier;
import com.businessmanagement.master.repository.SupplierRepository;
import com.businessmanagement.master.web.SupplierController;
import com.businessmanagement.pricelocks.dto.PriceLockPrintData;
import com.businessmanagement.pricelocks.service.PriceLockService;
import com.businessmanagement.pricelocks.web.PriceLockController;
import com.businessmanagement.vehicles.entity.Vehicle;
import com.businessmanagement.vehicles.repository.VehicleRepository;
import com.businessmanagement.vehicles.web.VehicleController;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest({SupplierController.class, VehicleController.class, DashboardController.class,
        MarketPriceController.class, PriceLockController.class, ExportController.class})
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class BacklogControllerWebTest {

    @Autowired MockMvc mockMvc;
    @MockBean JwtService jwtService;
    @MockBean SupplierRepository supplierRepository;
    @MockBean VehicleRepository vehicleRepository;
    @MockBean DashboardService dashboardService;
    @MockBean MarketPriceService marketPriceService;
    @MockBean PriceLockService priceLockService;
    @MockBean ExportService exportService;

    @Test
    void validatesSupplierAndVehicleRequestsWithUnifiedErrors() throws Exception {
        mockMvc.perform(post("/api/suppliers").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"phone\":\"0900000000\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
        mockMvc.perform(post("/api/vehicles").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"licensePlate\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details[0].field").value("licensePlate"));
    }

    @Test
    void searchesSupplierByIdentityThenNormalizedPhone() throws Exception {
        Supplier supplier = new Supplier("Coffee Supplier", "0900000001");
        when(supplierRepository.findAllByIdentityNo("012345678901")).thenReturn(List.of(supplier));
        mockMvc.perform(get("/api/suppliers/search").param("identityNo", " 012345678901 ")
                        .param("phone", "+84 900 000 002"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Coffee Supplier"));
        verify(supplierRepository).findAllByIdentityNo("012345678901");

        when(supplierRepository.findAllByIdentityNo("missing")).thenReturn(List.of());
        when(supplierRepository.findAllByPhone("0900000002")).thenReturn(List.of(supplier));
        mockMvc.perform(get("/api/suppliers/search").param("identityNo", "missing")
                        .param("phone", "+84 900 000 002"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Coffee Supplier"));
    }

    @Test
    void returnsBadRequestWhenSupplierSearchHasNoCriteria() throws Exception {
        mockMvc.perform(get("/api/suppliers/search").param("identityNo", " ").param("phone", " "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("REQUEST_ERROR"));
    }

    @Test
    void listsVehiclesAndDeactivatesVehicle() throws Exception {
        Vehicle vehicle = new Vehicle("51A-12345");
        when(vehicleRepository.findByActiveTrueOrderByLicensePlateAsc()).thenReturn(List.of(vehicle));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        mockMvc.perform(get("/api/vehicles")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].licensePlate").value("51A-12345"));
        mockMvc.perform(delete("/api/vehicles/1")).andExpect(status().isNoContent());
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void returnsDashboardTotalsAndSupportsMissingGrossProfit() throws Exception {
        DashboardResponse response = new DashboardResponse(List.of(
                new DashboardResponse.DailySummary(LocalDate.of(2026, 11, 6),
                        new BigDecimal("100.00"), null, new BigDecimal("2.00"), false)),
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 6), java.time.LocalDateTime.now());
        when(dashboardService.summarize(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 6), 2L))
                .thenReturn(response);
        mockMvc.perform(get("/api/dashboard").param("fromDate", "2026-11-01")
                        .param("toDate", "2026-11-06").param("itemId", "2"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.days[0].revenue").value(100))
                .andExpect(jsonPath("$.days[0].grossProfitAvailable").value(false))
                .andExpect(jsonPath("$.days[0].grossProfit").doesNotExist());
    }

    @Test
    void returnsMarketPriceTodayIncludingUnpricedStatus() throws Exception {
        when(marketPriceService.today()).thenReturn(new MarketPriceTodayResponse(LocalDate.of(2026, 11, 6),
                List.of(new MarketPriceTodayResponse.ItemPrice(2L, "Coffee", "MISSING", null, null, null))));
        mockMvc.perform(get("/api/market-prices/today")).andExpect(status().isOk())
                .andExpect(jsonPath("$.prices[0].status").value("MISSING"))
                .andExpect(jsonPath("$.prices[0].pricePerKg").doesNotExist());
    }

    @Test
    void validatesDeliveryWarehouseFieldsAndForwardsThem() throws Exception {
        mockMvc.perform(post("/api/exports/8/deliveries").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"deliveredVolume\":4,\"deliveryDate\":\"2026-11-06\",\"loadingWarehouse\":\"Kho bốc A\",\"importWarehouse\":\"Kho giao B\"}"))
                .andExpect(status().isOk());
        org.mockito.ArgumentCaptor<DeliveryRequest> captor =
                org.mockito.ArgumentCaptor.forClass(DeliveryRequest.class);
        verify(exportService).deliver(org.mockito.ArgumentMatchers.eq(8L), captor.capture());
        org.junit.jupiter.api.Assertions.assertEquals("Kho bốc A", captor.getValue().loadingWarehouse());
        org.junit.jupiter.api.Assertions.assertEquals("Kho giao B", captor.getValue().importWarehouse());
    }

    @Test
    void rejectsOversizedDeliveryWarehouseField() throws Exception {
        String oversized = "x".repeat(256);
        mockMvc.perform(post("/api/exports/8/deliveries").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"deliveredVolume\":4,\"deliveryDate\":\"2026-11-06\",\"loadingWarehouse\":\"" + oversized + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void exposesPrintDataForPriceLockAndDelivery() throws Exception {
        when(priceLockService.printData(5L)).thenReturn(new PriceLockPrintData(5L, 1L, "Supplier", 2L,
                "Coffee", "Grade A", new BigDecimal("10.0"), new BigDecimal("20.0"),
                new BigDecimal("200.0"), "CONFIRMED", LocalDate.of(2026, 11, 6), "note"));
        when(exportService.deliveryPrintData(8L, 9L)).thenReturn(new DeliveryPrintData(8L, 9L, 3L,
                "Customer", 2L, "Coffee", "Grade A", new BigDecimal("10.0"), new BigDecimal("4.0"),
                new BigDecimal("30.0"), new BigDecimal("120.0"), "CONFIRMED", "CONFIRMED",
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 6), null, null, "WH", "DEST", "note"));
        mockMvc.perform(get("/api/price-locks/5/print-data")).andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(200));
        mockMvc.perform(get("/api/exports/8/deliveries/9/print-data")).andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveredVolumeKg").value(4))
                .andExpect(jsonPath("$.amount").value(120));
    }
}
