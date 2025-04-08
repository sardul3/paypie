# syntax=docker/dockerfile:1

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
