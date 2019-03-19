FROM openjdk:8-alpine
COPY ./target/app.jar /usr/src/plantlogic/
WORKDIR /usr/src/plantlogic
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]