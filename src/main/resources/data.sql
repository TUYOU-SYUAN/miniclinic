INSERT OR IGNORE INTO departments (name) VALUES ('家醫科');
INSERT OR IGNORE INTO departments (name) VALUES ('內科');
INSERT OR IGNORE INTO departments (name) VALUES ('復健科');
INSERT OR IGNORE INTO departments (name) VALUES ('小兒科');
INSERT OR IGNORE INTO departments (name) VALUES ('身心科');

-- The following INSERT statement for the 'doctors' table has been modified
-- to include the 'password_hash' column as requested.
--
-- IMPORTANT: For this change to work correctly, you must ensure that your
-- 'doctors' table DDL (Data Definition Language, typically in a schema.sql file
-- or defined by your ORM entity) includes a 'password_hash' column,
-- for example: `password_hash VARCHAR(255) NOT NULL`.
-- The password hash values are taken from the 'doctor' (singular) table inserts
-- found later in this file.
INSERT OR IGNORE INTO doctors (id, name, department_id, password_hash) VALUES
    ('D001', '陳志明醫師', (SELECT id FROM departments WHERE name = '家醫科'), '$2a$10$XhyEgd4qh5TXJa7NkMg3gOqsJxATykAyJERH7ZqTD7eEPVlcmgewm'),
    ('D002', '林佩君醫師', (SELECT id FROM departments WHERE name = '內科'), '$2a$10$/x/fVm66HZJWeeYZRUbPp..gS9Czgs3a27RjYQPs75obpRoUWU9ZC'),
    ('D003', '王建華醫師', (SELECT id FROM departments WHERE name = '復健科'), '$2a$10$4fZBPZq1NJmqW5MUgOUsqukV6OiTJutAKR/WbiFiQ6PRTjFbNsMFy'),
    ('D004', '李美玲醫師', (SELECT id FROM departments WHERE name = '小兒科'), '$2a$10$ZlsUgEo2MOm0RYxwcP55qukrjipEXYNKyyRfdIKkOEv7RpuXEPhxK'),
    ('D005', '張雅筑醫師', (SELECT id FROM departments WHERE name = '身心科'), '$2a$10$XsgY9Cmk7PqJ2pve2k4xwuTnV/hakC6LOGJqicQyjH.wDiM7PQhWa');
    
-- The 'patients' table currently only has 'id' (integer) and 'name' based on DDL.
-- Modified to use "TEST" + five-digit number format for patient IDs.
INSERT OR IGNORE INTO patients (id, name) VALUES
    ('TEST00001', '測試病患甲'),
    ('TEST00002', '王小明'),
    ('TEST00003', '李小華');

-- The 'appointments' table has 'id', 'appointment_date', 'appointment_time', 'doctor_id', 'patient_id'.
-- 'chart_no' from original data.sql is mapped to 'patient_id' (integer ID from 'patients' table).
-- 'time_slot' ('AM', 'PM') is mapped to 'appointment_time' (e.g., '09:00:00', '14:00:00').
-- 'status' is not present in the current DDL and is omitted.
INSERT OR IGNORE INTO appointments (id, patient_id, doctor_id, appointment_date, appointment_time, status) VALUES
    (1, 'TEST00001', 'D001', '2026-05-01', '09:00:00', 'BOOKED'),
    (2, 'TEST00002', 'D002', '2026-05-01', '09:00:00', 'BOOKED'),
    (3, 'TEST00003', 'D003', '2026-05-02', '14:00:00', 'BOOKED');    