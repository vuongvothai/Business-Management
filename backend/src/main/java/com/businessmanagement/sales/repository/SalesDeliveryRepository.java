package com.businessmanagement.sales.repository;

import com.businessmanagement.sales.entity.SalesDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalesDeliveryRepository extends JpaRepository<SalesDelivery, Long> {
    List<SalesDelivery> findByContractIdOrderByDeliveryDateDesc(Long contractId);
}