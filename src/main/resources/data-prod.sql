-- =========================================================================
-- 1. 強制清空舊表（注意外鍵順序：有連動的表要先刪除）
-- =========================================================================
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS doctors;
DROP TABLE IF EXISTS departments;

-- =========================================================================
-- 2. 建立完全符合 Java Entity 宣告的資料表結構
-- =========================================================================

-- 建立 科別表 (departments)
CREATE TABLE departments (
    id SERIAL PRIMARY KEY,            -- 自動遞增的 ID
    name VARCHAR(255) UNIQUE          -- 科別名稱（家醫科、內科...）
);

-- 建立 醫師表 (doctors) -> 完全對齊你的 Doctor.java
CREATE TABLE doctors (
    id VARCHAR(255) PRIMARY KEY,      -- 對應 private String id;
    name VARCHAR(255),                -- 對應 private String name;
    department_id INT REFERENCES departments(id), -- 對應 department_id 外鍵
    password_hash VARCHAR(100)        -- 對應 password_hash 欄位
);

-- 建立 病患表 (patients)
CREATE TABLE patients (
    id VARCHAR(255) PRIMARY KEY,      -- 對應原本 data.sql 的 TEST00001
    name VARCHAR(255)
);

-- 建立 掛號表 (appointments)
CREATE TABLE appointments (
    id SERIAL PRIMARY KEY,
    patient_id VARCHAR(255) REFERENCES patients(id),
    doctor_id VARCHAR(255) REFERENCES doctors(id),
    appointment_date DATE,
    appointment_time TIME,
    status VARCHAR(255)
);

-- =========================================================================
-- 3. 完美倒入初始資料
-- =========================================================================

-- 插入科別
INSERT INTO departments (name) VALUES ('家醫科');
INSERT INTO departments (name) VALUES ('內科');
INSERT INTO departments (name) VALUES ('復健科');
INSERT INTO departments (name) VALUES ('小兒科');
INSERT INTO departments (name) VALUES ('身心科');

-- 插入醫師：透過子查詢自動去對應剛才建立的 department_id
INSERT INTO doctors (id, name, department_id, password_hash) VALUES
    ('D001', '陳志明醫師', (SELECT id FROM departments WHERE name = '家醫科'), '$2a$10$XhyEgd4qh5TXJa7NkMg3gOqsJxATykAyJERH7ZqTD7eEPVlcmgewm'),
    ('D002', '林佩君醫師', (SELECT id FROM departments WHERE name = '內科'), '$2a$10$/x/fVm66HZJWeeYZRUbPp..gS9Czgs3a27RjYQPs75obpRoUWU9ZC'),
    ('D003', '王建華醫師', (SELECT id FROM departments WHERE name = '復健科'), '$2a$10$4fZBPZq1NJmqW5MUgOUsqukV6OiTJutAKR/WbiFiQ6PRTjFbNsMFy'),
    ('D004', '李美玲醫師', (SELECT id FROM departments WHERE name = '小兒科'), '$2a$10$ZlsUgEo2MOm0RYxwcP55qukrjipEXYNKyyRfdIKkOEv7RpuXEPhxK'),
    ('D005', '張雅筑醫師', (SELECT id FROM departments WHERE name = '身心科'), '$2a$10$XsgY9Cmk7PqJ2pve2k4xwuTnV/hakC6LOGJqicQyjH.wDiM7PQhWa');
    
-- 插入病患
INSERT INTO patients (id, name) VALUES
    ('TEST00001', '測試病患甲'),
    ('TEST00002', '王小明'),
    ('TEST00003', '李小華');

-- 插入掛號
INSERT INTO appointments (id, patient_id, doctor_id, appointment_date, appointment_time, status) VALUES
    (1, 'TEST00001', 'D001', '2026-05-01', '09:00:00', 'BOOKED'),
    (2, 'TEST00002', 'D002', '2026-05-01', '09:00:00', 'BOOKED'),
    (3, 'TEST00003', 'D003', '2026-05-02', '14:00:00', 'BOOKED');    

-- 重設自動遞增序列
SELECT setval(pg_get_serial_sequence('appointments', 'id'),
              COALESCE((SELECT MAX(id) FROM appointments), 0) + 1, false);