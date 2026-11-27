-- V8: Gửi kho khách
CREATE TABLE customer_consignment_ticket (
    cc_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    warehouse_id BIGINT,
    weigh_ticket_id BIGINT,
    number_of_bags INT NOT NULL DEFAULT 0,
    gross_weight_kg DECIMAL(14,3) NOT NULL,
    standard_weight_kg DECIMAL(14,3) NOT NULL,
    consignment_date DATE NOT NULL,
    note VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cc_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT fk_cc_item FOREIGN KEY (item_id) REFERENCES item (item_id),
    CONSTRAINT fk_cc_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse (warehouse_id),
    CONSTRAINT fk_cc_weigh FOREIGN KEY (weigh_ticket_id) REFERENCES weigh_ticket (weigh_ticket_id),
    CONSTRAINT fk_cc_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_cc_status CHECK (status IN ('DRAFT', 'CONFIRMED', 'CANCELLED'))
) ENGINE = InnoDB;

CREATE TABLE customer_consignment_balance (
    balance_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    total_sent_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    total_withdrawn_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    remaining_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_cc_balance UNIQUE (customer_id, item_id),
    CONSTRAINT fk_ccb_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT fk_ccb_item FOREIGN KEY (item_id) REFERENCES item (item_id)
) ENGINE = InnoDB;

CREATE INDEX idx_cc_customer ON customer_consignment_ticket (customer_id);
CREATE INDEX idx_cc_date ON customer_consignment_ticket (consignment_date);