# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set a working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY build/libs/Auth-Service-0.0.1-SNAPSHOT.jar app.jar

# Expose the application's port (match it with the port configured in your app)
EXPOSE 8080

# Specify the command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
