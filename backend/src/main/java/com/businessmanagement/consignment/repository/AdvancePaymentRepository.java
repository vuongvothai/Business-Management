package com.businessmanagement.consignment.repository;

import com.businessmanagement.consignment.entity.AdvancePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdvancePaymentRepository extends JpaRepository<AdvancePayment, Long> {
    List<AdvancePayment> findByCustomerIdOrderByAdvanceDateDesc(Long customerId);
    List<AdvancePayment> findByStatusOrderByAdvanceDateDesc(String status);
    List<AdvancePayment> findByConsignmentIdAndStatus(Long consignmentId, String status);
}