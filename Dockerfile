FROM openjdk:11.0.7-jre-slim-buster

RUN mkdir /app
COPY  target/coding-dojo-spring-boot*.jar /app/weatherApp.jar

WORKDIR /app
ENTRYPOINT ["java","-jar","/app/weatherApp.jar"]