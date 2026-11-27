package com.businessmanagement.master.dto;

import com.businessmanagement.master.entity.Vehicle;

public record VehicleResponse(Long vehicleId, String licensePlate, boolean active) {
    public static VehicleResponse from(Vehicle v) {
        return new VehicleResponse(v.getVehicleId(), v.getLicensePlate(), v.isActive());
    }
}