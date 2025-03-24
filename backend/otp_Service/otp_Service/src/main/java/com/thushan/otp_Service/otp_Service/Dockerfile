# Use OpenJDK 21 slim image
FROM openjdk:21-jdk-slim

# Set metadata
LABEL authors="D.Thushan Madushara"

# Set working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/otp-service.jar otp-service.jar

# Expose the port for the service
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "otp-service.jar"]
