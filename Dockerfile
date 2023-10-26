FROM maven:3.9.5-amazoncorretto-17-al2023 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17
COPY --from=build target/works-register-api-0.0.1-SNAPSHOT.jar works.jar
# ENV PORT=8080
EXPOSE 80
# SECRETS
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=render","-Dport=80","works.jar"]