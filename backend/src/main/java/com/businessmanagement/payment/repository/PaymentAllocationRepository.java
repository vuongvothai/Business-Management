package com.businessmanagement.payment.repository;

import com.businessmanagement.payment.entity.PaymentAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentAllocationRepository extends JpaRepository<PaymentAllocation, Long> {
    List<PaymentAllocation> findByVoucherId(Long voucherId);
    List<PaymentAllocation> findByRefTypeAndRefId(String refType, Long refId);
}