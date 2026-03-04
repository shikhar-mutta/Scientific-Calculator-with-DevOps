# ── Stage 1: Build ──────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# 1. Copy only pom.xml first — dependency layer is cached separately.
#    Only re-runs when pom.xml changes (not on every source change).
COPY pom.xml .
RUN mvn dependency:go-offline -q

# 2. Copy source and build in offline mode (uses cached deps above).
COPY src ./src
RUN mvn clean package -DskipTests -o -q

# ── Stage 2: Runtime ─────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-jammy

# Create a non-root user for security
RUN groupadd --system appgroup && useradd --system --gid appgroup appuser

WORKDIR /app

COPY --from=builder /build/target/scientific-calculator-1.0-SNAPSHOT.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]
