# Build Stage
FROM gradle:jdk-21-and-24 AS builder
WORKDIR /workspace
COPY . .
RUN ./gradlew :api-gateway:bootJar --no-daemon

# RUN Stage
FROM amazoncorretto:21
WORKDIR /app
COPY --from=builder /workspace/api-gateway/build/libs/api-gateway.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]