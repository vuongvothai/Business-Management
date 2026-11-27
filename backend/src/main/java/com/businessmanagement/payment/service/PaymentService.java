package com.businessmanagement.payment.service;

import com.businessmanagement.payment.dto.*;
import com.businessmanagement.payment.entity.*;
import com.businessmanagement.payment.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentVoucherRepository voucherRepository;
    private final PaymentAllocationRepository allocationRepository;
    private final DebtLedgerRepository debtRepository;

    public PaymentService(PaymentVoucherRepository voucherRepository,
                          PaymentAllocationRepository allocationRepository,
                          DebtLedgerRepository debtRepository) {
        this.voucherRepository = voucherRepository;
        this.allocationRepository = allocationRepository;
        this.debtRepository = debtRepository;
    }

    @Transactional
    public PaymentVoucherResponse create(PaymentVoucherRequest req) {
        PaymentVoucher v = new PaymentVoucher();
        v.setCode(req.code());
        v.setVoucherType(req.voucherType());
        v.setPartyType(req.partyType());
        v.setPartyId(req.partyId());
        v.setPaymentMethod(req.paymentMethod());
        v.setAmount(req.amount());
        v.setVoucherDate(req.voucherDate());
        v.setNote(req.note());
        v.setCreatedBy(req.createdBy());
        PaymentVoucher saved = voucherRepository.save(v);

        if (req.allocations() != null) {
            for (PaymentVoucherRequest.AllocationRequest ar : req.allocations()) {
                PaymentAllocation a = new PaymentAllocation();
                a.setVoucherId(saved.getVoucherId());
                a.setRefType(ar.refType());
                a.setRefId(ar.refId());
                a.setAllocatedAmount(ar.allocatedAmount());
                allocationRepository.save(a);
            }
        }
        return PaymentVoucherResponse.from(saved);
    }

    public List<PaymentVoucherResponse> list(String voucherType, String partyType, Long partyId) {
        List<PaymentVoucher> all;
        if (voucherType != null && !voucherType.isBlank()) {
            all = voucherRepository.findByVoucherTypeOrderByVoucherDateDesc(voucherType);
        } else if (partyType != null && partyId != null) {
            all = voucherRepository.findByPartyTypeAndPartyIdOrderByVoucherDateDesc(partyType, partyId);
        } else {
            all = voucherRepository.findAll();
        }
        return all.stream().map(PaymentVoucherResponse::from).toList();
    }

    @Transactional
    public PaymentVoucherResponse confirm(Long id) {
        PaymentVoucher v = voucherRepository.findById(id).orElseThrow();
        v.setStatus("CONFIRMED");
        if ("PAYMENT".equals(v.getVoucherType())) {
            updateDebt(v.getPartyType(), v.getPartyId(), null, v.getAmount(), BigDecimal.ZERO);
        } else {
            updateDebt(v.getPartyType(), v.getPartyId(), null, BigDecimal.ZERO, v.getAmount());
        }
        return PaymentVoucherResponse.from(v);
    }

    private void updateDebt(String partyType, Long partyId, Long itemId,
                            BigDecimal debit, BigDecimal credit) {
        DebtLedger d = debtRepository
                .findByPartyTypeAndPartyIdOrderByUpdatedAtDesc(partyType, partyId)
                .stream().findFirst().orElseGet(() -> {
                    DebtLedger nd = new DebtLedger();
                    nd.setPartyType(partyType);
                    nd.setPartyId(partyId);
                    nd.setItemId(itemId);
                    return nd;
                });
        d.setDebitAmount(d.getDebitAmount().add(debit));
        d.setCreditAmount(d.getCreditAmount().add(credit));
        d.setBalanceAmount(d.getDebitAmount().subtract(d.getCreditAmount()));
        debtRepository.save(d);
    }

    public List<DebtLedgerResponse> listDebt(String partyType, Long partyId) {
        return debtRepository.findByPartyTypeAndPartyIdOrderByUpdatedAtDesc(partyType, partyId)
                .stream().map(DebtLedgerResponse::from).toList();
    }
}