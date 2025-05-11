FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY scripts/manual-input.sh /scripts/manual-input.sh
RUN chmod +x /scripts/manual-input.sh

COPY script-runner-service/target/script-runner-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]