# MiniClinic 社區診所掛號系統

MiniClinic 是一個基於 Spring Boot 3 打造的輕量化診所掛號管理系統。本專案旨在演示如何整合 Web、資料庫、安全驗證以及雲端部署技術，並提供醫師與開發者（透過 API）直觀的互動介面。

## 線上 Demo

*   **部署網址：** `https://miniclinic-[你的Render帳號].onrender.com`
*   **測試帳號：** `D001` / **密碼：** `pass1234`

## 技術棧

*   **核心框架：** Java 17, Spring Boot 3.5.x
*   **持久層：** Spring Data JPA (Hibernate)
*   **資料庫：** SQLite (開發環境) / PostgreSQL (生產環境)
*   **模板引擎：** Thymeleaf
*   **安全性：** BCrypt 密碼雜湊加密, Session-based 登入攔截器
*   **部署：** Docker (Multi-stage build), Render

## 功能清單

### 醫師端
*   **身分驗證：** 安全的醫師登入系統與密碼修改功能。
*   **個人 Dashboard：** 即時查看今日掛號清單。
*   **狀態管理：** 支援一鍵標記「看診完成」或「取消掛號」。

### 掛號與病患
*   **線上掛號：** 提供病患選擇醫師、時段與預約日期。
*   **病患管理：** 支援病患資料的新增與查詢。

### 系統整合 (API)
*   **統計摘要：** 公開的 `/api/stats` 端點，提供全系統運作數據摘要。
*   **掛號管理 API：** 支援第三方系統查詢掛號、新增預約及更新狀態。

## API 端點摘要

| 方法 | 路徑 | 說明 | 認證需求 |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/stats` | 取得系統總覽（醫師、病患、掛號統計） | 無 |
| `GET` | `/api/appointments/count` | 取得掛號總筆數 | 無 |
| `GET` | `/api/appointments` | 依日期或醫師篩選掛號資料 | 無 |
| `PUT` | `/api/appointments/{id}/status` | 更新掛號狀態 (`COMPLETED`/`CANCELLED`) | 醫師登入 |

## 本機執行

### 環境需求
*   Java 17+
*   Maven 3.6+

### 啟動步驟
```bash
# 複製專案
git clone https://github.com/[你的帳號]/miniclinic.git
cd miniclinic

# 使用預設的開發 Profile (SQLite) 啟動
./mvnw spring-boot:run
```

開啟瀏覽器訪問 http://localhost:8080

預設醫師帳密：

- D001 / pass1234
- D002 / pass1234
- （其他醫師密碼均為 pass1234）

## 資料初始化

第一次啟動時，`data.sql` 會自動插入：
- 5 位虛構醫師
- 3 位虛構病患（TEST00001, TEST00002, TEST00003）
- 3 筆示範掛號

## 專案結構

```
src/
├── main/
│   ├── java/tw/edu/fju/miniclinic/
│   │   ├── controller/     # HTTP 請求處理
│   │   ├── model/          # Entity 與 Repository
│   │   ├── interceptor/    # 登入驗證
│   │   └── config/         # Spring 配置
│   └── resources/
│       ├── templates/      # Thymeleaf 模板
│       ├── static/         # CSS、JS
│       └── application.properties
```

## 作者

2026 年 Java 程式設計課程作業

## 聲明

所有病患資料均為虛構，僅供教學使用。