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

RUN gradle clean build -x test

CMD ["ls", "-al", "/app/build/libs"]


#=========================================================================
## BUILD STAGE 2 - APP 실행

FROM amazoncorretto:17-alpine3.18

ENV PROFILE="dev-docker"

WORKDIR /app

COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=$PROFILE"]