-- V10: Nâng cao
CREATE TABLE report_schedule (
    schedule_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_type VARCHAR(30) NOT NULL,
    cron_expr VARCHAR(50) NOT NULL,
    recipient_email VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    last_sent_at DATETIME
) ENGINE = InnoDB;

CREATE TABLE alert_rule (
    alert_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    alert_type VARCHAR(30) NOT NULL,
    party_type VARCHAR(10),
    party_id BIGINT,
    item_id BIGINT,
    threshold_value DECIMAL(18,3) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_alert_item FOREIGN KEY (item_id) REFERENCES item (item_id)
) ENGINE = InnoDB;