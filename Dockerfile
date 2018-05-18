# Multi-stage build setup (https://docs.docker.com/develop/develop-images/multistage-build/)

# Stage 1 (to create a "build" image, ~140MB)
FROM azul/zulu-openjdk-alpine:8 AS builder
RUN java -version

COPY . /usr/src/myapp/
WORKDIR /usr/src/myapp/
# RUN apk --no-cache add maven && mvn --version
RUN ./gradlew --version
RUN ./gradlew shadowJar

# Stage 2 (to create a downsized "container executable", ~87MB)
FROM openjdk:8-jre-alpine3.7
#FROM azul/zulu-openjdk-alpine:8
WORKDIR /root/
COPY --from=builder /usr/src/myapp/build/libs/app-all.jar .

EXPOSE 8123
ENTRYPOINT ["java", "-jar", "./app-all.jar"]
