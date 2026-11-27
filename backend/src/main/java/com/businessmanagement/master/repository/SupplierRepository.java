package com.businessmanagement.master.repository;

import com.businessmanagement.master.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByIdentityNo(String identityNo);
    List<Supplier> findByPhoneContainingOrderByName(String phone);
    List<Supplier> findByNameContainingIgnoreCaseOrderByName(String name);
}