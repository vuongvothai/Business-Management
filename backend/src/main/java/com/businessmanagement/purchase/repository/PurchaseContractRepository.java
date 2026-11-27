package com.businessmanagement.purchase.repository;

import com.businessmanagement.purchase.entity.PurchaseContract;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseContractRepository extends JpaRepository<PurchaseContract, Long> {
    List<PurchaseContract> findBySupplierIdOrderByContractDateDesc(Long supplierId);
    List<PurchaseContract> findByStatusOrderByContractDateDesc(String status);
}