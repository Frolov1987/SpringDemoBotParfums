
FROM maven:3.8.2-jdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /target/SpringDemoBotParfums-0.0.1-SNAPSHOT.jar SpringDemoBotParfums.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","SpringDemoBotParfums.jar"]