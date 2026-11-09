package com.businessmanagement.master.web;

import com.businessmanagement.master.dto.SupplierRequest;
import com.businessmanagement.master.dto.SupplierResponse;
import com.businessmanagement.master.entity.Supplier;
import com.businessmanagement.master.repository.SupplierRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierRepository repository;

    public SupplierController(SupplierRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<SupplierResponse> list() {
        return repository.findAll().stream().map(SupplierResponse::from).toList();
    }

    @GetMapping("/search")
    public List<SupplierResponse> search(
            @RequestParam(required = false) String identityNo,
            @RequestParam(required = false) String phone) {
        String identity = clean(identityNo);
        String normalizedPhone = normalizePhone(phone);
        if (identity == null && normalizedPhone == null) {
            throw new IllegalArgumentException("identityNo hoặc phone phải được cung cấp");
        }

        List<Supplier> suppliers = identity == null ? List.of() : repository.findAllByIdentityNo(identity);
        if (suppliers.isEmpty() && normalizedPhone != null) {
            suppliers = repository.findAllByPhone(normalizedPhone);
        }
        return suppliers.stream().map(SupplierResponse::from).toList();
    }

    @GetMapping("/{id}")
    public SupplierResponse get(@PathVariable Long id) {
        return SupplierResponse.from(find(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierResponse create(@Valid @RequestBody SupplierRequest request) {
        String phone = normalizePhone(request.phone());
        Supplier supplier = new Supplier(request.name(), phone);
        supplier.update(request.name(), clean(request.identityNo()), phone, request.address());
        return SupplierResponse.from(repository.save(supplier));
    }

    @PutMapping("/{id}")
    public SupplierResponse update(@PathVariable Long id, @Valid @RequestBody SupplierRequest request) {
        Supplier supplier = find(id);
        supplier.update(request.name(), request.identityNo(), request.phone(), request.address());
        return SupplierResponse.from(repository.save(supplier));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw notFound(id);
        }
        repository.deleteById(id);
    }

    private String clean(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private String normalizePhone(String value) {
        String phone = clean(value);
        if (phone == null) {
            return null;
        }
        String digits = phone.replaceAll("\\D", "");
        if (digits.isEmpty() || digits.length() > 30) {
            throw new IllegalArgumentException("phone không hợp lệ");
        }
        if (digits.startsWith("84") && digits.length() > 2) {
            return "0" + digits.substring(2);
        }
        return digits;
    }

    private Supplier find(Long id) {
        return repository.findById(id).orElseThrow(() -> notFound(id));
    }

    private ResponseStatusException notFound(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found: " + id);
    }
}
