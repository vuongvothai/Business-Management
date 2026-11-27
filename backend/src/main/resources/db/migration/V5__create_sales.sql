-- V5: Bán hàng
CREATE TABLE sales_contract (
    contract_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contract_code VARCHAR(50) NOT NULL,
    contract_type VARCHAR(20) NOT NULL DEFAULT 'DIRECT',
    customer_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    committed_volume_kg DECIMAL(14,3) NOT NULL,
    delivered_volume_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    remaining_volume_kg DECIMAL(14,3) NOT NULL,
    unit_price_per_kg DECIMAL(15,2) NOT NULL,
    market_price_per_kg DECIMAL(15,2),
    avg_cost_per_kg DECIMAL(15,2),
    deposit_amount DECIMAL(18,2) NOT NULL DEFAULT 0,
    total_revenue DECIMAL(18,2) NOT NULL DEFAULT 0,
    estimated_profit DECIMAL(18,2),
    contract_date DATE NOT NULL,
    delivery_deadline DATE,
    note VARCHAR(1000),
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_sales_contract_code UNIQUE (contract_code),
    CONSTRAINT fk_sc_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT fk_sc_item FOREIGN KEY (item_id) REFERENCES item (item_id),
    CONSTRAINT fk_sc_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_sc_committed CHECK (committed_volume_kg > 0),
    CONSTRAINT chk_sc_type CHECK (contract_type IN ('DIRECT', 'LOCKED', 'FROM_CONSIGNMENT')),
    CONSTRAINT chk_sc_status CHECK (status IN ('DRAFT', 'CONFIRMED', 'PARTIALLY_DELIVERED', 'COMPLETED', 'CANCELLED'))
) ENGINE = InnoDB;

CREATE TABLE sales_delivery (
    delivery_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contract_id BIGINT NOT NULL,
    weigh_ticket_id BIGINT,
    warehouse_id BIGINT,
    vehicle_id BIGINT,
    truck_license_plate VARCHAR(30),
    delivered_actual_kg DECIMAL(14,3) NOT NULL,
    delivered_standard_kg DECIMAL(14,3) NOT NULL,
    customer_standard_kg DECIMAL(14,3),
    discrepancy_kg DECIMAL(14,3),
    excess_handling VARCHAR(30),
    delivery_date DATE NOT NULL,
    note VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sd_contract FOREIGN KEY (contract_id) REFERENCES sales_contract (contract_id),
    CONSTRAINT fk_sd_weigh FOREIGN KEY (weigh_ticket_id) REFERENCES weigh_ticket (weigh_ticket_id),
    CONSTRAINT fk_sd_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse (warehouse_id),
    CONSTRAINT fk_sd_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle (vehicle_id),
    CONSTRAINT fk_sd_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_sd_actual CHECK (delivered_actual_kg > 0),
    CONSTRAINT chk_sd_status CHECK (status IN ('CONFIRMED', 'CANCELLED'))
) ENGINE = InnoDB;

CREATE INDEX idx_sales_customer ON sales_contract (customer_id);
CREATE INDEX idx_sales_date ON sales_contract (contract_date);
CREATE INDEX idx_sales_type ON sales_contract (contract_type);
CREATE INDEX idx_sd_contract ON sales_delivery (contract_id);