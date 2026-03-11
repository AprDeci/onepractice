FROM maven:3.9.9-eclipse-temurin-21 AS builder
ARG SKIP_TESTS=true
ARG MAVEN_PROFILE=prod
WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY src ./src

RUN mvn -B -P ${MAVEN_PROFILE} -Druntime-profile=${MAVEN_PROFILE} -DskipTests=${SKIP_TESTS} clean package

# 新增 extractor 阶段：提取 layered JAR
FROM eclipse-temurin:21-jre-alpine AS extractor
WORKDIR /extracted

# 从 builder 拷贝打包好的 JAR
COPY --from=builder /app/target/*.jar app.jar

# 用 layertools 提取分层
RUN java -Djarmode=layertools -jar app.jar extract

### 运行阶段（用 alpine 版本，如你之前同意的）
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN mkdir -p /app/logs

ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-XX:InitialRAMPercentage=25 -XX:MaxRAMPercentage=75 -XX:+UseG1GC -XX:+UseStringDeduplication -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/app/logs/heapdump.hprof" \
    MAVEN_PROFILE=prod

# 拷贝提取后的分层（顺序重要：不变层在前）
COPY --from=extractor /extracted/dependencies/ ./
COPY --from=extractor /extracted/spring-boot-loader/ ./
COPY --from=extractor /extracted/snapshot-dependencies/ ./
COPY --from=extractor /extracted/application/ ./

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher