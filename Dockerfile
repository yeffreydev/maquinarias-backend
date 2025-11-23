# Use Eclipse Temurin OpenJDK 21 which is available on Docker Hub
FROM eclipse-temurin:21-jdk-alpine

# Install curl for health checks if needed
RUN apk add --no-cache curl

# Set the working directory inside the container
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradle ./gradle
COPY gradlew .
COPY gradlew.bat .
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable
RUN chmod +x ./gradlew

# Download dependencies (this layer will be cached if dependencies don't change)
RUN ./gradlew dependencies --no-daemon

# Copy the rest of the application source code
COPY src ./src

# Build the application
RUN ./gradlew bootJar --no-daemon

# Create uploads directory for file storage
RUN mkdir -p /app/uploads

# Expose the port that the application will run on
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=docker -jar build/libs/maquinarias-0.0.1-SNAPSHOT.jar"]