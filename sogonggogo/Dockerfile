# ---- Build stage ----
FROM gradle:8.9-jdk17-alpine AS build
WORKDIR /workspace

ARG GIT_SHA=dev
LABEL org.opencontainers.image.revision=$GIT_SHA

# Gradle wrapper 및 설정 복사
COPY gradlew gradlew
COPY gradle gradle
COPY settings.gradle* build.gradle* ./
RUN chmod +x ./gradlew

# Gradle 캐시 워밍업용 (옵션)
RUN ./gradlew --no-daemon -q help || true

# 전체 소스 복사
COPY . .

RUN chmod +x ./gradlew

# 테스트는 CI에서 돌리므로 여기서는 생략하고 bootJar만 빌드
RUN ./gradlew --no-daemon clean bootJar -x test

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 빌드 결과 JAR 복사
COPY --from=build /workspace/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
