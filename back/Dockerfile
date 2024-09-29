FROM maven:3.8-openjdk-17-slim AS builder
LABEL authors="Gabriel"

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder ./app/target/ifome-0.0.1-SNAPSHOT.jar ifome.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ifome.jar"]