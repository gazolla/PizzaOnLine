FROM openjdk:17-slim as build

WORKDIR /app

RUN apt-get update && apt-get install -y maven

COPY pom.xml .
COPY src src

RUN mvn clean install

FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 80

CMD ["java", "-jar", "app.jar"]
