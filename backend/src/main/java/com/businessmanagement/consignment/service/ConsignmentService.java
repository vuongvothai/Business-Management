package com.businessmanagement.consignment.service;

import com.businessmanagement.consignment.dto.*;
import com.businessmanagement.consignment.entity.*;
import com.businessmanagement.consignment.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ConsignmentService {

    private final ConsignmentTicketRepository ticketRepository;
    private final ConsignmentBalanceRepository balanceRepository;
    private final ConsignmentPriceLockRepository priceLockRepository;
    private final AdvancePaymentRepository advanceRepository;

    public ConsignmentService(ConsignmentTicketRepository ticketRepository,
                              ConsignmentBalanceRepository balanceRepository,
                              ConsignmentPriceLockRepository priceLockRepository,
                              AdvancePaymentRepository advanceRepository) {
        this.ticketRepository = ticketRepository;
        this.balanceRepository = balanceRepository;
        this.priceLockRepository = priceLockRepository;
        this.advanceRepository = advanceRepository;
    }

    @Transactional
    public ConsignmentTicketResponse createTicket(ConsignmentTicketRequest req) {
        ConsignmentTicket t = new ConsignmentTicket();
        t.setWeighTicketId(req.weighTicketId());
        t.setCustomerId(req.customerId());
        t.setItemId(req.itemId());
        t.setWarehouseId(req.warehouseId());
        t.setNumberOfBags(req.numberOfBags());
        t.setActualWeightKg(req.actualWeightKg());
        t.setStandardWeightKg(req.standardWeightKg());
        t.setMoisturePct(req.moisturePct());
        t.setImpurityPct(req.impurityPct());
        t.setBlackBrokenRatio(req.blackBrokenRatio());
        t.setScreenSize(req.screenSize());
        t.setFreeStorageDays(req.freeStorageDays());
        t.setStorageFeePerTonMonth(req.storageFeePerTonMonth());
        t.setNaturalLossRatePct(req.naturalLossRatePct());
        t.setPhotoUrl(req.photoUrl());
        t.setConsignmentDate(req.consignmentDate());
        t.setNote(req.note());
        t.setCreatedBy(req.createdBy());
        return ConsignmentTicketResponse.from(ticketRepository.save(t));
    }

    public List<ConsignmentTicketResponse> listTickets(Long customerId, String status) {
        List<ConsignmentTicket> all;
        if (customerId != null) {
            all = ticketRepository.findByCustomerIdOrderByConsignmentDateDesc(customerId);
        } else if (status != null && !status.isBlank()) {
            all = ticketRepository.findByStatusOrderByConsignmentDateDesc(status);
        } else {
            all = ticketRepository.findAll();
        }
        return all.stream().map(ConsignmentTicketResponse::from).toList();
    }

    @Transactional
    public ConsignmentTicketResponse confirmTicket(Long id) {
        ConsignmentTicket t = ticketRepository.findById(id).orElseThrow();
        t.setStatus("CONFIRMED");
        ConsignmentBalance b = balanceRepository
                .findByCustomerIdAndItemId(t.getCustomerId(), t.getItemId())
                .orElseGet(() -> {
                    ConsignmentBalance nb = new ConsignmentBalance();
                    nb.setCustomerId(t.getCustomerId());
                    nb.setItemId(t.getItemId());
                    nb.setWarehouseId(t.getWarehouseId());
                    return nb;
                });
        b.setTotalSentKg(b.getTotalSentKg().add(t.getStandardWeightKg()));
        b.setRemainingKg(b.getTotalSentKg().subtract(b.getTotalLockedKg()).subtract(b.getTotalWithdrawnKg()));
        balanceRepository.save(b);
        return ConsignmentTicketResponse.from(t);
    }

    public List<ConsignmentBalanceResponse> listBalances(Long customerId) {
        return balanceRepository.findByCustomerIdOrderByUpdatedAtDesc(customerId)
                .stream().map(ConsignmentBalanceResponse::from).toList();
    }

    @Transactional
    public ConsignmentPriceLockResponse createPriceLock(ConsignmentPriceLockRequest req) {
        ConsignmentBalance b = balanceRepository
                .findByCustomerIdAndItemId(req.customerId(), req.itemId())
                .orElseThrow(() -> new RuntimeException("No balance"));
        if (req.lockedVolumeKg().compareTo(b.getRemainingKg()) > 0) {
            throw new RuntimeException("Locked volume exceeds remaining");
        }
        ConsignmentPriceLock c = new ConsignmentPriceLock();
        c.setCustomerId(req.customerId());
        c.setConsignmentId(req.consignmentId());
        c.setItemId(req.itemId());
        c.setLockedVolumeKg(req.lockedVolumeKg());
        c.setLockedAt(req.lockedAt());
        c.setLockedPricePerKg(req.lockedPricePerKg());
        c.setGrossAmount(req.lockedVolumeKg().multiply(req.lockedPricePerKg()));
        c.setAdvanceDeductionAmount(req.advanceDeductionAmount() != null ? req.advanceDeductionAmount() : BigDecimal.ZERO);
        c.setNetAmount(c.getGrossAmount().subtract(c.getAdvanceDeductionAmount()));
        c.setNote(req.note());
        c.setCreatedBy(req.createdBy());
        ConsignmentPriceLock saved = priceLockRepository.save(c);

        b.setTotalLockedKg(b.getTotalLockedKg().add(req.lockedVolumeKg()));
        b.setRemainingKg(b.getTotalSentKg().subtract(b.getTotalLockedKg()).subtract(b.getTotalWithdrawnKg()));
        balanceRepository.save(b);
        return ConsignmentPriceLockResponse.from(saved);
    }

    @Transactional
    public AdvancePaymentResponse createAdvance(AdvancePaymentRequest req) {
        AdvancePayment a = new AdvancePayment();
        a.setCustomerId(req.customerId());
        a.setConsignmentId(req.consignmentId());
        a.setAdvanceAmount(req.advanceAmount());
        a.setAdvanceDate(req.advanceDate());
        a.setMonthlyInterestRatePct(req.monthlyInterestRatePct());
        a.setMarketPriceAtAdvance(req.marketPriceAtAdvance());
        a.setCreatedBy(req.createdBy());
        return AdvancePaymentResponse.from(advanceRepository.save(a));
    }

    @Transactional
    public AdvancePaymentResponse settleAdvance(Long id, java.time.LocalDate settleDate) {
        AdvancePayment a = advanceRepository.findById(id).orElseThrow();
        long days = ChronoUnit.DAYS.between(a.getAdvanceDate(), settleDate);
        a.setInterestDays((int) days);
        BigDecimal interest = a.getAdvanceAmount()
                .multiply(a.getMonthlyInterestRatePct())
                .divide(BigDecimal.valueOf(30), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(days));
        a.setInterestAmount(interest);
        a.setTotalRepayment(a.getAdvanceAmount().add(interest));
        a.setSettledAmount(a.getTotalRepayment());
        a.setStatus("SETTLED");
        return AdvancePaymentResponse.from(a);
    }
}