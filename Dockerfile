
FROM maven:3.8.2-openjdk-17 AS build

COPY . .
RUN mvn clean package -DskipTests

# Этап упаковки
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/SpringDemoBotParfums-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]
