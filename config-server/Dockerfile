# Build Stage
FROM gradle:jdk-21-and-24 AS builder
WORKDIR /workspace
COPY . .
RUN ./gradlew :config-server:bootJar --no-daemon

# RUN Stage
FROM amazoncorretto:21
WORKDIR /app
COPY --from=builder /workspace/config-server/build/libs/config-server.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]