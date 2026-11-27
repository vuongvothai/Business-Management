package com.businessmanagement.consignment.repository;

import com.businessmanagement.consignment.entity.ConsignmentBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ConsignmentBalanceRepository extends JpaRepository<ConsignmentBalance, Long> {
    Optional<ConsignmentBalance> findByCustomerIdAndItemId(Long customerId, Long itemId);
    List<ConsignmentBalance> findByCustomerIdOrderByUpdatedAtDesc(Long customerId);
}