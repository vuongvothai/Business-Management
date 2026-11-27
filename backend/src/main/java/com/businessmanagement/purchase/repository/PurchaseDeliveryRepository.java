package com.businessmanagement.purchase.repository;

import com.businessmanagement.purchase.entity.PurchaseDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseDeliveryRepository extends JpaRepository<PurchaseDelivery, Long> {
    List<PurchaseDelivery> findByPurchaseContractIdOrderByDeliveryDateDesc(Long purchaseContractId);
}