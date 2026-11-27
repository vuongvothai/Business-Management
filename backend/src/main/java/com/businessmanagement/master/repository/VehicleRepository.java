package com.businessmanagement.master.repository;

import com.businessmanagement.master.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByActiveTrueOrderByLicensePlate();
    Optional<Vehicle> findByLicensePlate(String licensePlate);
}