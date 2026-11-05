package com.businessmanagement.master.repository;

import com.businessmanagement.master.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
