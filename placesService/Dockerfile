FROM openjdk:21-slim as build

RUN apt-get update && apt-get install -y maven

WORKDIR /places

COPY placesService/pom.xml /places/

COPY placesService/src /places/src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

COPY --from=build /places/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]