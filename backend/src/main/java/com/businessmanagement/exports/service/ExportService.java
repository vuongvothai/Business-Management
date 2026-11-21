package com.businessmanagement.exports.service;

import com.businessmanagement.exports.dto.DeliveryRequest;
import com.businessmanagement.exports.dto.ExportRequest;
import com.businessmanagement.exports.dto.ExportResponse;
import com.businessmanagement.exports.dto.DeliveryPrintData;
import com.businessmanagement.audit.service.AuditLogService;
import com.businessmanagement.exports.entity.DeliveryStatus;
import com.businessmanagement.exports.entity.ExportDelivery;
import com.businessmanagement.exports.entity.ExportTicket;
import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.master.entity.Customer;
import com.businessmanagement.master.entity.Item;
import com.businessmanagement.master.repository.AppUserRepository;
import com.businessmanagement.master.repository.CustomerRepository;
import com.businessmanagement.master.repository.ExportDeliveryRepository;
import com.businessmanagement.master.repository.ExportTicketRepository;
import com.businessmanagement.master.repository.ItemRepository;
import com.businessmanagement.master.repository.PriceLockRepository;
import com.businessmanagement.master.repository.PriceLockSummary;
import com.businessmanagement.vehicles.entity.Vehicle;
import com.businessmanagement.vehicles.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class ExportService {

    private final ExportTicketRepository exportRepository;
    private final ExportDeliveryRepository deliveryRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final AppUserRepository appUserRepository;
    private final VehicleRepository vehicleRepository;
    private final PriceLockRepository priceLockRepository;
    private final AuditLogService auditLogService;

    public ExportService(ExportTicketRepository exportRepository,
                         ExportDeliveryRepository deliveryRepository,
                         CustomerRepository customerRepository,
                         ItemRepository itemRepository,
                         AppUserRepository appUserRepository,
                         AuditLogService auditLogService) {
        this.exportRepository = exportRepository;
        this.deliveryRepository = deliveryRepository;
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
        this.appUserRepository = appUserRepository;
        this.auditLogService = auditLogService;
        this.vehicleRepository = null;
        this.priceLockRepository = null;
    }

    @Autowired
    public ExportService(ExportTicketRepository exportRepository,
                         ExportDeliveryRepository deliveryRepository,
                         CustomerRepository customerRepository,
                         ItemRepository itemRepository,
                         AppUserRepository appUserRepository,
                         VehicleRepository vehicleRepository,
                         PriceLockRepository priceLockRepository,
                         AuditLogService auditLogService) {
        this.exportRepository = exportRepository;
        this.deliveryRepository = deliveryRepository;
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
        this.appUserRepository = appUserRepository;
        this.auditLogService = auditLogService;
        this.vehicleRepository = vehicleRepository;
        this.priceLockRepository = priceLockRepository;
    }

    @Transactional
    public ExportResponse create(ExportRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> notFound("Customer", request.customerId()));
        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> notFound("Item", request.itemId()));
        AppUser user = resolveUser(request.createdByUserId());
        validateDecimalScale(request.committedVolume(), 3, "committedVolume");
        validateDecimalScale(request.sellingPricePerKg(), 2, "sellingPricePerKg");
        validateExportDates(request.exportDate(), request.deliveryDueDate());
        ExportTicket ticket = new ExportTicket(customer, item, request.committedVolume(),
                request.sellingPricePerKg(), request.exportDate(), request.deliveryDueDate(),
                request.note(), user);
        if (priceLockRepository != null) {
            PriceLockSummary summary = priceLockRepository.summarizeByItem(item.getId());
            if (summary != null && summary.volume() != null && summary.amount() != null
                    && summary.volume().signum() > 0 && summary.amount().signum() > 0) {
                ticket.snapshotCostPerKg(summary.amount().divide(summary.volume(), 2,
                        java.math.RoundingMode.HALF_UP));
            }
        }
        ticket = exportRepository.save(ticket);
        if (auditLogService != null) {
            auditLogService.record(user, "CREATE", "EXPORT", ticket.getId(), "Export created");
        }
        return ExportResponse.from(ticket, BigDecimal.ZERO);
    }

    @Transactional
    public ExportResponse deliver(Long exportId, DeliveryRequest request) {
        ExportTicket ticket = exportRepository.findByIdForUpdate(exportId)
                .orElseThrow(() -> notFound("Export", exportId));
        Item item = itemRepository.findByIdForUpdate(ticket.getItem().getId())
                .orElseThrow(() -> notFound("Item", ticket.getItem().getId()));
        BigDecimal delivered = deliveryRepository.sumDelivered(exportId, DeliveryStatus.CONFIRMED);
        validateDecimalScale(request.deliveredVolume(), 3, "deliveredVolume");
        BigDecimal newTotal = delivered.add(request.deliveredVolume());
        if (newTotal.compareTo(ticket.getCommittedVolume()) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "DELIVERY_EXCEEDED: delivered volume exceeds committed volume");
        }
        AppUser user = resolveUser(request.createdByUserId());
        Vehicle vehicle = resolveActiveVehicle(request.vehicleId());
        validateDeliveryDate(request.deliveryDate());
        deliveryRepository.save(new ExportDelivery(ticket, request.deliveredVolume(),
                request.deliveryDate(), vehicle, request.loadingWarehouse(),
                request.importWarehouse(), request.note(), user));
        item.addAvailableKg(request.deliveredVolume().negate());
        ticket.updateDeliveryStatus(newTotal);
        if (auditLogService != null) {
            auditLogService.record(user, "CREATE", "DELIVERY", exportId, "Delivery created");
        }
        return ExportResponse.from(ticket, newTotal);
    }

    @Transactional
    public DeliveryPrintData deliveryPrintData(Long exportId, Long deliveryId) {
        ExportDelivery delivery = deliveryRepository.findForPrint(exportId, deliveryId)
                .orElseThrow(() -> notFound("Delivery", deliveryId));
        return DeliveryPrintData.from(delivery);
    }

    @Transactional
    public List<ExportResponse> list() {
        return exportRepository.findAll().stream()
                .map(ticket -> ExportResponse.from(ticket,
                        deliveryRepository.sumDelivered(ticket.getId(), DeliveryStatus.CONFIRMED)))
                .toList();
    }

    private void validateDecimalScale(BigDecimal value, int scale, String field) {
        if (value.scale() > scale) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    field + " supports at most " + scale + " decimals");
        }
    }

    private void validateExportDates(java.time.LocalDate exportDate, java.time.LocalDate deliveryDueDate) {
        if (deliveryDueDate != null && deliveryDueDate.isBefore(exportDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "deliveryDueDate must not be before exportDate");
        }
    }

    private void validateDeliveryDate(java.time.LocalDate deliveryDate) {
        if (deliveryDate.isAfter(java.time.LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "deliveryDate must not be in the future");
        }
    }

    private Vehicle resolveActiveVehicle(Long id) {
        if (id == null) return null;
        Vehicle vehicle = vehicleRepository == null ? null : vehicleRepository.findById(id)
                .orElseThrow(() -> notFound("Vehicle", id));
        if (vehicle == null || !vehicle.isActive()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle is inactive: " + id);
        return vehicle;
    }

    private AppUser resolveUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            return appUserRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> notFound("User", authentication.getName()));
        }
        if (id != null) {
            return appUserRepository.findById(id)
                    .orElseThrow(() -> notFound("User", id));
        }
        return appUserRepository.findTopByOrderByIdAsc()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "No application user is configured"));
    }

    private ResponseStatusException notFound(String resource, Object id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, resource + " not found: " + id);
    }
}
