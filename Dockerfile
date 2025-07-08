# ----- Build Stage -----
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Kopiere Projektdateien & baue das Projekt
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ----- Runtime Stage -----
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Umgebungsvariablen setzen
ENV SPRING_PROFILES_ACTIVE=prod
ENV spring.application.name=cuc_ta_alliance_manager
ENV server.port=8080
ENV app.save.intervall=60000
ENV app.pwd=f7M728O1Zfab7AFAZwcxUzlu4L7VwdqSBI0iBq4kciHUi493kdDg93
ENV app.allowed.world=463
ENV app.open.api.for.ip.read=*

# Kopiere JAR aus dem vorherigen Stage
COPY --from=build /app/target/*.jar app.jar

# Port (optional – rein dokumentarisch)
EXPOSE 8080

# App starten
ENTRYPOINT ["java", "-jar", "app.jar"]