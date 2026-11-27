package com.businessmanagement.loan.repository;

import com.businessmanagement.loan.entity.LoanTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanTransactionRepository extends JpaRepository<LoanTransaction, Long> {
    List<LoanTransaction> findByLoanIdOrderByTxnDateDesc(Long loanId);
}