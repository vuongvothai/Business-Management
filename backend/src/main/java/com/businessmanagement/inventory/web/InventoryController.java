package com.businessmanagement.inventory.web;

import com.businessmanagement.inventory.dto.*;
import com.businessmanagement.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping("/transactions")
    public List<InventoryTransactionResponse> list(
            @RequestParam Long warehouseId,
            @RequestParam Long itemId) {
        return service.list(warehouseId, itemId);
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryTransactionResponse create(@Valid @RequestBody InventoryTransactionRequest req) {
        return service.create(req);
    }
}