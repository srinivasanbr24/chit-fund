FROM eclipse-temurin:17-jdk-noble AS builder

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-noble

WORKDIR /app
COPY --from=builder /app/target/*.jar chit-fund-app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "chit-fund-app.jar"]