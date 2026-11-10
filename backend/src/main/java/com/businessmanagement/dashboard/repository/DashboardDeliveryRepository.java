package com.businessmanagement.dashboard.repository;

import com.businessmanagement.exports.entity.DeliveryStatus;
import com.businessmanagement.exports.entity.ExportDelivery;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DashboardDeliveryRepository extends JpaRepository<ExportDelivery, Long> {
    @Query("select d from ExportDelivery d join fetch d.exportTicket e join fetch e.item "
            + "where d.deliveryDate between :fromDate and :toDate and d.status = :status")
    List<ExportDelivery> findConfirmedBetween(@Param("fromDate") LocalDate fromDate,
                                               @Param("toDate") LocalDate toDate,
                                               @Param("status") DeliveryStatus status);
}
