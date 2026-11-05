package com.businessmanagement.exports.web;

import com.businessmanagement.exports.dto.DeliveryRequest;
import com.businessmanagement.exports.dto.ExportRequest;
import com.businessmanagement.exports.dto.ExportResponse;
import com.businessmanagement.exports.service.ExportService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exports")
public class ExportController {

    private final ExportService service;

    public ExportController(ExportService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExportResponse create(@Valid @RequestBody ExportRequest request) {
        return service.create(request);
    }

    @PostMapping("/{exportId}/deliveries")
    public ExportResponse deliver(@PathVariable Long exportId,
                                  @Valid @RequestBody DeliveryRequest request) {
        return service.deliver(exportId, request);
    }

    @GetMapping
    public List<ExportResponse> list() {
        return service.list();
    }
}
