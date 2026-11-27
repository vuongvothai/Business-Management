package com.businessmanagement.master.repository;

import com.businessmanagement.master.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByCategoryOrderByItemId(String category);
}