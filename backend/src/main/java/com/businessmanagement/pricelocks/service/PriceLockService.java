package com.businessmanagement.pricelocks.service;

import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.entity.Item;
import com.businessmanagement.master.entity.Supplier;
import com.businessmanagement.master.repository.AppUserRepository;
import com.businessmanagement.master.repository.ItemRepository;
import com.businessmanagement.master.repository.PriceLockRepository;
import com.businessmanagement.master.repository.SupplierRepository;
import com.businessmanagement.pricelocks.dto.PriceLockRequest;
import com.businessmanagement.pricelocks.dto.PriceLockResponse;
import com.businessmanagement.pricelocks.entity.PriceLock;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PriceLockService {

    private final PriceLockRepository priceLockRepository;
    private final SupplierRepository supplierRepository;
    private final ItemRepository itemRepository;
    private final AppUserRepository appUserRepository;

    public PriceLockService(PriceLockRepository priceLockRepository,
                            SupplierRepository supplierRepository,
                            ItemRepository itemRepository,
                            AppUserRepository appUserRepository) {
        this.priceLockRepository = priceLockRepository;
        this.supplierRepository = supplierRepository;
        this.itemRepository = itemRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public PriceLockResponse create(PriceLockRequest request) {
        Supplier supplier = supplierRepository.findByIdForUpdate(request.supplierId())
                .orElseThrow(() -> notFound("Supplier", request.supplierId()));
        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> notFound("Item", request.itemId()));
        if (supplier.getUsableCoffeeKg().compareTo(request.lockedVolume()) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "PRICE_LOCK_EXCEEDED: usable coffee balance is insufficient");
        }

        AppUser user = resolveUser(request.createdByUserId());
        BigDecimal amount = request.lockedVolume().multiply(request.lockedPricePerKg());
        PriceLock priceLock = new PriceLock(
                supplier, item, request.lockDate(), request.lockedVolume(),
                request.lockedPricePerKg(), amount, request.note(), user);
        supplier.subtractUsableCoffeeKg(request.lockedVolume());
        return PriceLockResponse.from(priceLockRepository.save(priceLock));
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
