-- V9: Sổ vay
CREATE TABLE loan_contract (
    loan_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(30) NOT NULL,
    loan_type VARCHAR(10) NOT NULL,
    party_type VARCHAR(10) NOT NULL,
    party_id BIGINT NOT NULL,
    principal_amount DECIMAL(18,2) NOT NULL,
    interest_rate DECIMAL(8,4) NOT NULL,
    start_date DATE NOT NULL,
    due_date DATE,
    outstanding_principal DECIMAL(18,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_loan_code UNIQUE (code),
    CONSTRAINT fk_loan_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_loan_type CHECK (loan_type IN ('LENDING', 'BORROWING')),
    CONSTRAINT chk_loan_party CHECK (party_type IN ('CUSTOMER', 'SUPPLIER', 'BANK', 'EMPLOYEE')),
    CONSTRAINT chk_loan_principal CHECK (principal_amount > 0),
    CONSTRAINT chk_loan_status CHECK (status IN ('ACTIVE', 'CLOSED', 'OVERDUE'))
) ENGINE = InnoDB;

CREATE TABLE loan_transaction (
    loan_txn_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id BIGINT NOT NULL,
    txn_type VARCHAR(30) NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    txn_date DATE NOT NULL,
    interest_days INT,
    note VARCHAR(1000),
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_loan_txn_loan FOREIGN KEY (loan_id) REFERENCES loan_contract (loan_id),
    CONSTRAINT fk_loan_txn_creator FOREIGN KEY (created_by) REFERENCES app_user (user_id),
    CONSTRAINT chk_loan_txn_type CHECK (txn_type IN ('DISBURSEMENT', 'PRINCIPAL_PAYMENT', 'INTEREST_RECEIPT', 'INTEREST_PAYMENT')),
    CONSTRAINT chk_loan_txn_amount CHECK (amount > 0)
) ENGINE = InnoDB;

CREATE INDEX idx_loan_party ON loan_contract (party_type, party_id);
CREATE INDEX idx_loan_txn_loan ON loan_transaction (loan_id);