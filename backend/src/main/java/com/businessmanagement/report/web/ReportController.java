package com.businessmanagement.report.web;

import com.businessmanagement.report.entity.AlertRule;
import com.businessmanagement.report.entity.ReportSchedule;
import com.businessmanagement.report.repository.AlertRuleRepository;
import com.businessmanagement.report.repository.ReportScheduleRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportScheduleRepository scheduleRepository;
    private final AlertRuleRepository alertRepository;

    public ReportController(ReportScheduleRepository scheduleRepository,
                            AlertRuleRepository alertRepository) {
        this.scheduleRepository = scheduleRepository;
        this.alertRepository = alertRepository;
    }

    @GetMapping("/schedules")
    public List<ReportSchedule> listSchedules() {
        return scheduleRepository.findByActiveTrueOrderByReportType();
    }

    @GetMapping("/alerts")
    public List<AlertRule> listAlerts() {
        return alertRepository.findByActiveTrueOrderByAlertType();
    }
}