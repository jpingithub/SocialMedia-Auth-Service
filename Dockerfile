# Build stage
FROM gradle:8.2.1-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/SocialMedia-Auth-Service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]