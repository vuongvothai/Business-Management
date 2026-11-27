package com.businessmanagement.loan.service;

import com.businessmanagement.loan.dto.*;
import com.businessmanagement.loan.entity.*;
import com.businessmanagement.loan.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class LoanService {

    private final LoanContractRepository contractRepository;
    private final LoanTransactionRepository txnRepository;

    public LoanService(LoanContractRepository contractRepository,
                       LoanTransactionRepository txnRepository) {
        this.contractRepository = contractRepository;
        this.txnRepository = txnRepository;
    }

    @Transactional
    public LoanContractResponse createContract(LoanContractRequest req) {
        LoanContract c = new LoanContract();
        c.setCode(req.code());
        c.setLoanType(req.loanType());
        c.setPartyType(req.partyType());
        c.setPartyId(req.partyId());
        c.setPrincipalAmount(req.principalAmount());
        c.setInterestRate(req.interestRate());
        c.setStartDate(req.startDate());
        c.setDueDate(req.dueDate());
        c.setCreatedBy(req.createdBy());
        return LoanContractResponse.from(contractRepository.save(c));
    }

    public List<LoanContractResponse> listContracts(String status, String partyType, Long partyId) {
        List<LoanContract> all;
        if (partyType != null && partyId != null) {
            all = contractRepository.findByPartyTypeAndPartyIdOrderByStartDateDesc(partyType, partyId);
        } else if (status != null && !status.isBlank()) {
            all = contractRepository.findByStatusOrderByStartDateDesc(status);
        } else {
            all = contractRepository.findAll();
        }
        return all.stream().map(LoanContractResponse::from).toList();
    }

    @Transactional
    public LoanTransactionResponse createTransaction(LoanTransactionRequest req) {
        LoanContract c = contractRepository.findById(req.loanId()).orElseThrow();
        LoanTransaction t = new LoanTransaction();
        t.setLoanId(req.loanId());
        t.setTxnType(req.txnType());
        t.setAmount(req.amount());
        t.setTxnDate(req.txnDate());
        t.setInterestDays(req.interestDays());
        t.setNote(req.note());
        t.setCreatedBy(req.createdBy());
        LoanTransaction saved = txnRepository.save(t);

        if ("PRINCIPAL_PAYMENT".equals(req.txnType())) {
            c.setOutstandingPrincipal(c.getOutstandingPrincipal().subtract(req.amount()));
            if (c.getOutstandingPrincipal().compareTo(BigDecimal.ZERO) <= 0) {
                c.setStatus("CLOSED");
            }
            contractRepository.save(c);
        }
        return LoanTransactionResponse.from(saved);
    }

    public List<LoanTransactionResponse> listTransactions(Long loanId) {
        return txnRepository.findByLoanIdOrderByTxnDateDesc(loanId)
                .stream().map(LoanTransactionResponse::from).toList();
    }
}