FROM maven:3.8.2-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Этап упаковки
FROM eclipse-temurin:17-alpine
WORKDIR /app
COPY --from=build /app/target/SpringDemoBotParfums-0.0.1-SNAPSHOT.jar SpringDemoBotParfums.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "SpringDemoBotParfums.jar"]


