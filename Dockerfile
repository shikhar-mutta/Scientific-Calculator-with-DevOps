# ── Stage 1: Build with Maven ──
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy pom.xml FIRST and resolve all dependencies.
# This layer is cached and only re-runs when pom.xml changes.
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Now copy source code and build.
# This layer re-runs only when source code changes (fast, no downloads).
COPY src ./src
RUN mvn clean package -o -q

# ── Stage 2: Runtime ──
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /build/target/scientific-calculator-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
