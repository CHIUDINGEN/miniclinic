-- 初始醫師資料（5 位）帶入 pass1234 的 BCrypt 雜湊
INSERT OR IGNORE INTO doctor (doctor_id, name, department, specialty, password_hash) VALUES
    ('D001', '陳志明醫師', '家醫科', '一般內科、慢性病管理','$2a$10$XhyEgd4qh5TXJa7NkMg3gOqsJxATykAyJERH7ZqTD7eEPVlcmgewm'),
    ('D002', '林佩君醫師', '內科',   '心臟血管、高血壓', '$2a$10$/x/fVm66HZJWeeYZRUbPp..gS9Czgs3a27RjYQPs75obpRoUWU9ZC'),
    ('D003', '王建華醫師', '復健科', '運動傷害、脊椎復健', '$2a$10$4fZBPZq1NJmqW5MUgOUsqukV6OiTJutAKR/WbiFiQ6PRTjFbNsMFy'),
    ('D004', '李美玲醫師', '小兒科', '兒童感冒、疫苗接種',  '$2a$10$ZlsUgEo2MOm0RYxwcP55qukrjipEXYNKyyRfdIKkOEv7RpuXEPhxK'),
    ('D005', '張雅筑醫師', '身心科', '焦慮、失眠、情緒調適', '$2a$10$XsgY9Cmk7PqJ2pve2k4xwuTnV/hakC6LOGJqicQyjH.wDiM7PQhWa');

-- 初始病患資料（3 位虛構病患）
INSERT OR IGNORE INTO patient (chart_no, name, gender, birth_date, phone) VALUES
    ('TEST00001', '王韋澔', '男', '2007-01-06', '0966-852-517'),
    ('TEST00002', '李彥諄', '男', '2007-03-05', '0979-200-305'),
    ('TEST00003', '吳宇涵', '女', '2007-03-01', '0988-540-334');

-- 初始掛號資料（appt_id 必須明確指定，INSERT OR IGNORE 才能防止重複，見 13.4 說明）
INSERT OR IGNORE INTO appointment (appt_id, chart_no, doctor_id, appt_date, time_slot, status) VALUES
    (1, 'TEST00001', 'D001', '2026-05-01', 'AM', 'BOOKED'),
    (2, 'TEST00002', 'D002', '2026-05-01', 'AM', 'BOOKED'),
    (3, 'TEST00003', 'D003', '2026-05-02', 'PM', 'BOOKED');