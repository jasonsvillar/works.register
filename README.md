# WORKS REGISTER API
Master - 1.4 - https://works-api-rest.onrender.com/doc/swagger-ui/index.html

Dev - Unreleased - https://works-api-rest-dev.onrender.com/doc/swagger-ui/index.html

_* These services are run with the free plan on render.com. Be patient._

### Angular Frontend Project
Master branch - https://github.com/jasonsvillar/works.register-frontend

Develop branch - https://github.com/jasonsvillar/works.register-frontend/tree/develop

## Project Description

Api Rest made with:
- Classic Architecture - Controller - Service - Entity.
- S.O.L.I.D. Principles.
- Java 17.
- Framework Spring boot 3.
- Liquibase - For Database creation and migration.
- Relational Database - Postgres.
- Swagger 3. Access URL _**/doc/swagger-ui/index.html**_
- Testing with JUnit 5, Mockito and TestContainer.
- Code Review with SonarQube.
- Spring Security with Basic Authentication.
- Basic Authentication with roles and privileges.
- Spring Security with OAuth2 GitHub Authentication and JSession.
- Implement Spring Security with JWT on the Basic Auth.
- Encrypt application.properties values with jasypt.

D.E.R:

<img alt="der" src="readme/der.png" title="der" width="600"/>

## To do

- ~~Implement Spring Security with Basic Authentication.~~
- ~~Implement Spring Security with OAuth2 GitHub Authentication and JSession.~~
- ~~Implement Spring Security with JWT on the Basic Auth.~~
- ~~Transform all endpoints (clients, services, works) for the current user logged.~~
- Implement admin endpoints with secure privilege.
- Develop Frontend using Angular 16. WIP: https://github.com/jasonsvillar/works.register-frontend
- Rewrite unit tests using BDD.
- Use kubernetes to run all the project.
- Implement CI/CD.
- Implement Clean Architecture and create a new project based on this.
- Subdivide this project into microservices and use Apache Kafka or RabitMQ.

# Notes for the DEVs
# Jasypt
 - Due to the implementation of jasypt to run the application it is necessary to send the environment variable JASYPT_ENCRYPTOR_PASSWORD.
 - Example to encrypt values in application.properties:
   1. spring.datasource.username=**DEC**(ExampleUserName)
   2. mvn jasypt:encrypt -Djasypt.encryptor.password=UltraSecretPassword
   3. Result: spring.datasource.username=**ENC**(ger86g65e4aga4ge5g445dfgLYS47l)