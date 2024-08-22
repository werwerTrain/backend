# 使用官方 Maven 和 JDK 镜像
FROM maven:3.9.9-eclipse-temurin-21-alpine

# 设置工作目录
WORKDIR /app

# 复制 pom.xml 和源码
COPY pom.xml ./
COPY src/ ./src/

# 构建项目
RUN mvn clean package

# 运行应用
CMD ["java", "-jar", "target/se_project-0.0.1-SNAPSHOT.jar"]

# 暴露端口
EXPOSE 8080