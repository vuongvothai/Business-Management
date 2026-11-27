package com.businessmanagement.consignment.repository;

import com.businessmanagement.consignment.entity.ConsignmentTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConsignmentTicketRepository extends JpaRepository<ConsignmentTicket, Long> {
    List<ConsignmentTicket> findByCustomerIdOrderByConsignmentDateDesc(Long customerId);
    List<ConsignmentTicket> findByStatusOrderByConsignmentDateDesc(String status);
}