# Use a lightweight JDK image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file to the container
COPY target/lmt-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app will run on
EXPOSE 8080

# Command to run the app
CMD ["java", "-Dserver.port=$PORT", "-jar", "app.jar"]
