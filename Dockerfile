# Этот этап собирает приложение
FROM golang:1.17-alpine as builder

WORKDIR /app

# Копируем go.mod и go.sum и скачиваем зависимости
COPY go.mod go.sum ./
RUN go mod download

# Копируем все остальные файлы
COPY . .

# Собираем приложение и сохраняем вывод
RUN go build -o app 2>&1 | tee build.log

# Этот этап собирает конечный образ
FROM alpine:latest

WORKDIR /app

# Копируем исполняемый файл из предыдущего этапа
COPY --from=builder /app/app .

# Указываем порт, который будет прослушивать приложение
EXPOSE 8080

# Команда для запуска приложения при старте контейнера
CMD ["./app"]
