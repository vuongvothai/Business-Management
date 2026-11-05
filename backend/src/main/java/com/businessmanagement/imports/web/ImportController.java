package com.businessmanagement.imports.web;

import com.businessmanagement.imports.dto.ImportRequest;
import com.businessmanagement.imports.dto.ImportResponse;
import com.businessmanagement.imports.service.ImportService;
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
    public List<ImportResponse> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public ImportResponse get(@PathVariable Long id) {
        return service.get(id);
    }
}
