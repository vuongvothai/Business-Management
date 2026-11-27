package com.businessmanagement.consignment.repository;

import com.businessmanagement.consignment.entity.ConsignmentPriceLock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConsignmentPriceLockRepository extends JpaRepository<ConsignmentPriceLock, Long> {
    List<ConsignmentPriceLock> findByCustomerIdOrderByLockedAtDesc(Long customerId);
    List<ConsignmentPriceLock> findByStatusOrderByLockedAtDesc(String status);
}