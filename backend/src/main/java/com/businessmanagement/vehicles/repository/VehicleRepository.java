package com.businessmanagement.vehicles.repository;

import com.businessmanagement.vehicles.entity.Vehicle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByActiveTrueOrderByLicensePlateAsc();
    java.util.Optional<Vehicle> findByLicensePlate(String licensePlate);
}
