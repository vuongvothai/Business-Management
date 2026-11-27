package com.businessmanagement.payment.repository;

import com.businessmanagement.payment.entity.DebtLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DebtLedgerRepository extends JpaRepository<DebtLedger, Long> {
    List<DebtLedger> findByPartyTypeAndPartyIdOrderByUpdatedAtDesc(String partyType, Long partyId);
}