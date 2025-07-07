# 第五章：配置与数据初始化

## 🎯 学习目标
- 掌握Spring Boot配置文件的使用
- 学习多环境配置管理
- 实现数据库初始化和数据预填充
- 了解配置属性绑定和验证

## ⏱️ 预计用时：30分钟

---

## 5.1 Spring Boot配置详解

### 📚 理论知识

#### 配置文件类型
Spring Boot支持多种配置文件格式：

| 格式 | 文件名 | 优先级 | 特点 |
|------|--------|--------|------|
| Properties | application.properties | 低 | 简单键值对 |
| YAML | application.yml | 高 | 层次结构，易读 |
| JSON | application.json | 中 | 结构化数据 |

#### 配置文件加载顺序
1. 命令行参数
2. 系统环境变量
3. application-{profile}.yml
4. application.yml
5. 默认配置

#### 多环境配置
```yaml
# 主配置文件 application.yml
spring:
  profiles:
    active: dev  # 激活开发环境

---
# 开发环境配置
spring:
  profiles: dev
  datasource:
    url: jdbc:h2:mem:devdb

---
# 生产环境配置
spring:
  profiles: prod
  datasource:
    url: jdbc:mysql://localhost:3306/proddb
```

### 🎯 实战任务1：完善application.yml配置

```yaml
# src/main/resources/application.yml
# Spring Boot 学生管理系统配置文件

# 服务器配置
server:
  port: 8080
  servlet:
    context-path: /
    encoding:
      charset: UTF-8
      enabled: true
      force: true
  # 错误页面配置
  error:
    path: /error
    include-message: always
    include-binding-errors: always
    include-stacktrace: on_param
    include-exception: false

# Spring 核心配置
spring:
  # 应用信息
  application:
    name: student-management-system
  
  # 环境配置
  profiles:
    active: dev
  
  # 数据源配置
  datasource:
    url: jdbc:h2:mem:studentdb
    driver-class-name: org.h2.Driver
    username: sa
    password: 
    # 连接池配置
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 300000
      connection-timeout: 20000
      max-lifetime: 1200000
      pool-name: StudentManagementHikariCP
  
  # JPA配置
  jpa:
    # 数据库平台
    database-platform: org.hibernate.dialect.H2Dialect
    # Hibernate配置
    hibernate:
      ddl-auto: create-drop  # 开发环境使用create-drop
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
        implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
    # 显示SQL
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
    # 延迟加载配置
    open-in-view: false
  
  # H2控制台配置
  h2:
    console:
      enabled: true
      path: /h2-console
      settings:
        web-allow-others: true
        trace: false
  
  # 数据初始化配置
  sql:
    init:
      mode: always
      schema-locations: classpath:schema.sql
      data-locations: classpath:data.sql
      continue-on-error: false
      separator: ;
      encoding: UTF-8
  
  # Jackson JSON配置
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
    default-property-inclusion: non_null
    serialization:
      write-dates-as-timestamps: false
      indent-output: true
    deserialization:
      fail-on-unknown-properties: false
  
  # 国际化配置
  messages:
    basename: messages
    encoding: UTF-8
    cache-duration: 3600
  
  # Web配置
  web:
    resources:
      static-locations: classpath:/static/
      cache:
        period: 3600
  
  # 开发工具配置
  devtools:
    restart:
      enabled: true
      additional-paths: src/main/java
    livereload:
      enabled: true

# 日志配置
logging:
  level:
    root: INFO
    com.example.studentmanagement: DEBUG
    org.springframework.web: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/student-management.log
    max-size: 10MB
    max-history: 30

# 管理端点配置
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,env,beans,mappings
      base-path: /actuator
  endpoint:
    health:
      show-details: when-authorized
    info:
      enabled: true
  info:
    env:
      enabled: true
    java:
      enabled: true
    os:
      enabled: true

# 应用信息配置
info:
  app:
    name: 学生管理系统
    description: 基于Spring Boot的学生信息管理系统
    version: 1.0.0
    author: 开发团队
  build:
    artifact: student-management
    group: com.example
    version: 1.0.0

# 自定义配置
app:
  # 分页配置
  pagination:
    default-page-size: 10
    max-page-size: 100
  
  # 文件上传配置
  upload:
    path: /uploads
    max-file-size: 10MB
    allowed-types: jpg,jpeg,png,gif,pdf,doc,docx
  
  # 缓存配置
  cache:
    enabled: true
    ttl: 3600
  
  # 安全配置
  security:
    jwt:
      secret: mySecretKey
      expiration: 86400
    cors:
      allowed-origins: "*"
      allowed-methods: GET,POST,PUT,DELETE,OPTIONS
      allowed-headers: "*"
      allow-credentials: true

---
# 开发环境配置
spring:
  config:
    activate:
      on-profile: dev
  
  # 开发环境数据源
  datasource:
    url: jdbc:h2:mem:devdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password: 
  
  # 开发环境JPA配置
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  
  # 开发环境H2控制台
  h2:
    console:
      enabled: true

# 开发环境日志
logging:
  level:
    com.example.studentmanagement: DEBUG
    org.springframework: INFO

---
# 测试环境配置
spring:
  config:
    activate:
      on-profile: test
  
  # 测试环境数据源
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password: 
  
  # 测试环境JPA配置
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: false
  
  # 禁用H2控制台
  h2:
    console:
      enabled: false

# 测试环境日志
logging:
  level:
    root: WARN
    com.example.studentmanagement: INFO

---
# 生产环境配置
spring:
  config:
    activate:
      on-profile: prod
  
  # 生产环境数据源（MySQL示例）
  datasource:
    url: jdbc:mysql://localhost:3306/student_management?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:password}
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 10
  
  # 生产环境JPA配置
  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto: validate  # 生产环境使用validate
    show-sql: false
  
  # 禁用H2控制台
  h2:
    console:
      enabled: false
  
  # 生产环境数据初始化
  sql:
    init:
      mode: never  # 生产环境不自动初始化

# 生产环境日志
logging:
  level:
    root: WARN
    com.example.studentmanagement: INFO
  file:
    name: /var/log/student-management/application.log

# 生产环境管理端点
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: never
```

### 🎯 实战任务2：创建自定义配置属性类

```java
// src/main/java/com/example/studentmanagement/config/AppProperties.java
package com.example.studentmanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 应用配置属性
 */
@Component
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {

    @Valid
    @NotNull
    private Pagination pagination = new Pagination();

    @Valid
    @NotNull
    private Upload upload = new Upload();

    @Valid
    @NotNull
    private Cache cache = new Cache();

    @Valid
    @NotNull
    private Security security = new Security();

    // Getters and Setters
    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Upload getUpload() {
        return upload;
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    /**
     * 分页配置
     */
    public static class Pagination {
        @Min(value = 1, message = "默认页面大小不能小于1")
        @Max(value = 100, message = "默认页面大小不能大于100")
        private int defaultPageSize = 10;

        @Min(value = 10, message = "最大页面大小不能小于10")
        @Max(value = 1000, message = "最大页面大小不能大于1000")
        private int maxPageSize = 100;

        public int getDefaultPageSize() {
            return defaultPageSize;
        }

        public void setDefaultPageSize(int defaultPageSize) {
            this.defaultPageSize = defaultPageSize;
        }

        public int getMaxPageSize() {
            return maxPageSize;
        }

        public void setMaxPageSize(int maxPageSize) {
            this.maxPageSize = maxPageSize;
        }
    }

    /**
     * 文件上传配置
     */
    public static class Upload {
        @NotBlank(message = "上传路径不能为空")
        private String path = "/uploads";

        @NotBlank(message = "最大文件大小不能为空")
        private String maxFileSize = "10MB";

        @NotBlank(message = "允许的文件类型不能为空")
        private String allowedTypes = "jpg,jpeg,png,gif,pdf,doc,docx";

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getMaxFileSize() {
            return maxFileSize;
        }

        public void setMaxFileSize(String maxFileSize) {
            this.maxFileSize = maxFileSize;
        }

        public String getAllowedTypes() {
            return allowedTypes;
        }

        public void setAllowedTypes(String allowedTypes) {
            this.allowedTypes = allowedTypes;
        }

        public String[] getAllowedTypesArray() {
            return allowedTypes.split(",");
        }
    }

    /**
     * 缓存配置
     */
    public static class Cache {
        private boolean enabled = true;

        @Min(value = 60, message = "缓存TTL不能小于60秒")
        private long ttl = 3600;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public long getTtl() {
            return ttl;
        }

        public void setTtl(long ttl) {
            this.ttl = ttl;
        }
    }

    /**
     * 安全配置
     */
    public static class Security {
        @Valid
        @NotNull
        private Jwt jwt = new Jwt();

        @Valid
        @NotNull
        private Cors cors = new Cors();

        public Jwt getJwt() {
            return jwt;
        }

        public void setJwt(Jwt jwt) {
            this.jwt = jwt;
        }

        public Cors getCors() {
            return cors;
        }

        public void setCors(Cors cors) {
            this.cors = cors;
        }

        /**
         * JWT配置
         */
        public static class Jwt {
            @NotBlank(message = "JWT密钥不能为空")
            private String secret = "mySecretKey";

            @Min(value = 3600, message = "JWT过期时间不能小于1小时")
            private long expiration = 86400;

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }

            public long getExpiration() {
                return expiration;
            }

            public void setExpiration(long expiration) {
                this.expiration = expiration;
            }
        }

        /**
         * CORS配置
         */
        public static class Cors {
            @NotBlank(message = "允许的源不能为空")
            private String allowedOrigins = "*";

            @NotBlank(message = "允许的方法不能为空")
            private String allowedMethods = "GET,POST,PUT,DELETE,OPTIONS";

            @NotBlank(message = "允许的头部不能为空")
            private String allowedHeaders = "*";

            private boolean allowCredentials = true;

            public String getAllowedOrigins() {
                return allowedOrigins;
            }

            public void setAllowedOrigins(String allowedOrigins) {
                this.allowedOrigins = allowedOrigins;
            }

            public String getAllowedMethods() {
                return allowedMethods;
            }

            public void setAllowedMethods(String allowedMethods) {
                this.allowedMethods = allowedMethods;
            }

            public String getAllowedHeaders() {
                return allowedHeaders;
            }

            public void setAllowedHeaders(String allowedHeaders) {
                this.allowedHeaders = allowedHeaders;
            }

            public boolean isAllowCredentials() {
                return allowCredentials;
            }

            public void setAllowCredentials(boolean allowCredentials) {
                this.allowCredentials = allowCredentials;
            }

            public String[] getAllowedOriginsArray() {
                return allowedOrigins.split(",");
            }

            public String[] getAllowedMethodsArray() {
                return allowedMethods.split(",");
            }

            public String[] getAllowedHeadersArray() {
                return allowedHeaders.split(",");
            }
        }
    }
}
```

---

## 5.2 数据库初始化

### 📚 理论知识

#### 数据初始化方式
1. **schema.sql**：创建表结构
2. **data.sql**：插入初始数据
3. **@Sql注解**：测试时执行SQL
4. **CommandLineRunner**：应用启动时执行
5. **ApplicationRunner**：应用启动时执行

#### 执行顺序
1. schema.sql（创建表结构）
2. data.sql（插入数据）
3. CommandLineRunner/ApplicationRunner

### 🎯 实战任务3：创建数据库初始化脚本

```sql
-- src/main/resources/schema.sql
-- 学生管理系统数据库表结构

-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS students;

-- 创建学生表
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    student_number VARCHAR(20) NOT NULL UNIQUE COMMENT '学号',
    age INT NOT NULL COMMENT '年龄',
    gender VARCHAR(10) NOT NULL COMMENT '性别',
    major VARCHAR(100) NOT NULL COMMENT '专业',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '电话',
    enrollment_date DATE COMMENT '入学日期',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version INT DEFAULT 0 COMMENT '版本号（乐观锁）'
);

-- 创建索引
CREATE INDEX idx_student_number ON students(student_number);
CREATE INDEX idx_student_name ON students(name);
CREATE INDEX idx_student_major ON students(major);
CREATE INDEX idx_student_enrollment_date ON students(enrollment_date);

-- 添加表注释
COMMENT ON TABLE students IS '学生信息表';
```

```sql
-- src/main/resources/data.sql
-- 学生管理系统初始数据

-- 插入测试学生数据
INSERT INTO students (name, student_number, age, gender, major, email, phone, enrollment_date) VALUES
('张三', '20210001', 20, '男', '计算机科学与技术', 'zhangsan@example.com', '13800138001', '2021-09-01'),
('李四', '20210002', 19, '女', '软件工程', 'lisi@example.com', '13800138002', '2021-09-01'),
('王五', '20210003', 21, '男', '信息安全', 'wangwu@example.com', '13800138003', '2021-09-01'),
('赵六', '20210004', 20, '女', '数据科学与大数据技术', 'zhaoliu@example.com', '13800138004', '2021-09-01'),
('钱七', '20210005', 22, '男', '人工智能', 'qianqi@example.com', '13800138005', '2021-09-01'),
('孙八', '20210006', 19, '女', '计算机科学与技术', 'sunba@example.com', '13800138006', '2021-09-01'),
('周九', '20210007', 20, '男', '软件工程', 'zhoujiu@example.com', '13800138007', '2021-09-01'),
('吴十', '20210008', 21, '女', '网络工程', 'wushi@example.com', '13800138008', '2021-09-01'),
('郑十一', '20210009', 19, '男', '物联网工程', 'zhengshiyi@example.com', '13800138009', '2021-09-01'),
('王十二', '20210010', 20, '女', '信息管理与信息系统', 'wangshier@example.com', '13800138010', '2021-09-01'),
('李十三', '20210011', 22, '男', '电子商务', 'lishisan@example.com', '13800138011', '2021-09-01'),
('张十四', '20210012', 19, '女', '数字媒体技术', 'zhangshisi@example.com', '13800138012', '2021-09-01'),
('刘十五', '20210013', 21, '男', '计算机科学与技术', 'liushiwu@example.com', '13800138013', '2021-09-01'),
('陈十六', '20210014', 20, '女', '软件工程', 'chenshiliu@example.com', '13800138014', '2021-09-01'),
('杨十七', '20210015', 19, '男', '信息安全', 'yangshiqi@example.com', '13800138015', '2021-09-01'),
('黄十八', '20220001', 19, '女', '计算机科学与技术', 'huangshiba@example.com', '13800138016', '2022-09-01'),
('林十九', '20220002', 18, '男', '软件工程', 'linshijiu@example.com', '13800138017', '2022-09-01'),
('何二十', '20220003', 19, '女', '数据科学与大数据技术', 'heershi@example.com', '13800138018', '2022-09-01'),
('罗二一', '20220004', 18, '男', '人工智能', 'luoeryi@example.com', '13800138019', '2022-09-01'),
('高二二', '20220005', 19, '女', '信息安全', 'gaoerer@example.com', '13800138020', '2022-09-01'),
('梁二三', '20230001', 18, '男', '计算机科学与技术', 'liangersan@example.com', '13800138021', '2023-09-01'),
('徐二四', '20230002', 18, '女', '软件工程', 'xuersi@example.com', '13800138022', '2023-09-01'),
('谢二五', '20230003', 18, '男', '网络工程', 'xieerwu@example.com', '13800138023', '2023-09-01'),
('唐二六', '20230004', 18, '女', '物联网工程', 'tangerliu@example.com', '13800138024', '2023-09-01'),
('韩二七', '20230005', 18, '男', '数字媒体技术', 'hanerqi@example.com', '13800138025', '2023-09-01');

-- 验证数据插入
-- SELECT COUNT(*) as total_students FROM students;
-- SELECT major, COUNT(*) as count FROM students GROUP BY major ORDER BY count DESC;
-- SELECT YEAR(enrollment_date) as year, COUNT(*) as count FROM students GROUP BY YEAR(enrollment_date) ORDER BY year;
```

### 🎯 实战任务4：创建数据初始化组件

```java
// src/main/java/com/example/studentmanagement/config/DataInitializer.java
package com.example.studentmanagement.config;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * 数据初始化器
 * 
 * 在应用启动时检查并初始化必要的数据
 */
@Component
@Profile({"dev", "test"}) // 只在开发和测试环境运行
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final StudentRepository studentRepository;

    @Autowired
    public DataInitializer(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("开始检查数据初始化状态...");
        
        // 检查是否已有数据
        long studentCount = studentRepository.count();
        logger.info("当前学生数量: {}", studentCount);
        
        if (studentCount == 0) {
            logger.info("未发现学生数据，开始初始化示例数据...");
            initializeStudentData();
        } else {
            logger.info("学生数据已存在，跳过初始化");
        }
        
        // 输出统计信息
        printStatistics();
        
        logger.info("数据初始化检查完成");
    }

    /**
     * 初始化学生数据
     */
    private void initializeStudentData() {
        List<Student> students = Arrays.asList(
            createStudent("张三", "20210001", 20, "男", "计算机科学与技术", 
                         "zhangsan@example.com", "13800138001", LocalDate.of(2021, 9, 1)),
            createStudent("李四", "20210002", 19, "女", "软件工程", 
                         "lisi@example.com", "13800138002", LocalDate.of(2021, 9, 1)),
            createStudent("王五", "20210003", 21, "男", "信息安全", 
                         "wangwu@example.com", "13800138003", LocalDate.of(2021, 9, 1)),
            createStudent("赵六", "20210004", 20, "女", "数据科学与大数据技术", 
                         "zhaoliu@example.com", "13800138004", LocalDate.of(2021, 9, 1)),
            createStudent("钱七", "20210005", 22, "男", "人工智能", 
                         "qianqi@example.com", "13800138005", LocalDate.of(2021, 9, 1))
        );
        
        try {
            studentRepository.saveAll(students);
            logger.info("成功初始化 {} 条学生数据", students.size());
        } catch (Exception e) {
            logger.error("初始化学生数据失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 创建学生对象
     */
    private Student createStudent(String name, String studentNumber, int age, String gender,
                                 String major, String email, String phone, LocalDate enrollmentDate) {
        Student student = new Student();
        student.setName(name);
        student.setStudentNumber(studentNumber);
        student.setAge(age);
        student.setGender(gender);
        student.setMajor(major);
        student.setEmail(email);
        student.setPhone(phone);
        student.setEnrollmentDate(enrollmentDate);
        return student;
    }

    /**
     * 打印统计信息
     */
    private void printStatistics() {
        try {
            long totalStudents = studentRepository.count();
            logger.info("=== 学生数据统计 ===");
            logger.info("学生总数: {}", totalStudents);
            
            if (totalStudents > 0) {
                // 按专业统计
                logger.info("专业分布:");
                List<Object[]> majorStats = studentRepository.findMajorStatistics();
                for (Object[] stat : majorStats) {
                    logger.info("  {}: {} 人", stat[0], stat[1]);
                }
                
                // 按性别统计
                long maleCount = studentRepository.countByGender("男");
                long femaleCount = studentRepository.countByGender("女");
                logger.info("性别分布: 男 {} 人, 女 {} 人", maleCount, femaleCount);
                
                // 年龄统计
                Object[] ageStats = studentRepository.findAgeStatistics();
                if (ageStats != null && ageStats.length >= 3) {
                    logger.info("年龄统计: 最小 {} 岁, 最大 {} 岁, 平均 {:.1f} 岁", 
                               ageStats[0], ageStats[1], ageStats[2]);
                }
            }
            
            logger.info("===================");
        } catch (Exception e) {
            logger.warn("获取统计信息失败: {}", e.getMessage());
        }
    }
}
```

### 🎯 实战任务5：创建配置验证组件

```java
// src/main/java/com/example/studentmanagement/config/ConfigurationValidator.java
package com.example.studentmanagement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 * 配置验证器
 * 
 * 在应用启动时验证关键配置是否正确
 */
@Component
public class ConfigurationValidator implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationValidator.class);

    private final Environment environment;
    private final DataSource dataSource;
    private final AppProperties appProperties;

    @Autowired
    public ConfigurationValidator(Environment environment, 
                                 DataSource dataSource, 
                                 AppProperties appProperties) {
        this.environment = environment;
        this.dataSource = dataSource;
        this.appProperties = appProperties;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("开始验证应用配置...");
        
        validateEnvironment();
        validateDatabase();
        validateCustomProperties();
        
        logger.info("应用配置验证完成");
    }

    /**
     * 验证环境配置
     */
    private void validateEnvironment() {
        logger.info("=== 环境配置验证 ===");
        
        String[] activeProfiles = environment.getActiveProfiles();
        String applicationName = environment.getProperty("spring.application.name");
        String serverPort = environment.getProperty("server.port");
        
        logger.info("应用名称: {}", applicationName);
        logger.info("服务端口: {}", serverPort);
        logger.info("激活的配置文件: {}", String.join(", ", activeProfiles));
        
        // 验证必要的配置
        if (applicationName == null || applicationName.trim().isEmpty()) {
            logger.warn("应用名称未配置");
        }
        
        if (activeProfiles.length == 0) {
            logger.warn("未激活任何配置文件，将使用默认配置");
        }
        
        logger.info("环境配置验证完成");
    }

    /**
     * 验证数据库配置
     */
    private void validateDatabase() {
        logger.info("=== 数据库配置验证 ===");
        
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            logger.info("数据库产品: {}", metaData.getDatabaseProductName());
            logger.info("数据库版本: {}", metaData.getDatabaseProductVersion());
            logger.info("驱动名称: {}", metaData.getDriverName());
            logger.info("驱动版本: {}", metaData.getDriverVersion());
            logger.info("连接URL: {}", metaData.getURL());
            logger.info("用户名: {}", metaData.getUserName());
            
            // 测试数据库连接
            boolean isValid = connection.isValid(5);
            logger.info("数据库连接状态: {}", isValid ? "正常" : "异常");
            
            if (!isValid) {
                logger.error("数据库连接验证失败！");
            }
            
        } catch (Exception e) {
            logger.error("数据库配置验证失败: {}", e.getMessage(), e);
        }
        
        logger.info("数据库配置验证完成");
    }

    /**
     * 验证自定义属性配置
     */
    private void validateCustomProperties() {
        logger.info("=== 自定义配置验证 ===");
        
        try {
            // 验证分页配置
            AppProperties.Pagination pagination = appProperties.getPagination();
            logger.info("分页配置 - 默认大小: {}, 最大大小: {}", 
                       pagination.getDefaultPageSize(), pagination.getMaxPageSize());
            
            // 验证上传配置
            AppProperties.Upload upload = appProperties.getUpload();
            logger.info("上传配置 - 路径: {}, 最大文件大小: {}, 允许类型: {}", 
                       upload.getPath(), upload.getMaxFileSize(), upload.getAllowedTypes());
            
            // 验证缓存配置
            AppProperties.Cache cache = appProperties.getCache();
            logger.info("缓存配置 - 启用: {}, TTL: {} 秒", 
                       cache.isEnabled(), cache.getTtl());
            
            // 验证安全配置
            AppProperties.Security security = appProperties.getSecurity();
            logger.info("安全配置 - JWT过期时间: {} 秒, CORS允许源: {}", 
                       security.getJwt().getExpiration(), 
                       security.getCors().getAllowedOrigins());
            
            logger.info("自定义配置验证通过");
            
        } catch (Exception e) {
            logger.error("自定义配置验证失败: {}", e.getMessage(), e);
        }
        
        logger.info("自定义配置验证完成");
    }
}
```

---

## 5.3 环境配置管理

### 📚 理论知识

#### 配置文件优先级
1. 命令行参数：`--server.port=8081`
2. 系统环境变量：`SERVER_PORT=8081`
3. application-{profile}.yml
4. application.yml
5. 默认配置

#### 外部化配置
```bash
# 通过命令行指定配置文件
java -jar app.jar --spring.config.location=classpath:/custom-config/

# 通过环境变量
export SPRING_PROFILES_ACTIVE=prod
export DATABASE_URL=jdbc:mysql://prod-db:3306/app

# 通过配置中心（如Spring Cloud Config）
spring:
  cloud:
    config:
      uri: http://config-server:8888
```

### 🎯 实战任务6：创建环境配置文件

```yaml
# src/main/resources/application-local.yml
# 本地开发环境配置
spring:
  datasource:
    url: jdbc:h2:file:./data/localdb;AUTO_SERVER=TRUE
    username: sa
    password: 
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  
  h2:
    console:
      enabled: true
      settings:
        web-allow-others: true

logging:
  level:
    com.example.studentmanagement: DEBUG
    org.springframework.web: DEBUG
  file:
    name: logs/local-development.log

app:
  upload:
    path: ./uploads/local
```

```yaml
# src/main/resources/application-docker.yml
# Docker环境配置
spring:
  datasource:
    url: jdbc:mysql://mysql:3306/student_management
    username: ${MYSQL_USER:student_user}
    password: ${MYSQL_PASSWORD:student_pass}
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto: validate
    show-sql: false
  
  h2:
    console:
      enabled: false

logging:
  level:
    root: INFO
    com.example.studentmanagement: INFO
  file:
    name: /app/logs/application.log

app:
  upload:
    path: /app/uploads
```

---

## ✅ 功能验证

### 🎯 实战任务7：创建配置测试类

```java
// src/test/java/com/example/studentmanagement/config/AppPropertiesTest.java
package com.example.studentmanagement.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 应用配置属性测试
 */
@SpringBootTest
@ActiveProfiles("test")
class AppPropertiesTest {

    @Autowired
    private AppProperties appProperties;

    @Test
    void testPaginationProperties() {
        AppProperties.Pagination pagination = appProperties.getPagination();
        
        assertThat(pagination.getDefaultPageSize()).isEqualTo(10);
        assertThat(pagination.getMaxPageSize()).isEqualTo(100);
    }

    @Test
    void testUploadProperties() {
        AppProperties.Upload upload = appProperties.getUpload();
        
        assertThat(upload.getPath()).isEqualTo("/uploads");
        assertThat(upload.getMaxFileSize()).isEqualTo("10MB");
        assertThat(upload.getAllowedTypes()).contains("jpg");
        assertThat(upload.getAllowedTypesArray()).contains("jpg", "png", "pdf");
    }

    @Test
    void testCacheProperties() {
        AppProperties.Cache cache = appProperties.getCache();
        
        assertThat(cache.isEnabled()).isTrue();
        assertThat(cache.getTtl()).isEqualTo(3600);
    }

    @Test
    void testSecurityProperties() {
        AppProperties.Security security = appProperties.getSecurity();
        
        assertThat(security.getJwt().getSecret()).isNotBlank();
        assertThat(security.getJwt().getExpiration()).isGreaterThan(0);
        
        assertThat(security.getCors().getAllowedOrigins()).isNotBlank();
        assertThat(security.getCors().getAllowedMethods()).contains("GET");
    }
}
```

### 🎯 实战任务8：创建数据初始化测试

```java
// src/test/java/com/example/studentmanagement/config/DataInitializerTest.java
package com.example.studentmanagement.config;

import com.example.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 数据初始化测试
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DataInitializerTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testDataInitialization() {
        // 验证数据是否已初始化
        long studentCount = studentRepository.count();
        assertThat(studentCount).isGreaterThan(0);
        
        // 验证特定数据
        boolean existsByNumber = studentRepository.existsByStudentNumber("20210001");
        assertThat(existsByNumber).isTrue();
        
        // 验证专业分布
        long csCount = studentRepository.countByMajor("计算机科学与技术");
        assertThat(csCount).isGreaterThan(0);
    }
}
```

---

## 💡 知识扩展

### 配置加密

```yaml
# 使用Jasypt加密敏感配置
spring:
  datasource:
    password: ENC(encrypted_password)

jasypt:
  encryptor:
    password: ${JASYPT_ENCRYPTOR_PASSWORD}
```

### 配置中心集成

```yaml
# Spring Cloud Config
spring:
  cloud:
    config:
      uri: http://config-server:8888
      profile: ${spring.profiles.active}
      label: master
```

### 健康检查配置

```yaml
management:
  health:
    db:
      enabled: true
    diskspace:
      enabled: true
      threshold: 10MB
  endpoint:
    health:
      show-details: when-authorized
```

---

## 📝 本章小结

✅ **已完成**：
- [x] 完善Spring Boot配置文件
- [x] 创建自定义配置属性类
- [x] 实现数据库初始化脚本
- [x] 创建数据初始化组件
- [x] 实现配置验证器
- [x] 配置多环境管理
- [x] 编写配置测试用例

🎯 **下一章预告**：
在第六章中，我们将开发前端界面，使用HTML、CSS和JavaScript创建用户友好的学生管理界面。

---

## 🏠 课后练习

1. 添加Redis缓存配置
2. 实现配置热更新机制
3. 集成配置中心（如Nacos）
4. 添加配置变更审计日志