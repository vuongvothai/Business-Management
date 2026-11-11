CREATE TABLE reconciliation_confirmation (
    confirmation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    confirmed_by BIGINT NOT NULL,
    confirmed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    note VARCHAR(1000),
    supplier_discrepancy_kg DECIMAL(18,3) NOT NULL,
    inventory_discrepancy_kg DECIMAL(18,3) NOT NULL,
    CONSTRAINT fk_reconciliation_user FOREIGN KEY (confirmed_by) REFERENCES app_user (user_id)
) ENGINE = InnoDB;
CREATE INDEX idx_reconciliation_confirmed_at ON reconciliation_confirmation (confirmed_at);
