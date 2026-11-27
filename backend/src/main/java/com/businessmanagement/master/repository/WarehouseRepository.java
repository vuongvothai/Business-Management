package com.businessmanagement.master.repository;

import com.businessmanagement.master.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findByActiveTrueOrderByName();
    Optional<Warehouse> findByCode(String code);
}