FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/data-storage-service-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yml ./application.yml
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:./application.yml"]
