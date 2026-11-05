package com.businessmanagement.imports.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.businessmanagement.imports.dto.ImportRequest;
import com.businessmanagement.imports.dto.ImportResponse;
import com.businessmanagement.imports.dto.ImportWeightRequest;
import com.businessmanagement.imports.entity.ImportTicket;
import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.entity.Item;
import com.businessmanagement.master.entity.Role;
import com.businessmanagement.master.entity.Supplier;
import com.businessmanagement.master.repository.AppUserRepository;
import com.businessmanagement.master.repository.ImportTicketRepository;
import com.businessmanagement.master.repository.ItemRepository;
import com.businessmanagement.master.repository.SupplierRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ImportServiceTest {

    @Mock
    private ImportTicketRepository importRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private AppUserRepository appUserRepository;

    private ImportService service;
    private Supplier supplier;
    private Item item;
    private AppUser user;

    @BeforeEach
    void setUp() {
        service = new ImportService(importRepository, supplierRepository, itemRepository, appUserRepository);
        supplier = new Supplier("Supplier", "0900000000");
        item = new Item("Green coffee", "Grade A", "KG");
        user = new AppUser("System administrator", new Role("ADMIN"));
        setId(supplier, 1L);
        setId(item, 2L);
        setId(user, 1L);
    }

    @Test
    void createCalculatesTotalsAndUpdatesBalances() {
        when(supplierRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(supplier));
        when(itemRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(item));
        when(appUserRepository.findTopByOrderByIdAsc()).thenReturn(Optional.of(user));
        when(importRepository.save(any(ImportTicket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        ImportRequest request = new ImportRequest(
                1L, 2L, LocalDate.of(2026, 11, 5),
                java.util.List.of(
                        new ImportWeightRequest(new BigDecimal("120.300"), 2),
                        new ImportWeightRequest(new BigDecimal("80.700"), 1)),
                new BigDecimal("12.5"), new BigDecimal("1.8"), null, null, null, "note", null);

        ImportResponse response = service.create(request);

        assertThat(response.numberOfBags()).isEqualTo(3);
        assertThat(response.totalKg()).isEqualByComparingTo("201.000");
        assertThat(response.weights()).hasSize(2);
        assertThat(supplier.getUsableCoffeeKg()).isEqualByComparingTo("201.000");
        assertThat(item.getAvailableKg()).isEqualByComparingTo("201.000");
        verify(importRepository).save(any(ImportTicket.class));
    }

    @Test
    void createRejectsUnknownSupplier() {
        when(supplierRepository.findByIdForUpdate(99L)).thenReturn(Optional.empty());
        ImportRequest request = new ImportRequest(
                99L, 2L, LocalDate.of(2026, 11, 5),
                java.util.List.of(new ImportWeightRequest(new BigDecimal("10"), 1)),
                null, null, null, null, null, null, null);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Supplier not found: 99");
        assertThat(supplier.getUsableCoffeeKg()).isZero();
        assertThat(item.getAvailableKg()).isZero();
    }

    private static void setId(Object entity, Long id) {
        try {
            var field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError(exception);
        }
    }
}
