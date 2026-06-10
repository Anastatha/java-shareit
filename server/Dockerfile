FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml ./pom.xml
COPY server/pom.xml ./server/pom.xml
COPY gateway/pom.xml ./gateway/pom.xml

RUN mvn -pl server -am -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/server/target/shareit-server-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
