
FROM openjdk:8-jdk-alpine

MAINTAINER runaysolutions.com

RUN addgroup -S mygroup && adduser -S myuser -G mygroup
USER myuser:mygroup
ARG JAR_FILE=build/libs/SpringBootProduction-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# we can also provider the spring profile to use here if needed
ENTRYPOINT ["java", "-jar", "app.jar"]