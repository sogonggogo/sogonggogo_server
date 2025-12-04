# 1단계: 빌드
FROM gradle:8.10-jdk17 AS builder
WORKDIR /app

COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle
RUN gradle clean --no-daemon || true

COPY . .
RUN gradle bootJar -x test --no-daemon

# 2단계: 실행
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN apk add --no-cache tzdata \
  && cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime \
  && echo "Asia/Seoul" > /etc/timezone

COPY --from=builder /app/build/libs/*SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
