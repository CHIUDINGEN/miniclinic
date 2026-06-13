# MiniClinic - 診所管理系統

MiniClinic 是一個基於 Spring Boot 3.4.1 開發的輕量級診所管理系統。本系統旨在提供醫師一個簡潔的介面來管理病患預約、查看診所即時營運數據，並維護醫師與病患資料。

**🔗 線上演示:** [https://miniclinic-chiudingen.onrender.com/](https://miniclinic-chiudingen.onrender.com/)

---

## 🚀 核心功能

### 1. 醫師管理後台 (需登入)
*   **個人儀表板**: 查看當日醫師專屬預約清單，支援時區同步（Asia/Taipei）。
*   **掛號狀態管理**: 醫師可即時更新掛號狀態為「已完成」或「已取消」。
*   **安全性**: 密碼採用 BCrypt 強力雜湊加密，確保登入安全。

### 2. 營運數據可視化
*   **數據統計面版**: 即時計算醫師總數、病患總數、掛號總量。
*   **科別分佈統計**: 自動分析各科別（如：家醫科、內科）的預約分佈狀況。
*   **REST API**: 提供 `/api/stats` 等接口，回傳 JSON 格式統計摘要。

### 3. 病患與掛號流程
*   **醫師/病患清單**: 支援依科別篩選醫師，並提供病歷摘要查看。
*   **掛號預約系統**: 具備表單驗證與衝突檢查的掛號流程。
*   **資料保護**: 提供虛擬隨機生成的病患數據，保護測試隱私。

---

## 🛠️ 技術棧

*   **後端**: Java 17, Spring Boot 3.4.1
*   **持久層**: Spring Data JPA (Hibernate)
*   **資料庫**: 
    *   **開發環境 (Dev)**: SQLite (檔案：`miniclinic.db`)
    *   **生產環境 (Prod)**: PostgreSQL (部署於 Render)
*   **樣板引擎**: Thymeleaf (UTF-8 全域編碼修正)
*   **安全性**: Spring Security Crypto (BCrypt)
*   **建置工具**: Maven

---

## 📦 快速開始

### 環境需求
*   JDK 17+
*   Maven 3.6+

### 本地執行步驟
1.  **複製專案**:
    ```bash
    git clone <your-repo-url>
    cd miniclinic
    ```
2.  **編譯並執行**:
    ```bash
    mvn spring-boot:run
    ```
3.  **瀏覽網頁**: 開啟 `http://localhost:8080`

---

## ⚙️ 配置說明

*   **編碼規範**: 專案全面強制使用 **UTF-8** 編碼（含 Maven, SQL 腳本, Thymeleaf 與 Servlet 回傳內容），徹底解決中文亂碼問題。
*   **環境切換**:
    *   `application-dev.properties`: 適用於本地開發。
    *   `application-prod.properties`: 適用於雲端部署（自動讀取 `data-prod.sql` 初始化資料）。
*   **DI 規範**: 全面採用 **建構子注入 (Constructor Injection)**，取代舊有的 `@Autowired` 欄位注入，提升程式碼可測試性與穩定性。

---

## 📝 測試帳號

| 醫師編號 | 預設密碼 | 權限內容 |
| :--- | :--- | :--- |
| D001 | pass1234 | 陳志明醫師 (家醫科) |
| D002 | pass1234 | 林佩君醫師 (內科) |
| D003 | pass1234 | 王建華醫師 (復健科) |
