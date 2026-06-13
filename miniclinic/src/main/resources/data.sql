-- 關閉外鍵檢查以避免初始化時的約束衝突
-- 初始醫師資料（5 位）帶入 pass1234 的 BCrypt 雜湊
INSERT INTO doctor (doctor_id, name, department, specialty, password_hash) VALUES
    ('D001', '陳志明醫師', '家醫科', '一般內科、慢性病管理','$2a$10$XhyEgd4qh5TXJa7NkMg3gOqsJxATykAyJERH7ZqTD7eEPVlcmgewm'),
    ('D002', '林佩君醫師', '內科',   '心臟血管、高血壓', '$2a$10$/x/fVm66HZJWeeYZRUbPp..gS9Czgs3a27RjYQPs75obpRoUWU9ZC'),
    ('D003', '王建華醫師', '復健科', '運動傷害、脊椎復健', '$2a$10$4fZBPZq1NJmqW5MUgOUsqukV6OiTJutAKR/WbiFiQ6PRTjFbNsMFy'),
    ('D004', '李美玲醫師', '小兒科', '兒童感冒、疫苗接種',  '$2a$10$ZlsUgEo2MOm0RYxwcP55qukrjipEXYNKyyRfdIKkOEv7RpuXEPhxK'),
    ('D005', '張雅筑醫師', '身心科', '焦慮、失眠、情緒調適', '$2a$10$XsgY9Cmk7PqJ2pve2k4xwuTnV/hakC6LOGJqicQyjH.wDiM7PQhWa')
ON CONFLICT(doctor_id) DO UPDATE SET 
    name=excluded.name, 
    department=excluded.department, 
    specialty=excluded.specialty, 
    password_hash=excluded.password_hash;

-- 初始病患資料（更新為隨機虛擬數據）
INSERT INTO patient (chart_no, name, gender, birth_date, phone) VALUES
    ('TEST00001', '張嘉明', '男', '1995-05-20', '0912-345-678'),
    ('TEST00002', '林秋燕', '女', '1988-11-12', '0921-888-777'),
    ('TEST00003', '陳俊宏', '男', '2002-01-01', '0933-555-666')
ON CONFLICT(chart_no) DO UPDATE SET 
    name=excluded.name, 
    gender=excluded.gender, 
    birth_date=excluded.birth_date, 
    phone=excluded.phone;

-- 初始掛號資料
INSERT INTO appointment (appt_id, chart_no, doctor_id, appt_date, time_slot, status) VALUES
    (1, 'TEST00001', 'D001', '2026-05-01', 'AM', 'BOOKED'),
    (2, 'TEST00002', 'D002', '2026-05-01', 'AM', 'BOOKED'),
    (3, 'TEST00003', 'D003', '2026-05-02', 'PM', 'BOOKED')
ON CONFLICT(appt_id) DO NOTHING;