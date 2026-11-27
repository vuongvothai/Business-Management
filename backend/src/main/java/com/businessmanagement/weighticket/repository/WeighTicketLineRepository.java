package com.businessmanagement.weighticket.repository;

import com.businessmanagement.weighticket.entity.WeighTicketLine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WeighTicketLineRepository extends JpaRepository<WeighTicketLine, Long> {
    List<WeighTicketLine> findByWeighTicketIdOrderByLineNo(Long weighTicketId);
}