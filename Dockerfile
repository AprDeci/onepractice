FROM maven:3.9.9-eclipse-temurin-21 AS builder
ARG SKIP_TESTS=true
ARG MAVEN_PROFILE=prod
WORKDIR /app


COPY pom.xml .
COPY .mvn .mvn
COPY src ./src


RUN mvn -B -P ${MAVEN_PROFILE} -Druntime-profile=${MAVEN_PROFILE} -DskipTests=${SKIP_TESTS} clean package

### 运行阶段仅保留 JRE，镜像体积更小
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN mkdir -p /app/logs

ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-XX:InitialRAMPercentage=25 -XX:MaxRAMPercentage=75 -XX:+UseG1GC -XX:+UseStringDeduplication -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/app/logs/heapdump.hprof" \
    MAVEN_PROFILE=prod

COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
