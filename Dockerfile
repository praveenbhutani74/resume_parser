# === Build stage ===
FROM maven:3.8.6-openjdk-11 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# === Package stage ===
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/resume-parser-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
