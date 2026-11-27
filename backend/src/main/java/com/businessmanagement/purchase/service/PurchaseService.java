package com.businessmanagement.purchase.service;

import com.businessmanagement.purchase.dto.*;
import com.businessmanagement.purchase.entity.*;
import com.businessmanagement.purchase.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseContractRepository contractRepository;
    private final ImportTicketRepository importRepository;
    private final PurchaseDeliveryRepository deliveryRepository;

    public PurchaseService(PurchaseContractRepository contractRepository,
                           ImportTicketRepository importRepository,
                           PurchaseDeliveryRepository deliveryRepository) {
        this.contractRepository = contractRepository;
        this.importRepository = importRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Transactional
    public PurchaseContractResponse createContract(PurchaseContractRequest req) {
        PurchaseContract c = new PurchaseContract();
        c.setContractCode(req.contractCode());
        c.setSupplierId(req.supplierId());
        c.setItemId(req.itemId());
        c.setCommittedVolumeKg(req.committedVolumeKg());
        c.setLockedPricePerKg(req.lockedPricePerKg());
        c.setContractDate(req.contractDate());
        c.setDeliveryDeadline(req.deliveryDeadline());
        c.setNote(req.note());
        c.setCreatedBy(req.createdBy());
        return PurchaseContractResponse.from(contractRepository.save(c));
    }

    public List<PurchaseContractResponse> listContracts(Long supplierId, String status) {
        List<PurchaseContract> all;
        if (supplierId != null) {
            all = contractRepository.findBySupplierIdOrderByContractDateDesc(supplierId);
        } else if (status != null && !status.isBlank()) {
            all = contractRepository.findByStatusOrderByContractDateDesc(status);
        } else {
            all = contractRepository.findAll();
        }
        return all.stream().map(PurchaseContractResponse::from).toList();
    }

    @Transactional
    public ImportResponse createImport(ImportRequest req) {
        ImportTicket t = new ImportTicket();
        t.setWeighTicketId(req.weighTicketId());
        t.setSupplierId(req.supplierId());
        t.setItemId(req.itemId());
        t.setWarehouseId(req.warehouseId());
        t.setPurchaseContractId(req.purchaseContractId());
        t.setNumberOfBags(req.numberOfBags());
        t.setActualWeightKg(req.actualWeightKg());
        t.setStandardWeightKg(req.standardWeightKg());
        t.setPurchasePricePerKg(req.purchasePricePerKg());
        t.setMarketPricePerKg(req.marketPricePerKg());
        t.setDeductionAmount(req.deductionAmount() != null ? req.deductionAmount() : BigDecimal.ZERO);
        t.setImportDate(req.importDate());
        t.setNote(req.note());
        t.setCreatedBy(req.createdBy());
        if (req.purchasePricePerKg() != null) {
            t.setTotalAmount(req.standardWeightKg().multiply(req.purchasePricePerKg()));
        }
        return ImportResponse.from(importRepository.save(t));
    }

    public List<ImportResponse> listImports(String status, Long supplierId) {
        List<ImportTicket> all;
        if (status != null && !status.isBlank()) {
            all = importRepository.findByStatusOrderByImportDateDesc(status);
        } else if (supplierId != null) {
            all = importRepository.findBySupplierIdOrderByImportDateDesc(supplierId);
        } else {
            all = importRepository.findAll();
        }
        return all.stream().map(ImportResponse::from).toList();
    }

    @Transactional
    public ImportResponse confirmImport(Long id) {
        ImportTicket t = importRepository.findById(id).orElseThrow();
        t.setStatus("CONFIRMED");
        if (t.getPurchaseContractId() != null) {
            PurchaseContract c = contractRepository.findById(t.getPurchaseContractId()).orElseThrow();
            BigDecimal newDelivered = c.getDeliveredVolumeKg().add(t.getStandardWeightKg());
            c.setDeliveredVolumeKg(newDelivered);
            c.setRemainingVolumeKg(c.getCommittedVolumeKg().subtract(newDelivered));
            if (c.getRemainingVolumeKg().compareTo(BigDecimal.ZERO) <= 0) {
                c.setStatus("COMPLETED");
            }
            contractRepository.save(c);

            PurchaseDelivery d = new PurchaseDelivery();
            d.setPurchaseContractId(c.getPurchaseContractId());
            d.setImportId(t.getImportId());
            d.setDeliveredActualKg(t.getActualWeightKg());
            d.setDeliveredStandardKg(t.getStandardWeightKg());
            d.setDeliveryDate(t.getImportDate());
            deliveryRepository.save(d);
        }
        return ImportResponse.from(t);
    }
}