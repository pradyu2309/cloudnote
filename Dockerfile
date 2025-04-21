# 1. Use an official OpenJDK image
FROM openjdk:17-jdk-slim

# 2. Set working directory
WORKDIR /app

# 3. Copy Maven target jar into container
COPY target/cloudnote-0.0.1-SNAPSHOT.jar app.jar

# 4. Expose port (same as Spring Boot runs on)
EXPOSE 8080

# 5. Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
