package com.businessmanagement.weighticket.repository;

import com.businessmanagement.weighticket.entity.WeighTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WeighTicketRepository extends JpaRepository<WeighTicket, Long> {
    List<WeighTicket> findByStatusOrderByWeighedAtDesc(String status);
    List<WeighTicket> findByDirectionOrderByWeighedAtDesc(String direction);
}