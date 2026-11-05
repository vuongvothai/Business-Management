package com.businessmanagement.imports.dto;

import java.util.List;

public record ImportPageResponse(
        List<ImportResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}
