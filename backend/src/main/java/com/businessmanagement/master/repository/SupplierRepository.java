package com.businessmanagement.master.repository;

import com.businessmanagement.master.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
