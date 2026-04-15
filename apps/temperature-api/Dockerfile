FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app
COPY src ./src

RUN mkdir -p target/classes && javac -d target/classes src/main/java/com/warmhouse/temperatureapi/*.java

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/classes ./
EXPOSE 8081
ENTRYPOINT ["java", "com.warmhouse.temperatureapi.TemperatureApi"]
