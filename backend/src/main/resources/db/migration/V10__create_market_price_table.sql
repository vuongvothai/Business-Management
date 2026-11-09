CREATE TABLE market_price (
    market_price_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT NOT NULL,
    price_date DATE NOT NULL,
    price_per_kg DECIMAL(15,2) NOT NULL,
    source VARCHAR(255),
    fetched_at DATETIME,
    CONSTRAINT fk_market_price_item FOREIGN KEY (item_id) REFERENCES item (item_id),
    CONSTRAINT uk_market_price_item_date UNIQUE (item_id, price_date),
    CONSTRAINT chk_market_price_value CHECK (price_per_kg > 0)
) ENGINE = InnoDB;

CREATE INDEX idx_market_price_item_date ON market_price (item_id, price_date);
