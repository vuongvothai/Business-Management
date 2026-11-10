package com.businessmanagement.master.repository;

import com.businessmanagement.exports.entity.DeliveryStatus;
import com.businessmanagement.exports.entity.ExportDelivery;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ExportDeliveryRepository extends JpaRepository<ExportDelivery, Long> {

    @Query("select d from ExportDelivery d join fetch d.exportTicket e join fetch e.customer "
            + "join fetch e.item where d.id = :deliveryId and e.id = :exportId")
    Optional<ExportDelivery> findForPrint(@Param("exportId") Long exportId,
                                          @Param("deliveryId") Long deliveryId);

    @Query("select coalesce(sum(d.deliveredVolume), 0) from ExportDelivery d "
            + "where d.exportTicket.id = :exportId and d.status = :status")
    BigDecimal sumDelivered(@Param("exportId") Long exportId,
                            @Param("status") DeliveryStatus status);
}
