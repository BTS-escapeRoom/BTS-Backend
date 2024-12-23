#=========================================================================
## BUILD STAGE 1 - APP 빌드
FROM gradle:jdk17 AS builder

WORKDIR /app

COPY appspec.yml ./
COPY gradle ./
COPY scripts ./
COPY build.gradle ./
COPY settings.gradle ./
COPY src ./
COPY gradlew ./

RUN chmod +x ./gradlew
RUN gradle wrapper
RUN ./gradlew build bootJar

RUN ls -al build/libs

RUN mv build/libs/*-SNAPSHOT.jar build/libs/app.jar


#=========================================================================
## BUILD STAGE 2 - APP 실행

FROM amazoncorretto:17-alpine3.18

ENV PROFILE="dev-docker"

WORKDIR /app

COPY appspec.yml ./
COPY --from=builder /app/build/libs/app.jar ./


ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=$PROFILE"]