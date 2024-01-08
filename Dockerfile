# Этот этап собирает приложение
FROM maven:3.8.2-openjdk-17 AS build

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем все файлы из текущего каталога в рабочую директорию
COPY . .

# Выполняем сборку Maven проекта, пропуская тесты
RUN mvn clean package -DskipTests

# Этот этап собирает конечный образ
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR-файл из предыдущего этапа
COPY --from=build /app/target/SpringDemoBotParfums-0.0.1-SNAPSHOT.jar .

# Указываем порт, который будет прослушивать приложение
EXPOSE 8080

# Устанавливаем переменную окружения (если нужно)
# ENV PORT=8080

# Команда для запуска приложения при старте контейнера
ENTRYPOINT ["java", "-jar", "SpringDemoBotParfums-0.0.1-SNAPSHOT.jar"]
