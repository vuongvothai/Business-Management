package com.businessmanagement.inventory.service;

import com.businessmanagement.audit.service.AuditLogService;
import com.businessmanagement.exports.entity.DeliveryStatus;
import com.businessmanagement.imports.entity.ImportStatus;
import com.businessmanagement.imports.entity.ImportTicket;
import com.businessmanagement.inventory.dto.*;
import com.businessmanagement.inventory.entity.ReconciliationConfirmation;
import com.businessmanagement.inventory.repository.ReconciliationConfirmationRepository;
import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.entity.Item;
import com.businessmanagement.master.entity.Supplier;
import com.businessmanagement.master.repository.*;
import com.businessmanagement.pricelocks.entity.PriceLock;
import com.businessmanagement.pricelocks.entity.PriceLockStatus;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ReconciliationService {
    private final SupplierRepository suppliers;
    private final ItemRepository items;
    private final ImportTicketRepository imports;
    private final PriceLockRepository locks;
    private final ExportDeliveryRepository deliveries;
    private final ReconciliationConfirmationRepository confirmations;
    private final AppUserRepository users;
    private final AuditLogService audit;

    public ReconciliationService(SupplierRepository suppliers, ItemRepository items, ImportTicketRepository imports,
            PriceLockRepository locks, ExportDeliveryRepository deliveries,
            ReconciliationConfirmationRepository confirmations, AppUserRepository users, AuditLogService audit) {
        this.suppliers = suppliers; this.items = items; this.imports = imports; this.locks = locks;
        this.deliveries = deliveries; this.confirmations = confirmations; this.users = users; this.audit = audit;
    }

    @Transactional
    public ReconciliationReportResponse report() {
        Map<Long, BigDecimal> importedBySupplier = imports.findAll().stream()
                        .filter(i -> i.getStatus() == ImportStatus.CONFIRMED)
                        .collect(Collectors.groupingBy(i -> i.getSupplier().getId(),
                                Collectors.reducing(BigDecimal.ZERO, ImportTicket::getTotalKg, BigDecimal::add)));
                Map<Long, BigDecimal> lockedBySupplier = locks.findAll().stream()
                        .filter(p -> p.getStatus() == PriceLockStatus.CONFIRMED)
                        .collect(Collectors.groupingBy(p -> p.getSupplier().getId(),
                                Collectors.reducing(BigDecimal.ZERO, PriceLock::getLockedVolume, BigDecimal::add)));
        List<ReconciliationSupplierRow> supplierRows = suppliers.findAll().stream().map(s -> {
            BigDecimal imported = importedBySupplier.getOrDefault(s.getId(), BigDecimal.ZERO);
            BigDecimal locked = lockedBySupplier.getOrDefault(s.getId(), BigDecimal.ZERO);
            BigDecimal expected = imported.subtract(locked);
            return new ReconciliationSupplierRow(s.getId(), s.getName(), imported, locked, expected,
                    s.getUsableCoffeeKg(), s.getUsableCoffeeKg().subtract(expected));
        }).toList();

        Map<Long, BigDecimal> importedByItem = imports.findAll().stream()
                        .filter(i -> i.getStatus() == ImportStatus.CONFIRMED)
                        .collect(Collectors.groupingBy(i -> i.getItem().getId(),
                                Collectors.reducing(BigDecimal.ZERO, ImportTicket::getTotalKg, BigDecimal::add)));
        Map<Long, BigDecimal> deliveredByItem = deliveries.findAll().stream().filter(d -> d.getStatus() == DeliveryStatus.CONFIRMED)
                .collect(Collectors.groupingBy(d -> d.getExportTicket().getItem().getId(),
                        Collectors.reducing(BigDecimal.ZERO, d -> d.getDeliveredVolume(), BigDecimal::add)));
        List<ReconciliationInventoryRow> inventoryRows = items.findAll().stream().map(i -> {
            BigDecimal imported = importedByItem.getOrDefault(i.getId(), BigDecimal.ZERO);
            BigDecimal delivered = deliveredByItem.getOrDefault(i.getId(), BigDecimal.ZERO);
            BigDecimal expected = imported.subtract(delivered);
            return new ReconciliationInventoryRow(i.getId(), i.getName(), imported, delivered, expected,
                    i.getAvailableKg(), i.getAvailableKg().subtract(expected));
        }).toList();
        BigDecimal supplierDiscrepancy = supplierRows.stream().map(ReconciliationSupplierRow::discrepancyKg).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal inventoryDiscrepancy = inventoryRows.stream().map(ReconciliationInventoryRow::discrepancyKg).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new ReconciliationReportResponse(OffsetDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")), supplierDiscrepancy, inventoryDiscrepancy, supplierRows, inventoryRows);
    }

    @Transactional
    public ReconciliationConfirmationResponse confirm(ReconciliationConfirmationRequest request) {
        ReconciliationReportResponse report = report();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = users.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));
        ReconciliationConfirmation saved = confirmations.save(new ReconciliationConfirmation(user, request.note(),
                report.supplierDiscrepancyKg(), report.inventoryDiscrepancyKg()));
        audit.record(user, "CONFIRM", "RECONCILIATION", saved.getId(), "Balances recorded; no automatic adjustment");
        return ReconciliationConfirmationResponse.from(saved);
    }
}
