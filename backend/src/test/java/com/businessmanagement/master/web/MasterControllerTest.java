package com.businessmanagement.master.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.businessmanagement.master.dto.CustomerRequest;
import com.businessmanagement.master.dto.ItemRequest;
import com.businessmanagement.master.dto.SupplierRequest;
import com.businessmanagement.master.entity.Customer;
import com.businessmanagement.master.entity.Item;
import com.businessmanagement.master.entity.Supplier;
import com.businessmanagement.master.repository.CustomerRepository;
import com.businessmanagement.master.repository.ItemRepository;
import com.businessmanagement.master.repository.SupplierRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class MasterControllerTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    void supplierControllerCreatesUpdatesListsAndDeletesSupplier() {
        SupplierController controller = new SupplierController(supplierRepository);
        Supplier supplier = new Supplier("Supplier", "0900000000");
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        when(supplierRepository.findAll()).thenReturn(List.of(supplier));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.existsById(1L)).thenReturn(true);

        assertThat(controller.create(new SupplierRequest("Supplier", "012345678901", "0900000000", "Address")))
                .isNotNull();
        assertThat(controller.list()).hasSize(1);
        assertThat(controller.get(1L)).isNotNull();
        assertThat(controller.update(1L,
                new SupplierRequest("Updated", "012345678901", "0900000001", "New address")))
                .isNotNull();
        controller.delete(1L);

        verify(supplierRepository).deleteById(1L);
    }

    @Test
    void itemControllerCreatesUpdatesListsAndRejectsMissingItem() {
        ItemController controller = new ItemController(itemRepository);
        Item item = new Item("Green coffee", "Grade A", "KG");
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThat(controller.create(new ItemRequest("Green coffee", "Grade A", "KG"))).isNotNull();
        assertThat(controller.list()).hasSize(1);
        assertThat(controller.get(1L)).isNotNull();
        assertThat(controller.update(1L, new ItemRequest("Updated coffee", "Grade B", "KG"))).isNotNull();
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> controller.get(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Item not found: 99");
    }

    @Test
    void customerControllerCreatesUpdatesListsAndDeletesCustomer() {
        CustomerController controller = new CustomerController(customerRepository);
        Customer customer = new Customer("Customer");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerRepository.findAll()).thenReturn(List.of(customer));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsById(1L)).thenReturn(true);

        assertThat(controller.create(new CustomerRequest("Customer", "0900000000", "Address"))).isNotNull();
        assertThat(controller.list()).hasSize(1);
        assertThat(controller.get(1L)).isNotNull();
        assertThat(controller.update(1L, new CustomerRequest("Updated", "0900000001", "New address")))
                .isNotNull();
        controller.delete(1L);

        verify(customerRepository).deleteById(1L);
    }
}
