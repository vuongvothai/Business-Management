package com.businessmanagement.master.repository;

import com.businessmanagement.exports.entity.DeliveryStatus;
import com.businessmanagement.exports.entity.ExportDelivery;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExportDeliveryRepository extends JpaRepository<ExportDelivery, Long> {

    @Query("select coalesce(sum(d.deliveredVolume), 0) from ExportDelivery d "
            + "where d.exportTicket.id = :exportId and d.status = :status")
    BigDecimal sumDelivered(@Param("exportId") Long exportId,
                            @Param("status") DeliveryStatus status);
}
