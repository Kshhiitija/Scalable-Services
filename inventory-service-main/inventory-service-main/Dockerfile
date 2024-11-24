# Stage 1: Build the Spring Boot Application
FROM openjdk:17-jdk-slim as build

# Install dependencies
RUN apt-get update && apt-get install -y git maven && rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Clone the Spring Boot application from GitHub
RUN git clone https://github.com/sanketmunotbits/inventory-service.git .

# Build the Spring Boot application using Maven (you can use Gradle as well if needed)
RUN mvn clean package -DskipTests -X && echo "Build Complete"

RUN ls -alh /app/target

# Stage 2: Create the runtime environment
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage to the runtime stage
COPY --from=build /app/target/inventory-0.0.1-SNAPSHOT.jar /app/inventory-service.jar

# Expose the port that your Spring Boot app will run on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/inventory-service.jar"]
