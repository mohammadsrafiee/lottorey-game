# Use an OpenJDK 17 base image
FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests
FROM maven:3.8.4-openjdk-17-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar game.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
