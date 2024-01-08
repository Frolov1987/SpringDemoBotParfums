
FROM maven:3.8.20-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Этап упаковки
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/SpringDemoBotParfums-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]
