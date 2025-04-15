FROM openjdk:21-slim as build

RUN apt-get update && apt-get install -y maven

WORKDIR /authUser

COPY authUser/pom.xml /authUser/

COPY authUser/src /authUser/src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

COPY --from=build /authUser/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]