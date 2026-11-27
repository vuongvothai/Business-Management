-- V4: Gửi kho, ứng tiền, chốt giá gửi
CREATE TABLE consignment_ticket (
    consignment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    weigh_ticket_id BIGINT,
    customer_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    warehouse_id BIGINT,
    number_of_bags INT NOT NULL DEFAULT 0,
    actual_weight_kg DECIMAL(14,3) NOT NULL,
    standard_weight_kg DECIMAL(14,3) NOT NULL,
    moisture_pct DECIMAL(5,2),
    impurity_pct DECIMAL(5,2),
    black_broken_ratio DECIMAL(5,2),
    screen_size VARCHAR(20),
    free_storage_days INT NOT NULL DEFAULT 0,
    storage_fee_per_ton_month DECIMAL(12,2),
    natural_loss_rate_pct DECIMAL(5,2),
    photo_url VARCHAR(1000),
    consignment_date DATE NOT NULL,
    note VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_consign_weigh FOREIGN KEY (weigh_ticket_id) REFERENCES weigh_ticket (weigh_ticket_id),
    CONSTRAINT fk_consign_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT fk_consign_item FOREIGN KEY (item_id) REFERENCES item (item_id),
    CONSTRAINT fk_consign_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse (warehouse_id),
    CONSTRAINT fk_consign_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_consign_actual CHECK (actual_weight_kg > 0),
    CONSTRAINT chk_consign_status CHECK (status IN ('DRAFT', 'CONFIRMED', 'CANCELLED'))
) ENGINE = InnoDB;

CREATE TABLE consignment_balance (
    balance_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    warehouse_id BIGINT,
    total_sent_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    total_locked_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    total_withdrawn_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    remaining_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_consign_balance UNIQUE (customer_id, item_id),
    CONSTRAINT fk_cb_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT fk_cb_item FOREIGN KEY (item_id) REFERENCES item (item_id),
    CONSTRAINT fk_cb_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse (warehouse_id)
) ENGINE = InnoDB;

CREATE TABLE consignment_price_lock (
    cpl_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    consignment_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    locked_volume_kg DECIMAL(14,3) NOT NULL,
    locked_at DATETIME NOT NULL,
    locked_price_per_kg DECIMAL(15,2) NOT NULL,
    gross_amount DECIMAL(18,2) NOT NULL,
    advance_deduction_amount DECIMAL(18,2) NOT NULL DEFAULT 0,
    net_amount DECIMAL(18,2) NOT NULL,
    note VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cpl_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT fk_cpl_consign FOREIGN KEY (consignment_id) REFERENCES consignment_ticket (consignment_id),
    CONSTRAINT fk_cpl_item FOREIGN KEY (item_id) REFERENCES item (item_id),
    CONSTRAINT fk_cpl_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_cpl_volume CHECK (locked_volume_kg > 0),
    CONSTRAINT chk_cpl_status CHECK (status IN ('DRAFT', 'CONFIRMED'))
) ENGINE = InnoDB;

CREATE TABLE advance_payment (
    advance_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    consignment_id BIGINT NOT NULL,
    advance_amount DECIMAL(18,2) NOT NULL,
    advance_date DATE NOT NULL,
    monthly_interest_rate_pct DECIMAL(8,4) NOT NULL,
    market_price_at_advance DECIMAL(15,2),
    interest_days INT NOT NULL DEFAULT 0,
    interest_amount DECIMAL(18,2) NOT NULL DEFAULT 0,
    total_repayment DECIMAL(18,2) NOT NULL DEFAULT 0,
    settled_amount DECIMAL(18,2) NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ap_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT fk_ap_consign FOREIGN KEY (consignment_id) REFERENCES consignment_ticket (consignment_id),
    CONSTRAINT fk_ap_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_ap_amount CHECK (advance_amount > 0),
    CONSTRAINT chk_ap_status CHECK (status IN ('ACTIVE', 'SETTLED'))
) ENGINE = InnoDB;

CREATE INDEX idx_consign_customer ON consignment_ticket (customer_id);
CREATE INDEX idx_consign_date ON consignment_ticket (consignment_date);
CREATE INDEX idx_cpl_customer ON consignment_price_lock (customer_id);
CREATE INDEX idx_ap_customer ON advance_payment (customer_id);