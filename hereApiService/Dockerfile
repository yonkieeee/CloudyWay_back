FROM openjdk:21-slim as build

RUN apt-get update && apt-get install -y maven

WORKDIR /Here_API_Service

COPY hereApiService/pom.xml /Here_API_Service/

COPY hereApiService/src /Here_API_Service/src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

COPY --from=build /Here_API_Service/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]