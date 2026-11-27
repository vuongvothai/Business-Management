-- V7: Tiền, chi phí, công nợ
CREATE TABLE payment_voucher (
    voucher_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(30) NOT NULL,
    voucher_type VARCHAR(10) NOT NULL,
    party_type VARCHAR(10) NOT NULL,
    party_id BIGINT NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    voucher_date DATE NOT NULL,
    note VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_voucher_code UNIQUE (code),
    CONSTRAINT fk_voucher_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_voucher_type CHECK (voucher_type IN ('RECEIPT', 'PAYMENT')),
    CONSTRAINT chk_voucher_party CHECK (party_type IN ('SUPPLIER', 'CUSTOMER', 'EMPLOYEE', 'BANK')),
    CONSTRAINT chk_voucher_method CHECK (payment_method IN ('CASH', 'BANK')),
    CONSTRAINT chk_voucher_amount CHECK (amount > 0),
    CONSTRAINT chk_voucher_status CHECK (status IN ('DRAFT', 'CONFIRMED', 'CANCELLED'))
) ENGINE = InnoDB;

CREATE TABLE payment_allocation (
    allocation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    voucher_id BIGINT NOT NULL,
    ref_type VARCHAR(30) NOT NULL,
    ref_id BIGINT NOT NULL,
    allocated_amount DECIMAL(18,2) NOT NULL,
    CONSTRAINT fk_alloc_voucher FOREIGN KEY (voucher_id) REFERENCES payment_voucher (voucher_id),
    CONSTRAINT chk_alloc_amount CHECK (allocated_amount > 0)
) ENGINE = InnoDB;

CREATE TABLE expense_category (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_expense_category_name UNIQUE (category_name)
) ENGINE = InnoDB;

CREATE TABLE expense (
    expense_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_method VARCHAR(20) NOT NULL DEFAULT 'CASH',
    total_amount DECIMAL(18,2) NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    note VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_expense_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_expense_amount CHECK (total_amount >= 0),
    CONSTRAINT chk_expense_period CHECK (period_end >= period_start),
    CONSTRAINT chk_expense_status CHECK (status IN ('DRAFT', 'CONFIRMED', 'CANCELLED'))
) ENGINE = InnoDB;

CREATE TABLE expense_detail (
    expense_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    expense_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    note VARCHAR(1000),
    CONSTRAINT fk_ed_expense FOREIGN KEY (expense_id) REFERENCES expense (expense_id),
    CONSTRAINT fk_ed_category FOREIGN KEY (category_id) REFERENCES expense_category (category_id),
    CONSTRAINT uk_ed_category UNIQUE (expense_id, category_id),
    CONSTRAINT chk_ed_amount CHECK (amount >= 0)
) ENGINE = InnoDB;

CREATE TABLE debt_ledger (
    debt_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    party_type VARCHAR(10) NOT NULL,
    party_id BIGINT NOT NULL,
    item_id BIGINT,
    debit_amount DECIMAL(18,2) NOT NULL DEFAULT 0,
    credit_amount DECIMAL(18,2) NOT NULL DEFAULT 0,
    balance_amount DECIMAL(18,2) NOT NULL DEFAULT 0,
    due_date DATE,
    note VARCHAR(1000),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_debt_item FOREIGN KEY (item_id) REFERENCES item (item_id),
    CONSTRAINT chk_debt_party CHECK (party_type IN ('SUPPLIER', 'CUSTOMER'))
) ENGINE = InnoDB;

CREATE INDEX idx_voucher_date ON payment_voucher (voucher_date);
CREATE INDEX idx_voucher_party ON payment_voucher (party_type, party_id);
CREATE INDEX idx_alloc_voucher ON payment_allocation (voucher_id);
CREATE INDEX idx_alloc_ref ON payment_allocation (ref_type, ref_id);
CREATE INDEX idx_expense_period ON expense (period_start, period_end);
CREATE INDEX idx_debt_party ON debt_ledger (party_type, party_id);