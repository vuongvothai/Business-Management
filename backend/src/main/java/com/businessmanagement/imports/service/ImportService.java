package com.businessmanagement.imports.service;

import com.businessmanagement.imports.dto.ImportRequest;
import com.businessmanagement.imports.dto.ImportPageResponse;
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
import com.businessmanagement.vehicles.entity.Vehicle;
import com.businessmanagement.vehicles.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ImportService {

    private final ImportTicketRepository importRepository;
    private final SupplierRepository supplierRepository;
    private final ItemRepository itemRepository;
    private final AppUserRepository appUserRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public ImportService(ImportTicketRepository importRepository,
                          SupplierRepository supplierRepository,
                          ItemRepository itemRepository,
                          AppUserRepository appUserRepository,
                          VehicleRepository vehicleRepository) {
        this.importRepository = importRepository;
        this.supplierRepository = supplierRepository;
        this.itemRepository = itemRepository;
        this.appUserRepository = appUserRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public ImportService(ImportTicketRepository importRepository,
                          SupplierRepository supplierRepository,
                          ItemRepository itemRepository,
                          AppUserRepository appUserRepository) {
        this.importRepository = importRepository;
        this.supplierRepository = supplierRepository;
        this.itemRepository = itemRepository;
        this.appUserRepository = appUserRepository;
        this.vehicleRepository = null;
    }

    @Transactional
    public ImportResponse create(ImportRequest request) {
        Supplier supplier = supplierRepository.findByIdForUpdate(request.supplierId())
                .orElseThrow(() -> notFound("Supplier", request.supplierId()));
        Item item = itemRepository.findByIdForUpdate(request.itemId())
                .orElseThrow(() -> notFound("Item", request.itemId()));
        AppUser user = resolveUser(request.createdByUserId());
        Vehicle vehicle = resolveActiveVehicle(request.vehicleId());

        BigDecimal totalKg = request.weights().stream()
                .map(ImportWeightRequest::weightKg)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int numberOfBags = request.weights().stream()
                .mapToInt(ImportWeightRequest::bagCount)
                .sum();

        ImportTicket ticket = new ImportTicket(
                supplier, item, request.importDate(), request.moisturePercent(),
                request.impurityPercent(), request.photoUrl(), request.note(), user);
        ticket.setVehicleDetails(vehicle, request.loadingWarehouse(), request.importWarehouse(),
                request.tareWeight(), request.sampleTestResult());
        ticket.setTotals(numberOfBags, totalKg);
        ticket.setPricing(request.purchasePricePerKg(), request.marketPricePerKg());
        BigDecimal totalDeduction = null;
        if (request.moisturePercent() != null || request.impurityPercent() != null || request.tareWeight() != null) {
            try {
                totalDeduction = ImportDeductionCalculator.totalDeductionKg(totalKg, numberOfBags,
                        request.tareWeight(), request.moisturePercent(), request.impurityPercent());
            } catch (IllegalArgumentException exception) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
            }
        }
        ticket.setCalculatedTotals(ImportDeductionCalculator.totalAmount(totalKg, request.purchasePricePerKg()), totalDeduction);
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
    public ImportPageResponse list(LocalDate fromDate, LocalDate toDate,
                                   Long supplierId, Pageable pageable) {
        Specification<ImportTicket> specification = Specification.where(null);
        if (fromDate != null) {
            specification = specification.and((root, query, builder) ->
                    builder.greaterThanOrEqualTo(root.get("importDate"), fromDate));
        }
        if (toDate != null) {
            specification = specification.and((root, query, builder) ->
                    builder.lessThanOrEqualTo(root.get("importDate"), toDate));
        }
        if (supplierId != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get("supplier").get("id"), supplierId));
        }
        Page<ImportResponse> result = importRepository.findAll(specification, pageable)
                .map(ImportResponse::from);
        return new ImportPageResponse(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages());
    }

    private Vehicle resolveActiveVehicle(Long id) {
        if (id == null) return null;
        Vehicle vehicle = vehicleRepository == null ? null : vehicleRepository.findById(id)
                .orElseThrow(() -> notFound("Vehicle", id));
        if (!vehicle.isActive()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle is inactive: " + id);
        return vehicle;
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
