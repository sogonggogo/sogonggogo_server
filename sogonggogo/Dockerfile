# 1단계: 빌드
FROM gradle:8.10-jdk17 AS builder
WORKDIR /app

# gradle 캐시 최적화용
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle
RUN gradle clean --no-daemon || true

# 실제 소스 복사 후 빌드
COPY . .
RUN gradle bootJar -x test --no-daemon

# 2단계: 런타임
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 타임존 설정 (한국)
RUN apk add --no-cache tzdata \
  && cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime \
  && echo "Asia/Seoul" > /etc/timezone

# 빌드된 jar 복사 (경로는 실제 빌드 결과 보고 맞추기)
COPY --from=builder /app/build/libs/*SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
