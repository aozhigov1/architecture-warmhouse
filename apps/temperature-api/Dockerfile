FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/temperature-api.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
