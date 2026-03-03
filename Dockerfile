# Use a lightweight JRE image matching the project's Java 11 target
FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/scientific-calculator-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
