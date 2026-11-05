CREATE TABLE price_lock_detail (
    price_lock_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    price_lock_id BIGINT NOT NULL,
    import_id BIGINT NOT NULL,
    locked_volume DECIMAL(14,3) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_price_lock_detail_lock
        FOREIGN KEY (price_lock_id) REFERENCES price_lock (price_lock_id),
    CONSTRAINT fk_price_lock_detail_import
        FOREIGN KEY (import_id) REFERENCES import_ticket (import_id),
    CONSTRAINT chk_price_lock_detail_volume CHECK (locked_volume > 0),
    CONSTRAINT uk_price_lock_detail_import UNIQUE (price_lock_id, import_id)
) ENGINE = InnoDB;

CREATE TABLE export_detail (
    export_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    delivery_id BIGINT NOT NULL,
    import_id BIGINT NOT NULL,
    volume DECIMAL(14,3) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_export_detail_delivery
        FOREIGN KEY (delivery_id) REFERENCES export_delivery (delivery_id),
    CONSTRAINT fk_export_detail_import
        FOREIGN KEY (import_id) REFERENCES import_ticket (import_id),
    CONSTRAINT chk_export_detail_volume CHECK (volume > 0),
    CONSTRAINT uk_export_detail_delivery_import UNIQUE (delivery_id, import_id)
) ENGINE = InnoDB;
