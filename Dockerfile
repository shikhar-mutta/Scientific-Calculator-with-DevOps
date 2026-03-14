# Stage 1: Build JAR
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build
# cd /build

# Cache dependency layer — only re-runs when pom.xml changes
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Build offline using cached deps
COPY src ./src
RUN mvn clean package -DskipTests -o -q

# Stage 2: Custom minimal JRE via jlink (musl/Alpine-compatible)
# IMPORTANT: Must use the Alpine variant so jlink output is musl-compatible
FROM eclipse-temurin:17-jdk-alpine AS jre-builder

RUN $JAVA_HOME/bin/jlink \
    --add-modules java.base,java.logging \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /custom-jre

# Stage 3: Bare Alpine + custom musl-compatible JRE 
FROM alpine:3.19

ENV JAVA_HOME=/opt/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Copy only the tiny custom JRE from the previous stage
COPY --from=jre-builder /custom-jre $JAVA_HOME

# Create non-root user (Alpine syntax)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --from=builder /build/target/scientific-calculator-1.0-SNAPSHOT.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]
