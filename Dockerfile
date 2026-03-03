# ── Stage 1: Build with Maven ──
FROM maven:3.8.7-openjdk-18-slim AS builder

WORKDIR /build
COPY pom.xml .
COPY src ./src

# Run tests and package the application
RUN mvn clean package -q

# ── Stage 2: Runtime ──
FROM openjdk:18-jdk-slim

WORKDIR /app

COPY --from=builder /build/target/scientific-calculator-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
