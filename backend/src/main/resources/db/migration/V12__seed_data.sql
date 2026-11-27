-- V12: Seed
INSERT INTO role (role_name) VALUES ('ADMIN'), ('WEIGHER'), ('ACCOUNTANT');

INSERT INTO app_user (username, password_hash, full_name, role_id)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'System administrator', 1);

INSERT INTO warehouse (code, name, address) VALUES
('KHO1', 'Kho chính', 'Buôn Drai, Xã Ea Na, Tỉnh Đăk Lăk'),
('KHO2', 'Kho 2', 'Buôn Drai, Xã Ea Na, Tỉnh Đăk Lăk');

INSERT INTO item (name, category, unit) VALUES
('Cà phê nhân Loại 1', 'Loại 1', 'KG'),
('Cà phê nhân Loại 2', 'Loại 2', 'KG'),
('Tiêu đen', 'Loại 1', 'KG');

INSERT INTO expense_category (category_name) VALUES
('Nhân công'), ('Vận chuyển'), ('Điện nước'), ('Khác');