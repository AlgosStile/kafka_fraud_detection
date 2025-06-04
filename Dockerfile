# Базовый образ
FROM eclipse-temurin:17-jdk-alpine

# Рабочая директория
WORKDIR /app

# 1. Копируем JAR-файл
COPY target/fraud-detection-*.jar app.jar

# 2. Копируем SSL-сертификаты
COPY docker/ssl /app/ssl

# Порт приложения
EXPOSE 8080

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]