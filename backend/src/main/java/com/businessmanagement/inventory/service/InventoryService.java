package com.businessmanagement.inventory.service;

import com.businessmanagement.inventory.dto.*;
import com.businessmanagement.inventory.entity.InventoryTransaction;
import com.businessmanagement.inventory.repository.InventoryTransactionRepository;
import com.businessmanagement.master.entity.Item;
import com.businessmanagement.master.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class InventoryService {

    private final InventoryTransactionRepository txnRepository;
    private final ItemRepository itemRepository;

    public InventoryService(InventoryTransactionRepository txnRepository,
                            ItemRepository itemRepository) {
        this.txnRepository = txnRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public InventoryTransactionResponse create(InventoryTransactionRequest req) {
        InventoryTransaction t = new InventoryTransaction();
        t.setWarehouseId(req.warehouseId());
        t.setItemId(req.itemId());
        t.setTransactionType(req.transactionType());
        t.setRefType(req.refType());
        t.setRefId(req.refId());
        t.setActualInKg(req.actualInKg() != null ? req.actualInKg() : BigDecimal.ZERO);
        t.setActualOutKg(req.actualOutKg() != null ? req.actualOutKg() : BigDecimal.ZERO);
        t.setStandardInKg(req.standardInKg() != null ? req.standardInKg() : BigDecimal.ZERO);
        t.setStandardOutKg(req.standardOutKg() != null ? req.standardOutKg() : BigDecimal.ZERO);
        t.setUnitCost(req.unitCost());
        t.setTransactionDate(req.transactionDate());
        t.setCreatedBy(req.createdBy());
        InventoryTransaction saved = txnRepository.save(t);

        Item item = itemRepository.findById(req.itemId()).orElseThrow();
        BigDecimal actualDelta = t.getActualInKg().subtract(t.getActualOutKg());
        BigDecimal standardDelta = t.getStandardInKg().subtract(t.getStandardOutKg());
        item.setActualStockKg(item.getActualStockKg().add(actualDelta));
        item.setStandardStockKg(item.getStandardStockKg().add(standardDelta));
        itemRepository.save(item);
        return InventoryTransactionResponse.from(saved);
    }

    public List<InventoryTransactionResponse> list(Long warehouseId, Long itemId) {
        return txnRepository
                .findByWarehouseIdAndItemIdOrderByTransactionDateDesc(warehouseId, itemId)
                .stream().map(InventoryTransactionResponse::from).toList();
    }
}