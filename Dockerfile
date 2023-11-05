FROM gradle:8.4.0-jdk17 AS build-stage

WORKDIR /home/gradle
COPY . .

RUN gradle clean build --no-daemon

FROM azul/zulu-openjdk:17

COPY --from=build-stage /home/gradle/build/libs/room-manager-*.jar /room-manager.jar


ENTRYPOINT ["java","-jar", "/room-manager.jar" ]
HEALTHCHECK CMD curl --fail http://localhost:8080/actuator/health || exit