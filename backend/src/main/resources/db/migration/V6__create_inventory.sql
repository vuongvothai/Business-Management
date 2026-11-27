-- V6: Tồn kho
CREATE TABLE inventory_transaction (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    ref_type VARCHAR(30) NOT NULL,
    ref_id BIGINT NOT NULL,
    actual_in_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    actual_out_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    standard_in_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    standard_out_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    unit_cost DECIMAL(15,2),
    transaction_date DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_it_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse (warehouse_id),
    CONSTRAINT fk_it_item FOREIGN KEY (item_id) REFERENCES item (item_id),
    CONSTRAINT fk_it_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_it_type CHECK (transaction_type IN ('IN', 'OUT', 'ADJUST')),
    CONSTRAINT chk_it_status CHECK (status IN ('CONFIRMED', 'CANCELLED'))
) ENGINE = InnoDB;

CREATE INDEX idx_it_warehouse_item ON inventory_transaction (warehouse_id, item_id);
CREATE INDEX idx_it_date ON inventory_transaction (transaction_date);
CREATE INDEX idx_it_ref ON inventory_transaction (ref_type, ref_id);