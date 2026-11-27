package com.businessmanagement.master.web;

import com.businessmanagement.master.dto.*;
import com.businessmanagement.master.entity.*;
import com.businessmanagement.master.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/master")
public class MasterController {

    private final SupplierRepository supplierRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final VehicleRepository vehicleRepository;
    private final WarehouseRepository warehouseRepository;

    public MasterController(SupplierRepository supplierRepository,
                            CustomerRepository customerRepository,
                            ItemRepository itemRepository,
                            VehicleRepository vehicleRepository,
                            WarehouseRepository warehouseRepository) {
        this.supplierRepository = supplierRepository;
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
        this.vehicleRepository = vehicleRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @GetMapping("/suppliers")
    public List<SupplierResponse> listSuppliers(@RequestParam(required = false) String q) {
        List<Supplier> all = (q == null || q.isBlank())
                ? supplierRepository.findAll()
                : supplierRepository.findByNameContainingIgnoreCaseOrderByName(q);
        return all.stream().map(SupplierResponse::from).toList();
    }

    @PostMapping("/suppliers")
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierResponse createSupplier(@Valid @RequestBody SupplierRequest req) {
        Supplier s = new Supplier();
        s.setName(req.name());
        s.setIdentityNo(req.identityNo());
        s.setPhone(req.phone());
        s.setAddress(req.address());
        return SupplierResponse.from(supplierRepository.save(s));
    }

    @GetMapping("/customers")
    public List<CustomerResponse> listCustomers(@RequestParam(required = false) String q) {
        List<Customer> all = (q == null || q.isBlank())
                ? customerRepository.findAll()
                : customerRepository.findByNameContainingIgnoreCaseOrderByName(q);
        return all.stream().map(CustomerResponse::from).toList();
    }

    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody CustomerRequest req) {
        Customer c = new Customer();
        c.setName(req.name());
        c.setPhone(req.phone());
        c.setAddress(req.address());
        return CustomerResponse.from(customerRepository.save(c));
    }

    @GetMapping("/items")
    public List<ItemResponse> listItems() {
        return itemRepository.findAll().stream().map(ItemResponse::from).toList();
    }

    @GetMapping("/vehicles")
    public List<VehicleResponse> listVehicles() {
        return vehicleRepository.findByActiveTrueOrderByLicensePlate().stream()
                .map(VehicleResponse::from).toList();
    }

    @GetMapping("/warehouses")
    public List<WarehouseResponse> listWarehouses() {
        return warehouseRepository.findByActiveTrueOrderByName().stream()
                .map(WarehouseResponse::from).toList();
    }
}