package com.businessmanagement.purchase.repository;

import com.businessmanagement.purchase.entity.ImportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImportTicketRepository extends JpaRepository<ImportTicket, Long> {
    List<ImportTicket> findByStatusOrderByImportDateDesc(String status);
    List<ImportTicket> findBySupplierIdOrderByImportDateDesc(Long supplierId);
}