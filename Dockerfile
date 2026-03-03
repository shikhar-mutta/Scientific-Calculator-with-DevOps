# ── Stage 1: Build with Maven ──
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build
COPY pom.xml .
COPY src ./src

# Run tests and package the application
RUN mvn clean package -q

# ── Stage 2: Runtime ──
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /build/target/scientific-calculator-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
