# Build Stage
FROM gradle:jdk-21-and-24 AS builder
WORKDIR /workspace
COPY . .
RUN ./gradlew :order-service:bootJar --no-daemon

# RUN Stage
FROM amazoncorretto:21
WORKDIR /app
COPY --from=builder /workspace/order-service/build/libs/order-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]