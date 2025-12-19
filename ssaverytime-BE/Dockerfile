# Build stage
FROM --platform=linux/amd64 maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw package -DskipTests

# Run stage
FROM amazoncorretto:17-alpine-jdk
# 필수 패키지 설치: ca-certificates (HTTPS 인증서 문제 해결)
RUN apk add --no-cache ca-certificates && update-ca-certificates
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
