CREATE TABLE role (
    role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(30) NOT NULL,
    CONSTRAINT uk_role_name UNIQUE (role_name),
    CONSTRAINT chk_role_name CHECK (role_name IN ('ADMIN', 'WEIGHER', 'ACCOUNTANT'))
) ENGINE = InnoDB;

CREATE TABLE app_user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_role
        FOREIGN KEY (role_id) REFERENCES role (role_id)
) ENGINE = InnoDB;

CREATE TABLE supplier (
    supplier_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    identity_no VARCHAR(50),
    phone VARCHAR(30) NOT NULL,
    address VARCHAR(500),
    usable_coffee_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_supplier_balance CHECK (usable_coffee_kg >= 0)
) ENGINE = InnoDB;

CREATE TABLE item (
    item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    unit VARCHAR(20) NOT NULL DEFAULT 'KG',
    available_kg DECIMAL(14,3) NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;

CREATE TABLE customer (
    customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(30),
    address VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;

CREATE TABLE import_ticket (
    import_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    number_of_bags INT NOT NULL,
    total_kg DECIMAL(14,3) NOT NULL,
    moisture_percent DECIMAL(5,2),
    impurity_percent DECIMAL(5,2),
    purchase_price_per_kg DECIMAL(15,2),
    market_price_per_kg DECIMAL(15,2),
    market_difference_per_kg DECIMAL(15,2),
    photo_url VARCHAR(1000),
    import_date DATE NOT NULL,
    note VARCHAR(1000),
    status VARCHAR(20) NOT NULL,
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_import_supplier
        FOREIGN KEY (supplier_id) REFERENCES supplier (supplier_id),
    CONSTRAINT fk_import_item
        FOREIGN KEY (item_id) REFERENCES item (item_id),
    CONSTRAINT fk_import_creator
        FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_import_bags CHECK (number_of_bags > 0),
    CONSTRAINT chk_import_total CHECK (total_kg > 0),
    CONSTRAINT chk_import_moisture CHECK (
        moisture_percent IS NULL OR moisture_percent BETWEEN 0 AND 100
    ),
    CONSTRAINT chk_import_impurity CHECK (
        impurity_percent IS NULL OR impurity_percent BETWEEN 0 AND 100
    ),
    CONSTRAINT chk_import_status CHECK (
        status IN ('DRAFT', 'CONFIRMED', 'CANCELLED')
    )
) ENGINE = InnoDB;

CREATE TABLE import_weight (
    weight_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    import_id BIGINT NOT NULL,
    weight_kg DECIMAL(12,3) NOT NULL,
    bag_count TINYINT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_weight_import
        FOREIGN KEY (import_id) REFERENCES import_ticket (import_id),
    CONSTRAINT fk_weight_creator
        FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_weight_value CHECK (weight_kg > 0),
    CONSTRAINT chk_weight_bags CHECK (bag_count IN (1, 2))
) ENGINE = InnoDB;

CREATE TABLE price_lock (
    price_lock_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    locked_volume DECIMAL(14,3) NOT NULL,
    lock_date DATE NOT NULL,
    locked_price_per_kg DECIMAL(15,2) NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    note VARCHAR(1000),
    status VARCHAR(20) NOT NULL,
    created_by BIGINT,
    updated_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_lock_supplier
        FOREIGN KEY (supplier_id) REFERENCES supplier (supplier_id),
    CONSTRAINT fk_lock_item
        FOREIGN KEY (item_id) REFERENCES item (item_id),
    CONSTRAINT fk_lock_creator
        FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT fk_lock_updater
        FOREIGN KEY (updated_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_lock_volume CHECK (locked_volume > 0),
    CONSTRAINT chk_lock_price CHECK (locked_price_per_kg > 0),
    CONSTRAINT chk_lock_amount CHECK (amount >= 0),
    CONSTRAINT chk_lock_status CHECK (status IN ('DRAFT', 'CONFIRMED'))
) ENGINE = InnoDB;

CREATE TABLE export_ticket (
    export_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    committed_volume DECIMAL(14,3) NOT NULL,
    selling_price_per_kg DECIMAL(15,2) NOT NULL,
    market_price_per_kg DECIMAL(15,2),
    market_difference_per_kg DECIMAL(15,2),
    avg_cost_per_kg DECIMAL(15,2),
    revenue DECIMAL(18,2),
    estimated_profit DECIMAL(18,2),
    export_date DATE NOT NULL,
    delivery_due_date DATE,
    note VARCHAR(1000),
    status VARCHAR(30) NOT NULL,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_export_customer
        FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT fk_export_item
        FOREIGN KEY (item_id) REFERENCES item (item_id),
    CONSTRAINT fk_export_creator
        FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT fk_export_updater
        FOREIGN KEY (updated_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_export_volume CHECK (committed_volume > 0),
    CONSTRAINT chk_export_price CHECK (selling_price_per_kg > 0),
    CONSTRAINT chk_export_status CHECK (
        status IN (
            'DRAFT', 'CONFIRMED', 'PARTIALLY_DELIVERED',
            'COMPLETED', 'CANCELLED'
        )
    )
) ENGINE = InnoDB;

CREATE TABLE export_delivery (
    delivery_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    export_id BIGINT NOT NULL,
    delivered_volume DECIMAL(14,3) NOT NULL,
    delivery_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    note VARCHAR(1000),
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_delivery_export
        FOREIGN KEY (export_id) REFERENCES export_ticket (export_id),
    CONSTRAINT fk_delivery_creator
        FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_delivery_volume CHECK (delivered_volume > 0),
    CONSTRAINT chk_delivery_status CHECK (status IN ('CONFIRMED', 'CANCELLED'))
) ENGINE = InnoDB;

CREATE TABLE expense (
    expense_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total_amount DECIMAL(18,2) NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    note VARCHAR(1000),
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_expense_creator
        FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT fk_expense_updater
        FOREIGN KEY (updated_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_expense_amount CHECK (total_amount >= 0),
    CONSTRAINT chk_expense_period CHECK (period_end >= period_start)
) ENGINE = InnoDB;

CREATE TABLE expense_detail (
    expense_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    expense_id BIGINT NOT NULL,
    category_name VARCHAR(100) NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    CONSTRAINT fk_expense_detail_expense
        FOREIGN KEY (expense_id) REFERENCES expense (expense_id),
    CONSTRAINT uk_expense_category UNIQUE (expense_id, category_name),
    CONSTRAINT chk_expense_detail_amount CHECK (amount >= 0)
) ENGINE = InnoDB;

CREATE TABLE market_price (
    market_price_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT NOT NULL,
    price_date DATE NOT NULL,
    price_per_kg DECIMAL(15,2) NOT NULL,
    source VARCHAR(255),
    fetched_at DATETIME,
    CONSTRAINT fk_market_price_item
        FOREIGN KEY (item_id) REFERENCES item (item_id),
    CONSTRAINT uk_market_price_item_date UNIQUE (item_id, price_date),
    CONSTRAINT chk_market_price_value CHECK (price_per_kg > 0)
) ENGINE = InnoDB;

CREATE INDEX idx_import_ticket_date ON import_ticket (import_date);
CREATE INDEX idx_import_ticket_supplier ON import_ticket (supplier_id);
CREATE INDEX idx_import_ticket_item ON import_ticket (item_id);
CREATE INDEX idx_import_weight_import ON import_weight (import_id);
CREATE INDEX idx_price_lock_supplier ON price_lock (supplier_id);
CREATE INDEX idx_price_lock_item ON price_lock (item_id);
CREATE INDEX idx_export_ticket_customer ON export_ticket (customer_id);
CREATE INDEX idx_export_ticket_item ON export_ticket (item_id);
CREATE INDEX idx_export_delivery_export ON export_delivery (export_id);
CREATE INDEX idx_expense_period ON expense (period_start, period_end);
CREATE INDEX idx_market_price_item_date ON market_price (item_id, price_date);
