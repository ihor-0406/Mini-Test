FROM ubuntu:latest
LABEL authors="IHOR"

ENTRYPOINT ["top", "-b"]

# ===== build =====
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

# ===== run =====
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV SERVER_PORT=8080 \
    POSTGRES_URL=jdbc:postgresql://postgres:5432/authdb \
    POSTGRES_USER=postgres \
    POSTGRES_PASSWORD=postgres \
    JWT_SECRET=please_override_me_32bytes_minimum_aaaaaaaaaaaaaaaaaaaa \
    INTERNAL_TOKEN=dev-secret

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
