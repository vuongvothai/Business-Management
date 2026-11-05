package com.businessmanagement.inventory.web;

import com.businessmanagement.inventory.dto.InventorySummaryResponse;
import com.businessmanagement.inventory.service.InventorySummaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
public class InventorySummaryController {

    private final InventorySummaryService service;

    public InventorySummaryController(InventorySummaryService service) {
        this.service = service;
    }

    @GetMapping("/summary")
    public InventorySummaryResponse summary() {
        return service.summarize();
    }
}
