# Use OpenJDK 17 and install Maven
FROM openjdk:17-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app

# Copy project files
COPY . .

# Build the app
RUN mvn clean package -DskipTests

EXPOSE 8080

# Run the Spring Boot jar (make sure the jar name matches your output)
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
