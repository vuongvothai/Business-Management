package com.businessmanagement.sales.service;

import com.businessmanagement.sales.dto.*;
import com.businessmanagement.sales.entity.*;
import com.businessmanagement.sales.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class SalesService {

    private final SalesContractRepository contractRepository;
    private final SalesDeliveryRepository deliveryRepository;

    public SalesService(SalesContractRepository contractRepository,
                        SalesDeliveryRepository deliveryRepository) {
        this.contractRepository = contractRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Transactional
    public SalesContractResponse createContract(SalesContractRequest req) {
        SalesContract c = new SalesContract();
        c.setContractCode(req.contractCode());
        c.setContractType(req.contractType() != null ? req.contractType() : "DIRECT");
        c.setCustomerId(req.customerId());
        c.setItemId(req.itemId());
        c.setCommittedVolumeKg(req.committedVolumeKg());
        c.setUnitPricePerKg(req.unitPricePerKg());
        c.setMarketPricePerKg(req.marketPricePerKg());
        c.setAvgCostPerKg(req.avgCostPerKg());
        c.setDepositAmount(req.depositAmount() != null ? req.depositAmount() : BigDecimal.ZERO);
        c.setContractDate(req.contractDate());
        c.setDeliveryDeadline(req.deliveryDeadline());
        c.setNote(req.note());
        c.setCreatedBy(req.createdBy());
        c.setTotalRevenue(req.committedVolumeKg().multiply(req.unitPricePerKg()));
        if (req.avgCostPerKg() != null) {
            c.setEstimatedProfit(c.getTotalRevenue()
                    .subtract(req.avgCostPerKg().multiply(req.committedVolumeKg())));
        }
        return SalesContractResponse.from(contractRepository.save(c));
    }

    public List<SalesContractResponse> listContracts(Long customerId, String contractType, String status) {
        List<SalesContract> all;
        if (customerId != null) {
            all = contractRepository.findByCustomerIdOrderByContractDateDesc(customerId);
        } else if (contractType != null && !contractType.isBlank()) {
            all = contractRepository.findByContractTypeOrderByContractDateDesc(contractType);
        } else if (status != null && !status.isBlank()) {
            all = contractRepository.findByStatusOrderByContractDateDesc(status);
        } else {
            all = contractRepository.findAll();
        }
        return all.stream().map(SalesContractResponse::from).toList();
    }

    @Transactional
    public SalesDeliveryResponse createDelivery(SalesDeliveryRequest req) {
        SalesContract c = contractRepository.findById(req.contractId()).orElseThrow();
        SalesDelivery d = new SalesDelivery();
        d.setContractId(req.contractId());
        d.setWeighTicketId(req.weighTicketId());
        d.setWarehouseId(req.warehouseId());
        d.setVehicleId(req.vehicleId());
        d.setTruckLicensePlate(req.truckLicensePlate());
        d.setDeliveredActualKg(req.deliveredActualKg());
        d.setDeliveredStandardKg(req.deliveredStandardKg());
        d.setCustomerStandardKg(req.customerStandardKg());
        if (req.customerStandardKg() != null) {
            d.setDiscrepancyKg(req.deliveredStandardKg().subtract(req.customerStandardKg()));
        }
        d.setExcessHandling(req.excessHandling());
        d.setDeliveryDate(req.deliveryDate());
        d.setNote(req.note());
        d.setCreatedBy(req.createdBy());
        SalesDelivery saved = deliveryRepository.save(d);

        BigDecimal newDelivered = c.getDeliveredVolumeKg().add(req.deliveredStandardKg());
        c.setDeliveredVolumeKg(newDelivered);
        c.setRemainingVolumeKg(c.getCommittedVolumeKg().subtract(newDelivered));
        if (c.getRemainingVolumeKg().compareTo(BigDecimal.ZERO) <= 0) {
            c.setStatus("COMPLETED");
        } else if (newDelivered.compareTo(BigDecimal.ZERO) > 0) {
            c.setStatus("PARTIALLY_DELIVERED");
        }
        contractRepository.save(c);
        return SalesDeliveryResponse.from(saved);
    }

    public List<SalesDeliveryResponse> listDeliveries(Long contractId) {
        return deliveryRepository.findByContractIdOrderByDeliveryDateDesc(contractId)
                .stream().map(SalesDeliveryResponse::from).toList();
    }
}