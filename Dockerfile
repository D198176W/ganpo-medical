# ---------- 构建阶段 ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# 先拷贝 pom 利用 Docker 层缓存加速依赖下载
COPY pom.xml .
COPY consult/pom.xml consult/
COPY health-management/pom.xml health-management/
COPY pathology/pom.xml pathology/
COPY medical-resource/pom.xml medical-resource/
RUN mvn -q -pl consult -am dependency:resolve -DskipTests || true

# 拷贝源码并打包 consult 模块
COPY . .
RUN mvn -q -pl consult -am package -DskipTests

# ---------- 运行阶段 ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /build/consult/target/consult-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD sh -c 'curl -fsS http://localhost:8080/doc.html >/dev/null 2>&1 || exit 1'

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
