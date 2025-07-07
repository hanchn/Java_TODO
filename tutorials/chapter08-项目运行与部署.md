# 第八章：项目运行与部署

> **学习目标**：掌握Spring Boot项目的本地运行、打包部署、环境配置和生产环境部署策略

---

## 📋 本章概览

### 🎯 核心内容
- **本地开发环境**：IDE运行、Maven命令、热部署
- **项目打包**：JAR包构建、WAR包部署、Docker容器化
- **环境配置**：多环境配置、外部化配置、配置加密
- **部署策略**：传统部署、容器化部署、云平台部署
- **监控运维**：健康检查、日志管理、性能监控

### 📚 技术栈
- **构建工具**：Maven、Gradle
- **容器化**：Docker、Docker Compose
- **部署平台**：Tomcat、Nginx、云服务
- **监控工具**：Spring Boot Actuator、Prometheus、Grafana

---

## 8.1 本地开发环境

### 🎯 实战任务1：配置开发环境

#### IDE运行配置
```java
// src/main/java/com/example/studentmanagement/StudentManagementApplication.java

package com.example.studentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 学生管理系统主启动类
 * 
 * 功能特性：
 * 1. 自动配置Spring Boot组件
 * 2. 启用JPA审计功能
 * 3. 启用缓存支持
 * 4. 启用异步处理
 * 5. 启用事务管理
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableTransactionManagement
@EnableConfigurationProperties
public class StudentManagementApplication {
    
    public static void main(String[] args) {
        // 设置系统属性
        System.setProperty("spring.profiles.active", 
            System.getProperty("spring.profiles.active", "dev"));
        
        // 启动Spring Boot应用
        SpringApplication app = new SpringApplication(StudentManagementApplication.class);
        
        // 自定义启动配置
        app.setAdditionalProfiles("dev");
        app.setLogStartupInfo(true);
        
        app.run(args);
    }
}
```

#### 开发环境配置
```yaml
# src/main/resources/application-dev.yml

spring:
  # 数据源配置
  datasource:
    url: jdbc:h2:mem:devdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password: 
    
  # H2控制台配置
  h2:
    console:
      enabled: true
      path: /h2-console
      settings:
        web-allow-others: true
        
  # JPA配置
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true
        
  # 热部署配置
  devtools:
    restart:
      enabled: true
      additional-paths: src/main/java
      exclude: static/**,public/**
    livereload:
      enabled: true
      
  # Web配置
  web:
    resources:
      static-locations: classpath:/static/
      cache:
        period: 0
        
# 日志配置
logging:
  level:
    com.example.studentmanagement: DEBUG
    org.springframework.web: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    
# 管理端点配置
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,env,configprops,beans
  endpoint:
    health:
      show-details: always
      
# 应用配置
app:
  name: Student Management System
  version: 1.0.0-DEV
  description: 学生管理系统开发环境
  features:
    cache-enabled: false
    async-enabled: true
    audit-enabled: true
```

#### Maven开发配置
```xml
<!-- pom.xml 开发环境配置 -->

<profiles>
    <!-- 开发环境 -->
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <spring.profiles.active>dev</spring.profiles.active>
            <maven.test.skip>false</maven.test.skip>
        </properties>
        <dependencies>
            <!-- 开发工具 -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-devtools</artifactId>
                <scope>runtime</scope>
                <optional>true</optional>
            </dependency>
            
            <!-- H2数据库 -->
            <dependency>
                <groupId>com.h2database</groupId>
                <artifactId>h2</artifactId>
                <scope>runtime</scope>
            </dependency>
        </dependencies>
        
        <build>
            <plugins>
                <!-- Spring Boot Maven插件 -->
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <configuration>
                        <profiles>
                            <profile>dev</profile>
                        </profiles>
                        <jvmArguments>
                            -Dspring.profiles.active=dev
                            -Xdebug
                            -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005
                        </jvmArguments>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```

### 🔧 Maven命令详解

#### 常用开发命令
```bash
# 1. 清理项目
mvn clean

# 2. 编译项目
mvn compile

# 3. 运行测试
mvn test

# 4. 打包项目
mvn package

# 5. 安装到本地仓库
mvn install

# 6. 运行Spring Boot应用
mvn spring-boot:run

# 7. 指定环境运行
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 8. 调试模式运行
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# 9. 跳过测试打包
mvn package -DskipTests

# 10. 生成项目报告
mvn site
```

#### 自定义Maven脚本
```bash
#!/bin/bash
# scripts/dev-start.sh

echo "Starting Student Management System in Development Mode..."

# 设置环境变量
export SPRING_PROFILES_ACTIVE=dev
export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=256m"

# 清理并启动
mvn clean spring-boot:run \
  -Dspring-boot.run.profiles=dev \
  -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005" \
  -Dspring-boot.run.arguments="--logging.level.com.example.studentmanagement=DEBUG"
```

### 🔥 热部署配置

#### Spring Boot DevTools配置
```properties
# src/main/resources/application-dev.properties

# DevTools配置
spring.devtools.restart.enabled=true
spring.devtools.restart.additional-paths=src/main/java,src/main/resources
spring.devtools.restart.exclude=static/**,public/**,templates/**
spring.devtools.livereload.enabled=true
spring.devtools.livereload.port=35729

# 缓存禁用
spring.thymeleaf.cache=false
spring.web.resources.cache.period=0
spring.web.resources.chain.cache=false

# 文件监控
spring.devtools.restart.poll-interval=1000
spring.devtools.restart.quiet-period=400
```

#### IDE热部署配置
```java
// src/main/java/com/example/studentmanagement/config/DevConfig.java

package com.example.studentmanagement.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 开发环境配置
 */
@Configuration
@Profile("dev")
public class DevConfig {
    
    /**
     * 开发环境CORS配置
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:3000", "http://localhost:8080")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);
            }
        };
    }
    
    /**
     * 开发环境数据初始化
     */
    @Bean
    @ConditionalOnProperty(name = "app.dev.init-data", havingValue = "true")
    public DevDataInitializer devDataInitializer() {
        return new DevDataInitializer();
    }
}
```

---

## 8.2 项目打包与构建

### 🎯 实战任务2：多环境打包配置

#### JAR包打包
```xml
<!-- pom.xml JAR包配置 -->

<packaging>jar</packaging>

<build>
    <finalName>${project.artifactId}-${project.version}</finalName>
    
    <plugins>
        <!-- Spring Boot Maven插件 -->
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <executable>true</executable>
                <includeSystemScope>true</includeSystemScope>
                <mainClass>com.example.studentmanagement.StudentManagementApplication</mainClass>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        
        <!-- Maven编译插件 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>17</source>
                <target>17</target>
                <encoding>UTF-8</encoding>
            </configuration>
        </plugin>
        
        <!-- Maven资源插件 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-resources-plugin</artifactId>
            <version>3.2.0</version>
            <configuration>
                <encoding>UTF-8</encoding>
            </configuration>
        </plugin>
        
        <!-- Maven测试插件 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.0.0-M7</version>
            <configuration>
                <skipTests>${maven.test.skip}</skipTests>
                <includes>
                    <include>**/*Test.java</include>
                    <include>**/*Tests.java</include>
                </includes>
            </configuration>
        </plugin>
    </plugins>
    
    <!-- 资源过滤 -->
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <filtering>true</filtering>
            <includes>
                <include>**/*.yml</include>
                <include>**/*.yaml</include>
                <include>**/*.properties</include>
            </includes>
        </resource>
        <resource>
            <directory>src/main/resources</directory>
            <filtering>false</filtering>
            <excludes>
                <exclude>**/*.yml</exclude>
                <exclude>**/*.yaml</exclude>
                <exclude>**/*.properties</exclude>
            </excludes>
        </resource>
    </resources>
</build>
```

#### 多环境打包脚本
```bash
#!/bin/bash
# scripts/build.sh

set -e

echo "Building Student Management System..."

# 参数解析
ENVIRONMENT=${1:-prod}
SKIP_TESTS=${2:-false}

echo "Environment: $ENVIRONMENT"
echo "Skip Tests: $SKIP_TESTS"

# 清理
echo "Cleaning project..."
mvn clean

# 编译
echo "Compiling project..."
mvn compile

# 测试
if [ "$SKIP_TESTS" = "false" ]; then
    echo "Running tests..."
    mvn test -P$ENVIRONMENT
else
    echo "Skipping tests..."
fi

# 打包
echo "Packaging project..."
mvn package -P$ENVIRONMENT -DskipTests=$SKIP_TESTS

# 检查打包结果
JAR_FILE="target/student-management-*.jar"
if [ -f $JAR_FILE ]; then
    echo "Build successful!"
    echo "JAR file: $(ls -la $JAR_FILE)"
    
    # 显示JAR信息
    echo "JAR file details:"
    java -jar $JAR_FILE --version 2>/dev/null || echo "Version info not available"
else
    echo "Build failed! JAR file not found."
    exit 1
fi

echo "Build completed successfully!"
```

### 🐳 Docker容器化

#### Dockerfile配置
```dockerfile
# Dockerfile

# 多阶段构建
# 阶段1：构建阶段
FROM maven:3.8.6-openjdk-17-slim AS builder

# 设置工作目录
WORKDIR /app

# 复制pom.xml和源代码
COPY pom.xml .
COPY src ./src

# 构建应用
RUN mvn clean package -DskipTests

# 阶段2：运行阶段
FROM openjdk:17-jre-slim

# 设置维护者信息
LABEL maintainer="your-email@example.com"
LABEL description="Student Management System"
LABEL version="1.0.0"

# 创建应用用户
RUN groupadd -r appuser && useradd -r -g appuser appuser

# 设置工作目录
WORKDIR /app

# 复制JAR文件
COPY --from=builder /app/target/student-management-*.jar app.jar

# 创建日志目录
RUN mkdir -p /app/logs && chown -R appuser:appuser /app

# 切换到应用用户
USER appuser

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动命令
ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:prod}", \
    "-Xmx512m", \
    "-Xms256m", \
    "-jar", \
    "app.jar"]
```

#### Docker Compose配置
```yaml
# docker-compose.yml

version: '3.8'

services:
  # 应用服务
  student-management:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: student-management-app
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/studentdb
      - SPRING_DATASOURCE_USERNAME=student_user
      - SPRING_DATASOURCE_PASSWORD=student_pass
      - SPRING_REDIS_HOST=redis
      - SPRING_REDIS_PORT=6379
    depends_on:
      - postgres
      - redis
    networks:
      - student-network
    volumes:
      - ./logs:/app/logs
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 60s

  # PostgreSQL数据库
  postgres:
    image: postgres:13
    container_name: student-management-db
    environment:
      - POSTGRES_DB=studentdb
      - POSTGRES_USER=student_user
      - POSTGRES_PASSWORD=student_pass
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./scripts/init.sql:/docker-entrypoint-initdb.d/init.sql
    networks:
      - student-network
    restart: unless-stopped

  # Redis缓存
  redis:
    image: redis:7-alpine
    container_name: student-management-cache
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    networks:
      - student-network
    restart: unless-stopped
    command: redis-server --appendonly yes

  # Nginx反向代理
  nginx:
    image: nginx:alpine
    container_name: student-management-proxy
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf
      - ./nginx/ssl:/etc/nginx/ssl
      - ./static:/usr/share/nginx/html
    depends_on:
      - student-management
    networks:
      - student-network
    restart: unless-stopped

volumes:
  postgres_data:
  redis_data:

networks:
  student-network:
    driver: bridge
```

#### Docker构建脚本
```bash
#!/bin/bash
# scripts/docker-build.sh

set -e

echo "Building Docker image for Student Management System..."

# 参数
IMAGE_NAME="student-management"
IMAGE_TAG=${1:-latest}
ENVIRONMENT=${2:-prod}

echo "Image: $IMAGE_NAME:$IMAGE_TAG"
echo "Environment: $ENVIRONMENT"

# 构建镜像
echo "Building Docker image..."
docker build \
  --build-arg SPRING_PROFILES_ACTIVE=$ENVIRONMENT \
  -t $IMAGE_NAME:$IMAGE_TAG \
  -t $IMAGE_NAME:latest \
  .

# 检查镜像
echo "Docker image built successfully:"
docker images | grep $IMAGE_NAME

# 运行容器测试
echo "Testing Docker container..."
docker run --rm -d \
  --name test-container \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=$ENVIRONMENT \
  $IMAGE_NAME:$IMAGE_TAG

# 等待容器启动
echo "Waiting for container to start..."
sleep 30

# 健康检查
if curl -f http://localhost:8080/actuator/health; then
    echo "Container health check passed!"
else
    echo "Container health check failed!"
    docker logs test-container
    exit 1
fi

# 停止测试容器
docker stop test-container

echo "Docker build and test completed successfully!"
```

---

## 8.3 环境配置管理

### 🎯 实战任务3：多环境配置

#### 生产环境配置
```yaml
# src/main/resources/application-prod.yml

spring:
  # 数据源配置
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/studentdb}
    username: ${DATABASE_USERNAME:student_user}
    password: ${DATABASE_PASSWORD:student_pass}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 300000
      connection-timeout: 20000
      max-lifetime: 1200000
      
  # JPA配置
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: false
        use_sql_comments: false
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
        
  # Redis配置
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD:}
    timeout: 2000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
        max-wait: -1ms
        
  # 缓存配置
  cache:
    type: redis
    redis:
      time-to-live: 600000
      cache-null-values: false
      
# 服务器配置
server:
  port: ${SERVER_PORT:8080}
  servlet:
    context-path: /
  compression:
    enabled: true
    mime-types: text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json
    min-response-size: 1024
  http2:
    enabled: true
    
# 日志配置
logging:
  level:
    com.example.studentmanagement: INFO
    org.springframework: WARN
    org.hibernate: WARN
    org.apache: WARN
  file:
    name: logs/student-management.log
  logback:
    rollingpolicy:
      max-file-size: 100MB
      max-history: 30
      total-size-cap: 3GB
      
# 管理端点配置
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
      base-path: /actuator
  endpoint:
    health:
      show-details: when-authorized
      show-components: always
  metrics:
    export:
      prometheus:
        enabled: true
  health:
    redis:
      enabled: true
    db:
      enabled: true
      
# 应用配置
app:
  name: Student Management System
  version: @project.version@
  description: 学生管理系统生产环境
  features:
    cache-enabled: true
    async-enabled: true
    audit-enabled: true
  security:
    jwt:
      secret: ${JWT_SECRET:your-secret-key}
      expiration: 86400000
  file:
    upload-dir: ${FILE_UPLOAD_DIR:/app/uploads}
    max-size: 10MB
```

#### 测试环境配置
```yaml
# src/main/resources/application-test.yml

spring:
  # 数据源配置
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password: 
    
  # JPA配置
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        
  # Redis配置（使用嵌入式）
  redis:
    host: localhost
    port: 6370
    
# 日志配置
logging:
  level:
    com.example.studentmanagement: DEBUG
    org.springframework.test: DEBUG
    
# 应用配置
app:
  features:
    cache-enabled: false
    async-enabled: false
    audit-enabled: true
```

#### 外部化配置
```java
// src/main/java/com/example/studentmanagement/config/AppProperties.java

package com.example.studentmanagement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 应用配置属性
 */
@Data
@Component
@Validated
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    
    @NotBlank
    private String name;
    
    @NotBlank
    private String version;
    
    private String description;
    
    @Valid
    @NotNull
    private Features features = new Features();
    
    @Valid
    @NotNull
    private Security security = new Security();
    
    @Valid
    @NotNull
    private FileConfig file = new FileConfig();
    
    @Data
    public static class Features {
        private boolean cacheEnabled = true;
        private boolean asyncEnabled = true;
        private boolean auditEnabled = true;
    }
    
    @Data
    public static class Security {
        @Valid
        @NotNull
        private Jwt jwt = new Jwt();
        
        @Data
        public static class Jwt {
            @NotBlank
            private String secret;
            
            @Positive
            private long expiration = 86400000; // 24小时
        }
    }
    
    @Data
    public static class FileConfig {
        @NotBlank
        private String uploadDir = "/tmp/uploads";
        
        @NotBlank
        private String maxSize = "10MB";
    }
}
```

#### 配置加密
```java
// src/main/java/com/example/studentmanagement/config/EncryptionConfig.java

package com.example.studentmanagement.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置加密
 */
@Configuration
public class EncryptionConfig {
    
    @Value("${jasypt.encryptor.password:default-password}")
    private String encryptorPassword;
    
    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        
        config.setPassword(encryptorPassword);
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        
        encryptor.setConfig(config);
        return encryptor;
    }
}
```

---

## 8.4 部署策略

### 🎯 实战任务4：生产环境部署

#### 传统部署脚本
```bash
#!/bin/bash
# scripts/deploy.sh

set -e

# 配置参数
APP_NAME="student-management"
APP_VERSION="1.0.0"
DEPLOY_DIR="/opt/student-management"
BACKUP_DIR="/opt/backups"
LOG_DIR="/var/log/student-management"
PID_FILE="$DEPLOY_DIR/app.pid"
JAR_FILE="$DEPLOY_DIR/$APP_NAME-$APP_VERSION.jar"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查是否为root用户
check_user() {
    if [ "$EUID" -ne 0 ]; then
        log_error "Please run as root"
        exit 1
    fi
}

# 创建必要目录
create_directories() {
    log_info "Creating directories..."
    mkdir -p $DEPLOY_DIR
    mkdir -p $BACKUP_DIR
    mkdir -p $LOG_DIR
    
    # 设置权限
    chown -R app:app $DEPLOY_DIR
    chown -R app:app $LOG_DIR
    chmod 755 $DEPLOY_DIR
    chmod 755 $LOG_DIR
}

# 停止应用
stop_app() {
    log_info "Stopping application..."
    
    if [ -f $PID_FILE ]; then
        PID=$(cat $PID_FILE)
        if ps -p $PID > /dev/null 2>&1; then
            log_info "Stopping process $PID"
            kill -TERM $PID
            
            # 等待进程停止
            for i in {1..30}; do
                if ! ps -p $PID > /dev/null 2>&1; then
                    log_info "Process stopped successfully"
                    break
                fi
                sleep 1
            done
            
            # 强制停止
            if ps -p $PID > /dev/null 2>&1; then
                log_warn "Force killing process $PID"
                kill -KILL $PID
            fi
        fi
        rm -f $PID_FILE
    else
        log_info "No PID file found, application may not be running"
    fi
}

# 备份当前版本
backup_current() {
    if [ -f $JAR_FILE ]; then
        log_info "Backing up current version..."
        BACKUP_FILE="$BACKUP_DIR/$APP_NAME-$(date +%Y%m%d_%H%M%S).jar"
        cp $JAR_FILE $BACKUP_FILE
        log_info "Backup created: $BACKUP_FILE"
    fi
}

# 部署新版本
deploy_new_version() {
    log_info "Deploying new version..."
    
    if [ ! -f "target/$APP_NAME-$APP_VERSION.jar" ]; then
        log_error "JAR file not found: target/$APP_NAME-$APP_VERSION.jar"
        exit 1
    fi
    
    cp "target/$APP_NAME-$APP_VERSION.jar" $JAR_FILE
    chown app:app $JAR_FILE
    chmod 755 $JAR_FILE
    
    log_info "New version deployed successfully"
}

# 启动应用
start_app() {
    log_info "Starting application..."
    
    # 设置JVM参数
    JAVA_OPTS="-Xms512m -Xmx1024m"
    JAVA_OPTS="$JAVA_OPTS -XX:+UseG1GC"
    JAVA_OPTS="$JAVA_OPTS -XX:MaxGCPauseMillis=200"
    JAVA_OPTS="$JAVA_OPTS -Djava.security.egd=file:/dev/./urandom"
    JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=prod"
    JAVA_OPTS="$JAVA_OPTS -Dlogging.file.name=$LOG_DIR/application.log"
    
    # 启动应用
    sudo -u app nohup java $JAVA_OPTS -jar $JAR_FILE > $LOG_DIR/startup.log 2>&1 &
    
    # 保存PID
    echo $! > $PID_FILE
    
    log_info "Application started with PID: $(cat $PID_FILE)"
}

# 健康检查
health_check() {
    log_info "Performing health check..."
    
    for i in {1..30}; do
        if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
            log_info "Health check passed!"
            return 0
        fi
        log_info "Waiting for application to start... ($i/30)"
        sleep 10
    done
    
    log_error "Health check failed!"
    return 1
}

# 回滚
rollback() {
    log_warn "Rolling back to previous version..."
    
    LATEST_BACKUP=$(ls -t $BACKUP_DIR/$APP_NAME-*.jar 2>/dev/null | head -n1)
    
    if [ -n "$LATEST_BACKUP" ]; then
        stop_app
        cp $LATEST_BACKUP $JAR_FILE
        start_app
        
        if health_check; then
            log_info "Rollback successful"
        else
            log_error "Rollback failed"
            exit 1
        fi
    else
        log_error "No backup found for rollback"
        exit 1
    fi
}

# 主函数
main() {
    log_info "Starting deployment of $APP_NAME v$APP_VERSION"
    
    check_user
    create_directories
    stop_app
    backup_current
    deploy_new_version
    start_app
    
    if health_check; then
        log_info "Deployment completed successfully!"
    else
        log_error "Deployment failed, rolling back..."
        rollback
    fi
}

# 执行主函数
main "$@"
```

#### Systemd服务配置
```ini
# /etc/systemd/system/student-management.service

[Unit]
Description=Student Management System
After=network.target
Wants=network.target

[Service]
Type=forking
User=app
Group=app
WorkingDirectory=/opt/student-management
ExecStart=/opt/student-management/start.sh
ExecStop=/opt/student-management/stop.sh
ExecReload=/bin/kill -HUP $MAINPID
PIDFile=/opt/student-management/app.pid
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=student-management

# 环境变量
Environment=SPRING_PROFILES_ACTIVE=prod
Environment=JAVA_HOME=/usr/lib/jvm/java-17-openjdk
Environment=LOG_DIR=/var/log/student-management

# 资源限制
LimitNOFILE=65536
LimitNPROC=4096

[Install]
WantedBy=multi-user.target
```

#### 启动脚本
```bash
#!/bin/bash
# /opt/student-management/start.sh

set -e

# 配置
APP_DIR="/opt/student-management"
JAR_FILE="$APP_DIR/student-management-1.0.0.jar"
PID_FILE="$APP_DIR/app.pid"
LOG_DIR="/var/log/student-management"

# JVM参数
JAVA_OPTS="-server"
JAVA_OPTS="$JAVA_OPTS -Xms512m -Xmx1024m"
JAVA_OPTS="$JAVA_OPTS -XX:+UseG1GC"
JAVA_OPTS="$JAVA_OPTS -XX:MaxGCPauseMillis=200"
JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError"
JAVA_OPTS="$JAVA_OPTS -XX:HeapDumpPath=$LOG_DIR/heapdump.hprof"
JAVA_OPTS="$JAVA_OPTS -Djava.security.egd=file:/dev/./urandom"
JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=prod"
JAVA_OPTS="$JAVA_OPTS -Dlogging.file.name=$LOG_DIR/application.log"

# 启动应用
cd $APP_DIR
nohup java $JAVA_OPTS -jar $JAR_FILE > $LOG_DIR/startup.log 2>&1 &

# 保存PID
echo $! > $PID_FILE

echo "Application started with PID: $(cat $PID_FILE)"
```

---

## 8.5 监控与运维

### 🎯 实战任务5：生产环境监控

#### Nginx配置
```nginx
# /etc/nginx/sites-available/student-management

upstream student_management {
    server 127.0.0.1:8080 max_fails=3 fail_timeout=30s;
    # 如果有多个实例
    # server 127.0.0.1:8081 max_fails=3 fail_timeout=30s;
    keepalive 32;
}

server {
    listen 80;
    server_name your-domain.com www.your-domain.com;
    
    # 重定向到HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name your-domain.com www.your-domain.com;
    
    # SSL配置
    ssl_certificate /etc/nginx/ssl/cert.pem;
    ssl_certificate_key /etc/nginx/ssl/key.pem;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers ECDHE-RSA-AES256-GCM-SHA512:DHE-RSA-AES256-GCM-SHA512:ECDHE-RSA-AES256-GCM-SHA384:DHE-RSA-AES256-GCM-SHA384;
    ssl_prefer_server_ciphers off;
    ssl_session_cache shared:SSL:10m;
    ssl_session_timeout 10m;
    
    # 安全头
    add_header X-Frame-Options DENY;
    add_header X-Content-Type-Options nosniff;
    add_header X-XSS-Protection "1; mode=block";
    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;
    
    # 日志
    access_log /var/log/nginx/student-management.access.log;
    error_log /var/log/nginx/student-management.error.log;
    
    # 静态文件
    location /static/ {
        alias /opt/student-management/static/;
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
    
    # API代理
    location /api/ {
        proxy_pass http://student_management;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # 超时设置
        proxy_connect_timeout 30s;
        proxy_send_timeout 30s;
        proxy_read_timeout 30s;
        
        # 缓冲设置
        proxy_buffering on;
        proxy_buffer_size 4k;
        proxy_buffers 8 4k;
        
        # 健康检查
        proxy_next_upstream error timeout invalid_header http_500 http_502 http_503 http_504;
    }
    
    # 健康检查端点
    location /actuator/health {
        proxy_pass http://student_management;
        access_log off;
    }
    
    # 主页
    location / {
        proxy_pass http://student_management;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

#### 监控脚本
```bash
#!/bin/bash
# scripts/monitor.sh

# 配置
APP_NAME="student-management"
HEALTH_URL="http://localhost:8080/actuator/health"
PID_FILE="/opt/student-management/app.pid"
LOG_FILE="/var/log/student-management/monitor.log"
ALERT_EMAIL="admin@example.com"

# 日志函数
log() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') - $1" | tee -a $LOG_FILE
}

# 检查进程
check_process() {
    if [ -f $PID_FILE ]; then
        PID=$(cat $PID_FILE)
        if ps -p $PID > /dev/null 2>&1; then
            log "[OK] Process $PID is running"
            return 0
        else
            log "[ERROR] Process $PID not found"
            return 1
        fi
    else
        log "[ERROR] PID file not found"
        return 1
    fi
}

# 检查健康状态
check_health() {
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" $HEALTH_URL)
    
    if [ "$HTTP_CODE" = "200" ]; then
        log "[OK] Health check passed (HTTP $HTTP_CODE)"
        return 0
    else
        log "[ERROR] Health check failed (HTTP $HTTP_CODE)"
        return 1
    fi
}

# 检查内存使用
check_memory() {
    if [ -f $PID_FILE ]; then
        PID=$(cat $PID_FILE)
        MEMORY_USAGE=$(ps -p $PID -o %mem --no-headers 2>/dev/null | tr -d ' ')
        
        if [ -n "$MEMORY_USAGE" ]; then
            MEMORY_THRESHOLD=80
            if (( $(echo "$MEMORY_USAGE > $MEMORY_THRESHOLD" | bc -l) )); then
                log "[WARN] High memory usage: ${MEMORY_USAGE}%"
                return 1
            else
                log "[OK] Memory usage: ${MEMORY_USAGE}%"
                return 0
            fi
        fi
    fi
    
    log "[ERROR] Cannot check memory usage"
    return 1
}

# 检查磁盘空间
check_disk() {
    DISK_USAGE=$(df /opt/student-management | awk 'NR==2 {print $5}' | sed 's/%//')
    DISK_THRESHOLD=85
    
    if [ "$DISK_USAGE" -gt "$DISK_THRESHOLD" ]; then
        log "[WARN] High disk usage: ${DISK_USAGE}%"
        return 1
    else
        log "[OK] Disk usage: ${DISK_USAGE}%"
        return 0
    fi
}

# 发送告警
send_alert() {
    SUBJECT="$APP_NAME Alert: $1"
    MESSAGE="$2"
    
    echo "$MESSAGE" | mail -s "$SUBJECT" $ALERT_EMAIL
    log "[ALERT] Sent alert: $SUBJECT"
}

# 重启应用
restart_app() {
    log "[ACTION] Restarting application..."
    systemctl restart student-management
    
    # 等待启动
    sleep 30
    
    if check_health; then
        log "[OK] Application restarted successfully"
        send_alert "Recovery" "Application has been restarted and is now healthy"
    else
        log "[ERROR] Application restart failed"
        send_alert "Critical" "Application restart failed, manual intervention required"
    fi
}

# 主监控逻辑
main() {
    log "Starting health check..."
    
    ERRORS=0
    
    # 检查进程
    if ! check_process; then
        ((ERRORS++))
    fi
    
    # 检查健康状态
    if ! check_health; then
        ((ERRORS++))
    fi
    
    # 检查内存
    if ! check_memory; then
        ((ERRORS++))
    fi
    
    # 检查磁盘
    if ! check_disk; then
        ((ERRORS++))
    fi
    
    # 如果有错误，尝试重启
    if [ $ERRORS -gt 1 ]; then
        send_alert "Critical" "Multiple health checks failed, attempting restart"
        restart_app
    elif [ $ERRORS -eq 1 ]; then
        send_alert "Warning" "One health check failed, monitoring closely"
    fi
    
    log "Health check completed with $ERRORS errors"
}

# 执行监控
main
```

---

## 📚 知识扩展

### 部署最佳实践

1. **蓝绿部署**
   - 零停机部署
   - 快速回滚
   - 风险控制

2. **滚动部署**
   - 逐步更新
   - 服务可用性
   - 资源优化

3. **金丝雀部署**
   - 小范围验证
   - 风险评估
   - 渐进式发布

### 容器化优势

1. **环境一致性**
   - 开发、测试、生产环境统一
   - 依赖管理简化
   - 配置标准化

2. **资源利用率**
   - 轻量级虚拟化
   - 快速启动
   - 弹性伸缩

3. **运维自动化**
   - 容器编排
   - 服务发现
   - 负载均衡

### 监控体系

1. **应用监控**
   - 性能指标
   - 业务指标
   - 错误追踪

2. **基础设施监控**
   - 服务器资源
   - 网络状态
   - 存储使用

3. **日志管理**
   - 集中收集
   - 结构化存储
   - 实时分析

---

## 🎯 实战练习

### 练习1：多环境部署
1. 配置开发、测试、生产三套环境
2. 实现自动化部署脚本
3. 验证环境隔离和配置管理

### 练习2：容器化部署
1. 编写Dockerfile和docker-compose.yml
2. 实现容器化部署
3. 配置容器监控和日志收集

### 练习3：CI/CD流水线
1. 集成Git、Jenkins/GitHub Actions
2. 实现自动化测试和部署
3. 配置部署策略和回滚机制

---

## 📖 本章总结

通过本章学习，你应该掌握：

### 核心技能
- Spring Boot应用的打包和部署
- 多环境配置管理
- Docker容器化技术
- 生产环境运维

### 实践能力
- 配置开发和生产环境
- 编写部署和监控脚本
- 实现容器化部署
- 进行性能调优和故障排查

### 工程思维
- DevOps理念和实践
- 自动化运维思维
- 监控和告警体系
- 持续集成和持续部署

### 下一步学习
- 学习Kubernetes容器编排
- 深入微服务架构
- 掌握云原生技术
- 学习服务网格和可观测性

### 参考资源
- [Spring Boot部署指南](https://spring.io/guides/gs/spring-boot-docker/)
- [Docker官方文档](https://docs.docker.com/)
- [Nginx配置指南](https://nginx.org/en/docs/)
- [Prometheus监控](https://prometheus.io/docs/)