package com.businessmanagement.imports.web;

import com.businessmanagement.imports.dto.ImportRequest;
import com.businessmanagement.imports.dto.ImportPageResponse;
import com.businessmanagement.imports.dto.ImportResponse;
import com.businessmanagement.imports.service.ImportService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/imports")
public class ImportController {

    private final ImportService service;

    public ImportController(ImportService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ImportResponse create(@Valid @RequestBody ImportRequest request) {
        return service.create(request);
    }

    @GetMapping
    public ImportPageResponse list(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        if (page < 0 || size < 1 || size > 100) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "page must be >= 0 and size must be between 1 and 100");
            }
            if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "fromDate must not be after toDate");
            }
        Pageable pageable = PageRequest.of(
                page, size, Sort.by(Sort.Direction.DESC, "importDate")
                        .and(Sort.by(Sort.Direction.DESC, "id")));
        return service.list(fromDate, toDate, supplierId, pageable);
    }

    @GetMapping("/{id}")
    public ImportResponse get(@PathVariable Long id) {
        return service.get(id);
    }
}
