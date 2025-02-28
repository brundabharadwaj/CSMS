CREATE TABLE IF NOT EXISTS authentication (
    station_uuid UUID PRIMARY KEY NOT NULL,
    driver_id VARCHAR(255) NOT NULL
);

INSERT INTO authentication (station_uuid, driver_id) VALUES ('550e8400-e29b-41d4-a716-446655440000', 'f47ac10b-58cc-4372-a567-0e02b2c3d479');
INSERT INTO authentication (station_uuid, driver_id) VALUES ('6a1b2c3d-4e5f-6789-abcd-ef0123456789', '8f14e45f-ea7f-4386-9b47-8cc57e5a9f83');
INSERT INTO authentication (station_uuid, driver_id) VALUES ('123e4567-e89b-12d3-a456-426614174000', '9a7f42b4-f7fc-4528-97d1-1f22e2bb8232');
INSERT INTO authentication (station_uuid, driver_id) VALUES ('9f8e7d6c-5b4a-3210-fedc-ba9876543210', 'ae2d7f95-973b-42a3-8f59-6f29e6b8f5e5');
INSERT INTO authentication (station_uuid, driver_id) VALUES ('3b2a1c4d-5e6f-7890-abcd-ef0987654321', 'b52a9682-c31f-485f-b7d7-8579e7e94487');
