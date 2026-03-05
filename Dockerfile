# ── Stage 1: Build JAR ──────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# Cache dependency layer — only re-runs when pom.xml changes
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Build offline using cached deps
COPY src ./src
RUN mvn clean package -DskipTests -o -q

# ── Stage 2: Minimal runtime (Alpine-native JRE) ─────────────────────
# eclipse-temurin:17-jre-alpine uses musl-compatible binaries — no jlink needed
FROM eclipse-temurin:17-jre-alpine

# Create non-root user (Alpine syntax)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --from=builder /build/target/scientific-calculator-1.0-SNAPSHOT.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]
