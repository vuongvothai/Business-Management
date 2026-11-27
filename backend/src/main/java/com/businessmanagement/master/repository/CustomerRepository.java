package com.businessmanagement.master.repository;

import com.businessmanagement.master.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByPhoneContainingOrderByName();
    List<Customer> findByNameContainingIgnoreCaseOrderByName(String name);
}