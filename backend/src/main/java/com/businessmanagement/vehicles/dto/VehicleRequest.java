package com.businessmanagement.vehicles.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VehicleRequest(@NotBlank @Size(max = 30) String licensePlate) {}
