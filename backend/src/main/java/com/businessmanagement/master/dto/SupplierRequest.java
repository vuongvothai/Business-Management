package com.businessmanagement.master.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SupplierRequest(
        @NotBlank @Size(max = 255) String name,
        @Size(max = 50) String identityNo,
        @NotBlank @Size(max = 30) String phone,
        @Size(max = 500) String address) {
}
