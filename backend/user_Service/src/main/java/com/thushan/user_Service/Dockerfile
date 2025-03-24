# Use OpenJDK 21 slim image
FROM openjdk:21-jdk-slim

# Set metadata
LABEL authors="D.Thushan Madushara"

# Set working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/user_Service-0.0.1-SNAPSHOT.jar user-service.jar

# Expose the port for the service
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "user-service.jar"]
