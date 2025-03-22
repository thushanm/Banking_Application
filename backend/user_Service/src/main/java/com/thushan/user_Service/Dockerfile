FROM ubuntu:latest
LABEL authors="D.Thushan Madushara"

ENTRYPOINT ["top", "-b"]
# Use OpenJDK 21
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy JAR file
COPY target/user-service.jar user-service.jar

# Expose the port
EXPOSE 8080

# Run the applications
CMD ["java", "-jar", "user-service.jar"]
