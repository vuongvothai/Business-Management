package com.businessmanagement.master.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItemRequest(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 50) String category,
        @NotBlank @Size(max = 20) String unit) {
}
