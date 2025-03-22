FROM ubuntu:latest
LABEL authors="D.Thushan Madushara"

ENTRYPOINT ["top", "-b"]
# Use OpenJDK 21
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy JAR file
COPY target/otp-service.jar otp-service.jar

# Expose the port
EXPOSE 8081

# Run the application
CMD ["java", "-jar", "otp-service.jar"]
