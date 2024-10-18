# Use a base image with Java installed
FROM --platform=linux/x86_64 amazoncorretto:17-alpine

# Set the working directory
WORKDIR /app

# Copy the Maven/Gradle built JAR file into the container (adjust path if needed)
COPY tic-tac-toe.jar /app/app.jar

EXPOSE 9090

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
