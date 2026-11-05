package com.businessmanagement.master.repository;

import com.businessmanagement.pricelocks.entity.PriceLock;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PriceLockRepository extends JpaRepository<PriceLock, Long> {

    @Query("select new com.businessmanagement.master.repository.PriceLockSummary("
            + "coalesce(sum(p.lockedVolume), 0), coalesce(sum(p.amount), 0)) "
            + "from PriceLock p where p.item.id = :itemId and p.status = "
            + "com.businessmanagement.pricelocks.entity.PriceLockStatus.CONFIRMED")
    PriceLockSummary summarizeByItem(@Param("itemId") Long itemId);
}
