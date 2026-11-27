package com.businessmanagement.inventory.repository;

import com.businessmanagement.inventory.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    List<InventoryTransaction> findByWarehouseIdAndItemIdOrderByTransactionDateDesc(Long warehouseId, Long itemId);
    List<InventoryTransaction> findByRefTypeAndRefIdOrderByTransactionDateDesc(String refType, Long refId);
}