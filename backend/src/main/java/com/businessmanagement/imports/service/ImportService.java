package com.businessmanagement.imports.service;

import com.businessmanagement.imports.dto.ImportRequest;
import com.businessmanagement.imports.dto.ImportResponse;
import com.businessmanagement.imports.dto.ImportWeightRequest;
import com.businessmanagement.imports.entity.ImportTicket;
import com.businessmanagement.imports.entity.ImportWeight;
import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.entity.Item;
import com.businessmanagement.master.entity.Supplier;
import com.businessmanagement.master.repository.AppUserRepository;
import com.businessmanagement.master.repository.ImportTicketRepository;
import com.businessmanagement.master.repository.ItemRepository;
import com.businessmanagement.master.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ImportService {

    private final ImportTicketRepository importRepository;
    private final SupplierRepository supplierRepository;
    private final ItemRepository itemRepository;
    private final AppUserRepository appUserRepository;

    public ImportService(ImportTicketRepository importRepository,
                          SupplierRepository supplierRepository,
                          ItemRepository itemRepository,
                          AppUserRepository appUserRepository) {
        this.importRepository = importRepository;
        this.supplierRepository = supplierRepository;
        this.itemRepository = itemRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public ImportResponse create(ImportRequest request) {
        Supplier supplier = supplierRepository.findByIdForUpdate(request.supplierId())
                .orElseThrow(() -> notFound("Supplier", request.supplierId()));
        Item item = itemRepository.findByIdForUpdate(request.itemId())
                .orElseThrow(() -> notFound("Item", request.itemId()));
        AppUser user = resolveUser(request.createdByUserId());

        BigDecimal totalKg = request.weights().stream()
                .map(ImportWeightRequest::weightKg)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int numberOfBags = request.weights().stream()
                .mapToInt(ImportWeightRequest::bagCount)
                .sum();

        ImportTicket ticket = new ImportTicket(
                supplier, item, request.importDate(), request.moisturePercent(),
                request.impurityPercent(), request.photoUrl(), request.note(), user);
        ticket.setTotals(numberOfBags, totalKg);
        for (ImportWeightRequest weight : request.weights()) {
            ticket.addWeight(new ImportWeight(weight.weightKg(), weight.bagCount(), user));
        }

        supplier.addUsableCoffeeKg(totalKg);
        item.addAvailableKg(totalKg);
        return ImportResponse.from(importRepository.save(ticket));
    }

    @Transactional
    public ImportResponse get(Long id) {
        return importRepository.findById(id)
                .map(ImportResponse::from)
                .orElseThrow(() -> notFound("Import", id));
    }

    @Transactional
    public List<ImportResponse> list() {
        return importRepository.findAll().stream().map(ImportResponse::from).toList();
    }

    private AppUser resolveUser(Long id) {
        if (id != null) {
            return appUserRepository.findById(id)
                    .orElseThrow(() -> notFound("User", id));
        }
        return appUserRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "No application user is configured"));
    }

    private ResponseStatusException notFound(String resource, Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, resource + " not found: " + id);
    }
}
