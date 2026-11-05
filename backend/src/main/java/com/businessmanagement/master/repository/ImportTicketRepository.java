package com.businessmanagement.master.repository;

import com.businessmanagement.imports.entity.ImportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ImportTicketRepository
        extends JpaRepository<ImportTicket, Long>, JpaSpecificationExecutor<ImportTicket> {
}
