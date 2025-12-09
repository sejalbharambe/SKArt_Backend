# ---------- BUILD STAGE ----------
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy Maven configuration
COPY pom.xml ./

# Install Maven manually
RUN apt-get update && apt-get install -y maven

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests

# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
