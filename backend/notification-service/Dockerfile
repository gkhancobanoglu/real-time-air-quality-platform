FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

COPY backend/pom.xml ../pom.xml

COPY backend/notification-service/pom.xml ./pom.xml
COPY backend/notification-service/src ./src

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /build/target/notification-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
