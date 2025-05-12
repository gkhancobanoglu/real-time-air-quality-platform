FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

COPY backend/pom.xml ../pom.xml
COPY backend/air-data-service/pom.xml ./pom.xml
COPY backend/air-data-service/src ./src

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /build/target/air-data-service-0.0.1-SNAPSHOT.jar app.jar
COPY backend/air-data-service/src/main/resources/application.yml ./application.yml

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.additional-location=file:./application.yml"]
