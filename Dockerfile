FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
COPY target/works-register-api-0.0.1-SNAPSHOT.jar works.jar
ENTRYPOINT ["java","-jar","/works.jar"]