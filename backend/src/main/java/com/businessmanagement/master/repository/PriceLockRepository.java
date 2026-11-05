package com.businessmanagement.master.repository;

import com.businessmanagement.pricelocks.entity.PriceLock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceLockRepository extends JpaRepository<PriceLock, Long> {
}
