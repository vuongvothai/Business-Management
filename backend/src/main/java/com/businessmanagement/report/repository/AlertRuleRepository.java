package com.businessmanagement.report.repository;

import com.businessmanagement.report.entity.AlertRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRuleRepository extends JpaRepository<AlertRule, Long> {
    List<AlertRule> findByActiveTrueOrderByAlertType();
}