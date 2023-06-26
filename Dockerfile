FROM gradle:8.1.1-jdk17-alpine AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean
RUN gradle build -x test

FROM openjdk:17-jdk-alpine

EXPOSE 5000

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/spb3app.jar

ENTRYPOINT ["java", "-jar", "/app/spb3app.jar"]