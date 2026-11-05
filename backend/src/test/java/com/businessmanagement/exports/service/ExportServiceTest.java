package com.businessmanagement.exports.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.businessmanagement.exports.dto.DeliveryRequest;
import com.businessmanagement.exports.dto.ExportRequest;
import com.businessmanagement.exports.dto.ExportResponse;
import com.businessmanagement.exports.entity.DeliveryStatus;
import com.businessmanagement.exports.entity.ExportTicket;
import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.entity.Customer;
import com.businessmanagement.master.entity.Item;
import com.businessmanagement.master.entity.Role;
import com.businessmanagement.master.repository.AppUserRepository;
import com.businessmanagement.master.repository.CustomerRepository;
import com.businessmanagement.master.repository.ExportDeliveryRepository;
import com.businessmanagement.master.repository.ExportTicketRepository;
import com.businessmanagement.master.repository.ItemRepository;
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
class ExportServiceTest {

    @Mock
    private ExportTicketRepository exportRepository;
    @Mock
    private ExportDeliveryRepository deliveryRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private AppUserRepository appUserRepository;

    private ExportService service;
    private Customer customer;
    private Item item;
    private AppUser user;

    @BeforeEach
    void setUp() {
        service = new ExportService(
                exportRepository, deliveryRepository, customerRepository,
                itemRepository, appUserRepository);
        customer = new Customer("Customer");
        item = new Item("Green coffee", "Grade A", "KG");
        user = new AppUser("System administrator", new Role("ADMIN"));
        setId(customer, 1L);
        setId(item, 2L);
        setId(user, 1L);
    }

    @Test
    void createDoesNotDeductInventory() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));
        when(appUserRepository.findTopByOrderByIdAsc()).thenReturn(Optional.of(user));
        when(exportRepository.save(any(ExportTicket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ExportResponse response = service.create(new ExportRequest(
                1L, 2L, new BigDecimal("100"), new BigDecimal("50000"),
                LocalDate.of(2026, 11, 6), null, "test", null));

        assertThat(response.deliveredVolume()).isZero();
        assertThat(response.remainingVolume()).isEqualByComparingTo("100");
        assertThat(item.getAvailableKg()).isZero();
    }

    @Test
    void deliverMultipleTimesCompletesTicketAndDeductsInventory() {
        customer = new Customer("Customer");
        ExportTicket ticket = new ExportTicket(
                customer, item, new BigDecimal("100"), new BigDecimal("50000"),
                LocalDate.of(2026, 11, 6), null, "test", user);
        setId(ticket, 1L);
        when(exportRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(ticket));
        when(itemRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(item));
        when(deliveryRepository.sumDelivered(1L, DeliveryStatus.CONFIRMED))
                .thenReturn(BigDecimal.ZERO, new BigDecimal("40"));
        when(appUserRepository.findTopByOrderByIdAsc()).thenReturn(Optional.of(user));
        when(deliveryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ExportResponse first = service.deliver(1L, new DeliveryRequest(
                new BigDecimal("40"), LocalDate.of(2026, 11, 7), null, null));
        ExportResponse second = service.deliver(1L, new DeliveryRequest(
                new BigDecimal("60"), LocalDate.of(2026, 11, 8), null, null));

        assertThat(first.remainingVolume()).isEqualByComparingTo("60");
        assertThat(first.status()).isEqualTo("PARTIALLY_DELIVERED");
        assertThat(second.remainingVolume()).isZero();
        assertThat(second.status()).isEqualTo("COMPLETED");
        assertThat(item.getAvailableKg()).isEqualByComparingTo("-100");
    }

    @Test
    void deliverRejectsVolumeAboveCommitment() {
        ExportTicket ticket = new ExportTicket(
                customer, item, new BigDecimal("100"), new BigDecimal("50000"),
                LocalDate.of(2026, 11, 6), null, "test", user);
        when(exportRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(ticket));
        when(itemRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(item));
        when(deliveryRepository.sumDelivered(1L, DeliveryStatus.CONFIRMED))
                .thenReturn(new BigDecimal("90"));

        assertThatThrownBy(() -> service.deliver(1L, new DeliveryRequest(
                new BigDecimal("11"), LocalDate.of(2026, 11, 7), null, null)))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("DELIVERY_EXCEEDED");
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
