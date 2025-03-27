FROM eclipse-temurin:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR file into the container
COPY out/artifacts/rp_jar/rp.jar app.jar

# Expose the port
EXPOSE 8080:8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]