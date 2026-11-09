package com.businessmanagement.vehicles.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class VehicleTest {

    @Test
    void startsActiveAndCanBeUpdatedOrDeactivated() {
        Vehicle vehicle = new Vehicle("51C-123.45");

        assertThat(vehicle.getLicensePlate()).isEqualTo("51C-123.45");
        assertThat(vehicle.isActive()).isTrue();

        vehicle.updateLicensePlate("51C-999.99");
        vehicle.deactivate();

        assertThat(vehicle.getLicensePlate()).isEqualTo("51C-999.99");
        assertThat(vehicle.isActive()).isFalse();
    }

    @Test
    void lifecycleCallbacksPopulateAuditTimestamps() {
        Vehicle vehicle = new Vehicle("51C-123.45");

        vehicle.onCreate();

        assertThat(vehicle.getCreatedAt()).isNotNull();
        assertThat(vehicle.getUpdatedAt()).isNotNull();
        assertThat(vehicle.getUpdatedAt()).isEqualTo(vehicle.getCreatedAt());
    }
}
