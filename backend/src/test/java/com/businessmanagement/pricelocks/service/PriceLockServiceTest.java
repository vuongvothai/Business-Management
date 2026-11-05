package com.businessmanagement.pricelocks.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.entity.Item;
import com.businessmanagement.master.entity.Role;
import com.businessmanagement.master.entity.Supplier;
import com.businessmanagement.master.repository.AppUserRepository;
import com.businessmanagement.master.repository.ItemRepository;
import com.businessmanagement.master.repository.PriceLockRepository;
import com.businessmanagement.master.repository.SupplierRepository;
import com.businessmanagement.pricelocks.dto.PriceLockRequest;
import com.businessmanagement.pricelocks.dto.PriceLockResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class PriceLockServiceTest {

    @Mock
    private PriceLockRepository priceLockRepository;
    @Mock
    private SupplierRepository supplierRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private AppUserRepository appUserRepository;

    private PriceLockService service;
    private Supplier supplier;
    private Item item;
    private AppUser user;

    @BeforeEach
    void setUp() {
        service = new PriceLockService(
                priceLockRepository, supplierRepository, itemRepository, appUserRepository);
        supplier = new Supplier("Supplier", "0900000000");
        item = new Item("Green coffee", "Grade A", "KG");
        user = new AppUser("System administrator", new Role("ADMIN"));
        setId(supplier, 1L);
        setId(item, 2L);
        setId(user, 1L);
    }

    @Test
    void createCalculatesAmountAndDeductsSupplierBalance() {
        supplier.addUsableCoffeeKg(new BigDecimal("201"));
        when(supplierRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(supplier));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));
        when(appUserRepository.findTopByOrderByIdAsc()).thenReturn(Optional.of(user));
        when(priceLockRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PriceLockResponse response = service.create(new PriceLockRequest(
                1L, 2L, LocalDate.of(2026, 11, 6),
                new BigDecimal("50"), new BigDecimal("42000"), "test", null));

        assertThat(response.amount()).isEqualByComparingTo("2100000");
        assertThat(response.status()).isEqualTo("CONFIRMED");
        assertThat(supplier.getUsableCoffeeKg()).isEqualByComparingTo("151");
        verify(priceLockRepository).save(any());
    }

    @Test
    void createRejectsVolumeAboveSupplierBalance() {
        supplier.addUsableCoffeeKg(new BigDecimal("10"));
        when(supplierRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(supplier));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> service.create(new PriceLockRequest(
                1L, 2L, LocalDate.of(2026, 11, 6),
                new BigDecimal("11"), new BigDecimal("42000"), null, null)))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("PRICE_LOCK_EXCEEDED");
        assertThat(supplier.getUsableCoffeeKg()).isEqualByComparingTo("10");
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
