# Build Stage
FROM gradle:jdk-21-and-24 AS builder
WORKDIR /workspace
COPY . .
RUN ./gradlew :user-service:bootJar --no-daemon

# RUN Stage
FROM amazoncorretto:21
WORKDIR /app
COPY --from=builder /workspace/user-service/build/libs/user-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]