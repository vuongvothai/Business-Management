package com.businessmanagement.loan.repository;

import com.businessmanagement.loan.entity.LoanContract;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanContractRepository extends JpaRepository<LoanContract, Long> {
    List<LoanContract> findByStatusOrderByStartDateDesc(String status);
    List<LoanContract> findByPartyTypeAndPartyIdOrderByStartDateDesc(String partyType, Long partyId);
}