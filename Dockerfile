# 第一階段：用 Maven 編譯出 JAR
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# 把所有東西複製進來（包含 miniclinic 資料夾）
COPY . .

# 【關鍵修正】切換進去真正有 pom.xml 的子資料夾
WORKDIR /app/miniclinic
RUN mvn clean package -DskipTests

# 第二階段：只帶著 JAR 執行，image 較小
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 【關鍵修正】從第一階段的子資料夾 target 裡把 JAR 檔拿出來
COPY --from=build /app/miniclinic/target/miniclinic-0.0.1-SNAPSHOT.jar app.jar

# 複製資料庫（確保 SQLite 能被讀到）
COPY --from=build /app/miniclinic.db ./miniclinic.db

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
