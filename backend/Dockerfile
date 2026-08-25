FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/devflow-backend-0.1.0.jar ./devflow-backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "devflow-backend.jar"]
