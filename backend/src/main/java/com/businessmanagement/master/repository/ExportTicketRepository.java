package com.businessmanagement.master.repository;

import com.businessmanagement.exports.entity.ExportTicket;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ExportTicketRepository extends JpaRepository<ExportTicket, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from ExportTicket e where e.id = :id")
    Optional<ExportTicket> findByIdForUpdate(Long id);
}
