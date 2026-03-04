# syntax=docker/dockerfile:1
# ── Stage 1: Build with Maven ──
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy pom.xml and resolve dependencies.
# The BuildKit cache mount persists ~/.m2 across builds — no re-downloading!
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -q

# Copy source and package. Skip tests — Jenkins runs them in a dedicated stage.
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests -q

# ── Stage 2: Runtime ──
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /build/target/scientific-calculator-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
