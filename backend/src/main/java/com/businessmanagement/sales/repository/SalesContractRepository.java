package com.businessmanagement.sales.repository;

import com.businessmanagement.sales.entity.SalesContract;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalesContractRepository extends JpaRepository<SalesContract, Long> {
    List<SalesContract> findByCustomerIdOrderByContractDateDesc(Long customerId);
    List<SalesContract> findByContractTypeOrderByContractDateDesc(String contractType);
    List<SalesContract> findByStatusOrderByContractDateDesc(String status);
}