CREATE TABLE vehicle (
    vehicle_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    license_plate VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_vehicle_license_plate UNIQUE (license_plate)
) ENGINE = InnoDB;

CREATE INDEX idx_vehicle_active ON vehicle (active);
