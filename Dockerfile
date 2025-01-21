# Stage 1: Build
FROM openjdk:17-jdk-slim AS builder

# Set the working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle/

# Ensure gradlew is executable
RUN chmod +x gradlew

# Download dependencies and build the application (skip tests to speed up the build)
RUN ./gradlew clean build -x test --no-daemon

# Copy the source code and build the application
COPY src /app/src
RUN ./gradlew bootJar --no-daemon

# Stage 2: Runtime
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/Auth-Service-0.0.1-SNAPSHOT.jar app.jar

# Expose the application's port
EXPOSE 8081

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
