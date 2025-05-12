FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build
COPY backend/pom.xml ../pom.xml

COPY backend/script-runner-service/pom.xml ./pom.xml
COPY backend/script-runner-service/src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y curl bash && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY backend/scripts/manual-input.sh /scripts/manual-input.sh
COPY backend/scripts/auto-test.sh /scripts/auto-test.sh
RUN chmod +x /scripts/*.sh

COPY --from=build /build/target/script-runner-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]