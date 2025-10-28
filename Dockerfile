# Use OpenJDK 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven build files and code
COPY . .

# Build the app
RUN ./mvnw clean package -DskipTests

# Expose port 8080
EXPOSE 8080

# Run the Spring Boot jar
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
