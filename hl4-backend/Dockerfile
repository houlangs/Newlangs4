FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn package -DskipTests

# 运行阶段
FROM openjdk:17-slim
WORKDIR /app
# 从构建阶段复制构建好的jar文件
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
