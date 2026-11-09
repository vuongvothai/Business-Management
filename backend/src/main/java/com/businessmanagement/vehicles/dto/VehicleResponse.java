package com.businessmanagement.vehicles.dto;

import com.businessmanagement.vehicles.entity.Vehicle;
import java.time.LocalDateTime;

public record VehicleResponse(Long id, String licensePlate, boolean active,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static VehicleResponse from(Vehicle vehicle) {
        return new VehicleResponse(vehicle.getId(), vehicle.getLicensePlate(), vehicle.isActive(),
                vehicle.getCreatedAt(), vehicle.getUpdatedAt());
    }
}
