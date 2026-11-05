package com.businessmanagement.exports.service;

import com.businessmanagement.exports.dto.DeliveryRequest;
import com.businessmanagement.exports.dto.ExportRequest;
import com.businessmanagement.exports.dto.ExportResponse;
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
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExportService {

    private final ExportTicketRepository exportRepository;
    private final ExportDeliveryRepository deliveryRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final AppUserRepository appUserRepository;

    public ExportService(ExportTicketRepository exportRepository,
                         ExportDeliveryRepository deliveryRepository,
                         CustomerRepository customerRepository,
                         ItemRepository itemRepository,
                         AppUserRepository appUserRepository) {
        this.exportRepository = exportRepository;
        this.deliveryRepository = deliveryRepository;
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public ExportResponse create(ExportRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> notFound("Customer", request.customerId()));
        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> notFound("Item", request.itemId()));
        AppUser user = resolveUser(request.createdByUserId());
        ExportTicket ticket = exportRepository.save(new ExportTicket(
                customer, item, request.committedVolume(), request.sellingPricePerKg(),
                request.exportDate(), request.deliveryDueDate(), request.note(), user));
        return ExportResponse.from(ticket, BigDecimal.ZERO);
    }

    @Transactional
    public ExportResponse deliver(Long exportId, DeliveryRequest request) {
        ExportTicket ticket = exportRepository.findByIdForUpdate(exportId)
                .orElseThrow(() -> notFound("Export", exportId));
        Item item = itemRepository.findByIdForUpdate(ticket.getItem().getId())
                .orElseThrow(() -> notFound("Item", ticket.getItem().getId()));
        BigDecimal delivered = deliveryRepository.sumDelivered(exportId, DeliveryStatus.CONFIRMED);
        BigDecimal newTotal = delivered.add(request.deliveredVolume());
        if (newTotal.compareTo(ticket.getCommittedVolume()) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "DELIVERY_EXCEEDED: delivered volume exceeds committed volume");
        }
        AppUser user = resolveUser(request.createdByUserId());
        deliveryRepository.save(new ExportDelivery(ticket, request.deliveredVolume(),
                request.deliveryDate(), request.note(), user));
        item.addAvailableKg(request.deliveredVolume().negate());
        ticket.updateDeliveryStatus(newTotal);
        return ExportResponse.from(ticket, newTotal);
    }

    @Transactional
    public List<ExportResponse> list() {
        return exportRepository.findAll().stream()
                .map(ticket -> ExportResponse.from(ticket,
                        deliveryRepository.sumDelivered(ticket.getId(), DeliveryStatus.CONFIRMED)))
                .toList();
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
