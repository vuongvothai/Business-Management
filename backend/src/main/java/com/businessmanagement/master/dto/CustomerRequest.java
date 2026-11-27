package com.businessmanagement.master.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRequest(
        @NotBlank String name,
        @Size(max = 30) String phone,
        @Size(max = 500) String address
) {}