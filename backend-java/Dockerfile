FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml ./
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV DB_URL=jdbc:postgresql://host.docker.internal:5432/pattern_analyzer
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=postgres

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
