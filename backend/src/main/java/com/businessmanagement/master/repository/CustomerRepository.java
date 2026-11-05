package com.businessmanagement.master.repository;

import com.businessmanagement.master.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
