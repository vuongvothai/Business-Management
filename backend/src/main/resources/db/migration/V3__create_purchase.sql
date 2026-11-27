-- V3: Thu mua
CREATE TABLE purchase_contract (
    purchase_contract_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contract_code VARCHAR(50) NOT NULL,
    supplier_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    committed_volume_kg DECIMAL(14,3) NOT NULL,
    delivered_volume_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    remaining_volume_kg DECIMAL(14,3) NOT NULL,
    locked_price_per_kg DECIMAL(15,2) NOT NULL,
    contract_date DATE NOT NULL,
    delivery_deadline DATE,
    note VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_purchase_contract_code UNIQUE (contract_code),
    CONSTRAINT fk_pc_supplier FOREIGN KEY (supplier_id) REFERENCES supplier (supplier_id),
    CONSTRAINT fk_pc_item FOREIGN KEY (item_id) REFERENCES item (item_id),
    CONSTRAINT fk_pc_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_pc_committed CHECK (committed_volume_kg > 0),
    CONSTRAINT chk_pc_status CHECK (status IN ('ACTIVE', 'COMPLETED', 'OVERDUE', 'CANCELLED'))
) ENGINE = InnoDB;

CREATE TABLE import_ticket (
    import_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    weigh_ticket_id BIGINT,
    supplier_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    warehouse_id BIGINT,
    purchase_contract_id BIGINT,
    number_of_bags INT NOT NULL DEFAULT 0,
    actual_weight_kg DECIMAL(14,3) NOT NULL,
    standard_weight_kg DECIMAL(14,3) NOT NULL,
    purchase_price_per_kg DECIMAL(15,2),
    market_price_per_kg DECIMAL(15,2),
    deduction_amount DECIMAL(18,2) NOT NULL DEFAULT 0,
    total_amount DECIMAL(18,2),
    import_date DATE NOT NULL,
    note VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_import_weigh FOREIGN KEY (weigh_ticket_id) REFERENCES weigh_ticket (weigh_ticket_id),
    CONSTRAINT fk_import_supplier FOREIGN KEY (supplier_id) REFERENCES supplier (supplier_id),
    CONSTRAINT fk_import_item FOREIGN KEY (item_id) REFERENCES item (item_id),
    CONSTRAINT fk_import_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse (warehouse_id),
    CONSTRAINT fk_import_pc FOREIGN KEY (purchase_contract_id) REFERENCES purchase_contract (purchase_contract_id),
    CONSTRAINT fk_import_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_import_actual CHECK (actual_weight_kg > 0),
    CONSTRAINT chk_import_status CHECK (status IN ('DRAFT', 'CONFIRMED', 'CANCELLED'))
) ENGINE = InnoDB;

CREATE TABLE purchase_delivery (
    purchase_delivery_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_contract_id BIGINT NOT NULL,
    import_id BIGINT NOT NULL,
    delivered_actual_kg DECIMAL(14,3) NOT NULL,
    delivered_standard_kg DECIMAL(14,3) NOT NULL,
    delivery_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pd_pc FOREIGN KEY (purchase_contract_id) REFERENCES purchase_contract (purchase_contract_id),
    CONSTRAINT fk_pd_import FOREIGN KEY (import_id) REFERENCES import_ticket (import_id),
    CONSTRAINT chk_pd_actual CHECK (delivered_actual_kg > 0),
    CONSTRAINT chk_pd_status CHECK (status IN ('CONFIRMED', 'CANCELLED'))
) ENGINE = InnoDB;

CREATE INDEX idx_import_date ON import_ticket (import_date);
CREATE INDEX idx_import_supplier ON import_ticket (supplier_id);
CREATE INDEX idx_import_item ON import_ticket (item_id);
CREATE INDEX idx_pc_supplier ON purchase_contract (supplier_id);
CREATE INDEX idx_pd_pc ON purchase_delivery (purchase_contract_id);