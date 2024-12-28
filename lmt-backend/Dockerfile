# Stage 1 - Build the application
FROM maven:3.9.9-amazoncorretto-17-al2023 AS build

WORKDIR /app

# Copy the pom.xml and download dependencies (optimize caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2 - Run the application
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built jar file from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port
EXPOSE 8080

# Command to run the app
CMD ["java", "-Dserver.port=$PORT", "-jar", "app.jar"]
