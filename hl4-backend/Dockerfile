FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
# 下载依赖
RUN mvn dependency:go-offline

# 复制源代码并构建
COPY src ./src
RUN mvn package -DskipTests

# 运行阶段
FROM openjdk:17-slim
WORKDIR /app
# 从构建阶段复制构建好的jar文件
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
