-- =========================================================================
-- 1. 強制清空舊表，確保重新倒資料（注意外鍵順序：有連動的 appointment 要先刪）
-- =========================================================================
DROP TABLE IF EXISTS appointment;
DROP TABLE IF EXISTS patient;
DROP TABLE IF EXISTS doctor;

-- =========================================================================
-- 2. 建立符合雲端 Java 實體（Entity）結構的單數形資料表
-- =========================================================================

-- 建立 醫師表 (doctor)
CREATE TABLE doctor (
    doctor_id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    department VARCHAR(255),       -- 科別（直接存名稱，如 '家醫科'）
    specialty VARCHAR(255),        -- 專長
    password_hash VARCHAR(255)
);

-- 建立 病患表 (patient)
CREATE TABLE patient (
    chart_no VARCHAR(255) PRIMARY KEY, -- 病歷號
    name VARCHAR(255),
    gender VARCHAR(255),
    birth_date DATE,
    phone VARCHAR(255)
);

-- 建立 掛號表 (appointment)
CREATE TABLE appointment (
    appt_id SERIAL PRIMARY KEY,    -- 掛號序號（自動遞增）
    chart_no VARCHAR(255),         -- 對應病患的病歷號
    doctor_id VARCHAR(255),        -- 對應醫師的 ID
    appt_date DATE,                -- 掛號日期
    time_slot VARCHAR(255),        -- 時段 ('AM', 'PM' 或具體時間)
    status VARCHAR(255)            -- 狀態 (如 'BOOKED')
);

-- =========================================================================
-- 3. 完美同步 data.sql 的內容（將資料結構轉換為符合單數表的格式）
-- =========================================================================

-- 同步醫師資料：把原本 departments 的科別名稱合併進來
INSERT INTO doctor (doctor_id, name, department, specialty, password_hash) VALUES
    ('D001', '陳志明醫師', '家醫科', '一般內科、慢性病管理', '$2a$10$XhyEgd4qh5TXJa7NkMg3gOqsJxATykAyJERH7ZqTD7eEPVlcmgewm'),
    ('D002', '林佩君醫師', '內科',   '心臟血管、高血壓',     '$2a$10$/x/fVm66HZJWeeYZRUbPp..gS9Czgs3a27RjYQPs75obpRoUWU9ZC'),
    ('D003', '王建華醫師', '復健科', '運動傷害、脊椎復健',   '$2a$10$4fZBPZq1NJmqW5MUgOUsqukV6OiTJutAKR/WbiFiQ6PRTjFbNsMFy'),
    ('D004', '李美玲醫師', '小兒科', '兒童感冒、疫苗接種',   '$2a$10$ZlsUgEo2MOm0RYxwcP55qukrjipEXYNKyyRfdIKkOEv7RpuXEPhxK'),
    ('D005', '張雅筑醫師', '身心科', '焦慮、失眠、情緒調適', '$2a$10$XsgY9Cmk7PqJ2pve2k4xwuTnV/hakC6LOGJqicQyjH.wDiM7PQhWa');

-- 同步病患資料：使用 data.sql 的 'TEST0000X' 格式
INSERT INTO patient (chart_no, name, gender, birth_date, phone) VALUES
    ('TEST00001', '測試病患甲', '男', '1985-03-15', '0912-345-678'),
    ('TEST00002', '王小明',     '男', '1990-07-22', '0923-456-789'),
    ('TEST00003', '李小華',     '女', '1988-11-30', '0934-567-890');

-- 同步掛號資料：將時間欄位轉換回你的單數表欄位型態
INSERT INTO appointment (appt_id, chart_no, doctor_id, appt_date, time_slot, status) VALUES
    (1, 'TEST00001', 'D001', '2026-05-01', 'AM', 'BOOKED'),
    (2, 'TEST00002', 'D002', '2026-05-01', 'AM', 'BOOKED'),
    (3, 'TEST00003', 'D003', '2026-05-02', 'PM', 'BOOKED');

-- =========================================================================
-- 4. 重設自動遞增序列防噴錯機制
-- =========================================================================
SELECT setval(pg_get_serial_sequence('appointment', 'appt_id'),
              COALESCE((SELECT MAX(appt_id) FROM appointment), 0) + 1, false);