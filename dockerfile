# ---------- Stage 1: Build ----------
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy only required files first (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests


# ---------- Stage 2: Run ----------
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy built jar
COPY --from=builder /app/target/*.jar app.jar

# Render uses dynamic port
ENV PORT=8080
EXPOSE 8080

# Important for Render
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]