CREATE TABLE expense (
    expense_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total_amount DECIMAL(18,2) NOT NULL DEFAULT 0,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    note VARCHAR(1000),
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_expense_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT fk_expense_updater FOREIGN KEY (updated_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_expense_amount CHECK (total_amount >= 0),
    CONSTRAINT chk_expense_period CHECK (period_end >= period_start)
) ENGINE = InnoDB;

CREATE TABLE expense_detail (
    expense_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    expense_id BIGINT NOT NULL,
    category_name VARCHAR(100) NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    CONSTRAINT fk_expense_detail_expense FOREIGN KEY (expense_id) REFERENCES expense (expense_id),
    CONSTRAINT uk_expense_category UNIQUE (expense_id, category_name),
    CONSTRAINT chk_expense_detail_amount CHECK (amount >= 0)
) ENGINE = InnoDB;

CREATE INDEX idx_expense_period ON expense (period_start, period_end);
