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

# читаем секрет из env
ENV SERVER_PORT=8081 \
    INTERNAL_TOKEN=dev-secret
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]
