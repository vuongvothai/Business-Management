ALTER TABLE import_weight
    DROP CHECK chk_weight_bags,
    ADD CONSTRAINT chk_weight_bags CHECK (bag_count >= 1);

ALTER TABLE import_ticket
    ADD COLUMN vehicle_id BIGINT NULL AFTER item_id,
    ADD COLUMN truck_license_plate VARCHAR(30) NULL AFTER vehicle_id,
    ADD COLUMN loading_warehouse VARCHAR(255) NULL AFTER truck_license_plate,
    ADD COLUMN import_warehouse VARCHAR(255) NULL AFTER loading_warehouse,
    ADD COLUMN tare_weight DECIMAL(12,3) NULL AFTER import_warehouse,
    ADD COLUMN sample_test_result VARCHAR(255) NULL AFTER tare_weight,
    ADD COLUMN total_amount DECIMAL(18,2) NULL AFTER market_difference_per_kg,
    ADD COLUMN total_deduction_amount DECIMAL(18,2) NULL AFTER total_amount,
    ADD CONSTRAINT fk_import_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle (vehicle_id),
    ADD CONSTRAINT chk_import_tare CHECK (tare_weight IS NULL OR tare_weight >= 0);

ALTER TABLE export_delivery
    ADD COLUMN vehicle_id BIGINT NULL AFTER export_id,
    ADD COLUMN truck_license_plate VARCHAR(30) NULL AFTER vehicle_id,
    ADD COLUMN loading_warehouse VARCHAR(255) NULL AFTER truck_license_plate,
    ADD COLUMN import_warehouse VARCHAR(255) NULL AFTER loading_warehouse,
    ADD CONSTRAINT fk_delivery_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle (vehicle_id);

CREATE INDEX idx_import_ticket_vehicle ON import_ticket (vehicle_id);
CREATE INDEX idx_export_delivery_vehicle ON export_delivery (vehicle_id);
