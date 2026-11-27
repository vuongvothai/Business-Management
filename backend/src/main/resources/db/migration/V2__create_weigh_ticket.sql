-- V2: Cân hàng
CREATE TABLE weigh_ticket (
    weigh_ticket_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(30) NOT NULL,
    direction VARCHAR(10) NOT NULL,
    weighed_at DATETIME NOT NULL,
    warehouse_id BIGINT,
    vehicle_id BIGINT,
    truck_license_plate VARCHAR(30),
    total_bags INT NOT NULL DEFAULT 0,
    gross_weight_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    tare_weight_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    net_weight_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    moisture_pct DECIMAL(5,2),
    impurity_pct DECIMAL(5,2),
    moisture_std DECIMAL(5,2) NOT NULL DEFAULT 12.50,
    impurity_std DECIMAL(5,2) NOT NULL DEFAULT 1.00,
    standard_weight_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    sample_test_result VARCHAR(500),
    photo_url VARCHAR(1000),
    note VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_weigh_ticket_code UNIQUE (code),
    CONSTRAINT fk_weigh_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse (warehouse_id),
    CONSTRAINT fk_weigh_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle (vehicle_id),
    CONSTRAINT fk_weigh_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_weigh_direction CHECK (direction IN ('IN', 'OUT')),
    CONSTRAINT chk_weigh_status CHECK (status IN ('DRAFT', 'CONFIRMED', 'CANCELLED'))
) ENGINE = InnoDB;

CREATE TABLE weigh_ticket_line (
    weigh_line_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    weigh_ticket_id BIGINT NOT NULL,
    line_no INT NOT NULL,
    weight_kg DECIMAL(12,3) NOT NULL,
    bag_count INT NOT NULL,
    bag_type VARCHAR(30),
    tare_per_bag_kg DECIMAL(8,3),
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_weigh_line_ticket FOREIGN KEY (weigh_ticket_id) REFERENCES weigh_ticket (weigh_ticket_id),
    CONSTRAINT fk_weigh_line_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_weigh_line_weight CHECK (weight_kg > 0),
    CONSTRAINT chk_weigh_line_bags CHECK (bag_count >= 1)
) ENGINE = InnoDB;

CREATE INDEX idx_weigh_ticket_date ON weigh_ticket (weighed_at);
CREATE INDEX idx_weigh_ticket_status ON weigh_ticket (status);
CREATE INDEX idx_weigh_line_ticket ON weigh_ticket_line (weigh_ticket_id);