package com.businessmanagement.report.repository;

import com.businessmanagement.report.entity.ReportSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportScheduleRepository extends JpaRepository<ReportSchedule, Long> {
    List<ReportSchedule> findByActiveTrueOrderByReportType();
}