# ===== Build stage: compile the app with Maven =====
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ===== Run stage: lightweight image with just the JRE + built jar =====
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/animehub-1.0.0.jar app.jar

# Render assigns its own port via $PORT - we tell Spring Boot to use it
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]
