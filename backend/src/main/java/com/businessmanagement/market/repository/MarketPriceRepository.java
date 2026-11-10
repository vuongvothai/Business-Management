package com.businessmanagement.market.repository;

import com.businessmanagement.market.entity.MarketPrice;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketPriceRepository extends JpaRepository<MarketPrice, Long> {
    List<MarketPrice> findByPriceDate(LocalDate priceDate);
}
