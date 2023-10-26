FROM maven:3.9.5-amazoncorretto-17-al2023
COPY . .
RUN mvn clean package -Pprod -DskipTests
COPY target/works-register-api-0.0.1-SNAPSHOT.jar works.jar
# ENV PORT=8080
EXPOSE 8080
# SECRETS
ENTRYPOINT ["java","-Dspring.datasource.url=jdbc:postgresql://dpg-ckt9eve5or3s73dcndu0-a:5432/works_4ayw","-Dspring.datasource.username=works","-Dspring.datasource.password=3pmKlO6Yp7AaGFkSyY2MTad557Zkl13d","-Djwt.keyForEncrypt=KS8K7jIWqGh+RNPMaXnzY3G26x97HJ+YapFcMqXDbKL7+3rUUPNQcvnCNMZZ2CafKab+aV82brAzHCCL48NgEKbey-b3hqZnnUt6AvLHUu8TucBrZAUv96IJBfeKZtfnU2PApxCzCEqzBc-KnBw3kQ","-jar","works.jar"]