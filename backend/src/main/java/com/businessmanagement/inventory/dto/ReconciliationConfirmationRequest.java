package com.businessmanagement.inventory.dto;

import jakarta.validation.constraints.Size;

public record ReconciliationConfirmationRequest(@Size(max = 1000) String note) { }
