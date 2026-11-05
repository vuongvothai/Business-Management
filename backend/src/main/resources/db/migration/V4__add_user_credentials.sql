ALTER TABLE app_user
    ADD COLUMN username VARCHAR(100),
    ADD COLUMN password_hash VARCHAR(100);

UPDATE app_user
SET username = 'admin',
    password_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE full_name = 'System administrator';

ALTER TABLE app_user
    MODIFY username VARCHAR(100) NOT NULL,
    MODIFY password_hash VARCHAR(100) NOT NULL,
    ADD CONSTRAINT uk_app_user_username UNIQUE (username);
