FROM gradle:4.8.1-jdk AS build

COPY --chown=gradle:gradle burning-okr /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle bootJar --no-daemon

FROM openjdk:8-jre-slim

EXPOSE 8080

RUN apt-get update; apt-get install -y fontconfig libfreetype6 

RUN mkdir /app

COPY --from=build /home/gradle/src/burning-okr-app/build/libs/*.jar /app/burning-okr.jar


ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/burning-okr.jar"]
