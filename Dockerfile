FROM openjdk:8-jdk-alpine
LABEL maintainer="pvymola@gmail.com"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/closeit-interview-0.0.1.jar
ADD ${JAR_FILE} closeit-interview.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/closeit-interview.jar"]