# Build Stage
FROM gradle:jdk-21-and-24 AS builder
WORKDIR /workspace
COPY . .
RUN ./gradlew :discovery-server:bootJar --no-daemon

# RUN Stage
FROM amazoncorretto:21
WORKDIR /app
COPY --from=builder /workspace/discovery-server/build/libs/discovery-server.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]