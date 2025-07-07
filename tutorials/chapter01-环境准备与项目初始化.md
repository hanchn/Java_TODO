# 第一章：环境准备与项目初始化

## 🎯 学习目标
- 搭建完整的Spring Boot开发环境
- 理解Maven项目结构和依赖管理
- 掌握Spring Boot项目的分层架构设计
- 创建项目基础结构

## ⏱️ 预计用时：30分钟

---

## 1.1 开发环境搭建

### 📚 理论知识

Spring Boot开发需要以下核心工具：
- **JDK**：Java开发工具包，提供Java运行环境
- **Maven**：项目构建和依赖管理工具
- **IDE**：集成开发环境，提高开发效率

### 🛠️ 环境安装步骤

#### 1. JDK安装与配置
```bash
# 检查Java版本
java -version

# 检查编译器版本
javac -version

# 查看JAVA_HOME环境变量
echo $JAVA_HOME
```

**要求**：JDK 8或更高版本

#### 2. Maven安装与配置
```bash
# 检查Maven版本
mvn -version

# 查看Maven配置
mvn help:system
```

**要求**：Maven 3.6或更高版本

#### 3. IDE选择与配置

**推荐IDE**：
- IntelliJ IDEA Community Edition（免费）
- Eclipse IDE for Enterprise Java Developers
- Visual Studio Code + Java扩展包

### ✅ 环境验证

创建一个简单的Java文件验证环境：

```java
// HelloWorld.java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, Spring Boot!");
    }
}
```

```bash
# 编译并运行
javac HelloWorld.java
java HelloWorld
```

---

## 1.2 项目结构设计

### 📚 理论知识

#### Maven项目标准目录结构
```
project-root/
├── pom.xml                 # Maven配置文件
├── src/
│   ├── main/
│   │   ├── java/           # Java源代码
│   │   └── resources/      # 配置文件和静态资源
│   └── test/
│       └── java/           # 测试代码
└── target/                 # 编译输出目录
```

#### Spring Boot分层架构
```
com.example.studentmanagement/
├── StudentManagementApplication.java  # 启动类
├── controller/                        # 控制层
├── service/                          # 业务逻辑层
├── repository/                       # 数据访问层
├── entity/                           # 实体类
├── dto/                              # 数据传输对象
└── exception/                        # 异常处理
```

### 💡 架构设计原则

1. **单一职责**：每层只负责特定功能
2. **依赖倒置**：高层模块不依赖低层模块
3. **开闭原则**：对扩展开放，对修改关闭

---

## 1.3 创建Maven项目

### 🎯 实战任务1：创建项目目录结构

```bash
# 创建项目根目录
mkdir student-management-system
cd student-management-system

# 创建Maven标准目录结构
mkdir -p src/main/java/com/example/studentmanagement
mkdir -p src/main/resources
mkdir -p src/test/java/com/example/studentmanagement
```

### 🎯 实战任务2：创建pom.xml文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!-- 项目基本信息 -->
    <groupId>com.example</groupId>
    <artifactId>student-management-system</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    <name>Student Management System</name>
    <description>基于Spring Boot的学生管理系统</description>

    <!-- Spring Boot父项目 -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.14</version>
        <relativePath/>
    </parent>

    <!-- 项目属性 -->
    <properties>
        <java.version>8</java.version>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <!-- 核心依赖 -->
    <dependencies>
        <!-- Spring Boot Web Starter -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Boot Data JPA Starter -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- Spring Boot Validation Starter -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- H2 Database -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Spring Boot Test Starter -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <!-- 构建配置 -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### 📖 依赖说明

| 依赖 | 作用 |
|------|------|
| spring-boot-starter-web | 提供Web开发功能，包含Spring MVC、Tomcat等 |
| spring-boot-starter-data-jpa | 提供JPA数据持久化功能 |
| spring-boot-starter-validation | 提供数据验证功能 |
| h2 | 内存数据库，适合开发和测试 |
| spring-boot-starter-test | 提供测试框架支持 |

### 🎯 实战任务3：创建启动类

```java
// src/main/java/com/example/studentmanagement/StudentManagementApplication.java
package com.example.studentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 学生管理系统启动类
 * 
 * @SpringBootApplication 注解包含：
 * - @Configuration: 标识为配置类
 * - @EnableAutoConfiguration: 启用自动配置
 * - @ComponentScan: 启用组件扫描
 */
@SpringBootApplication
public class StudentManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApplication.class, args);
        System.out.println("学生管理系统启动成功！");
        System.out.println("访问地址: http://localhost:8080");
    }
}
```

### ✅ 验证项目创建

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

**预期结果**：
- 控制台显示Spring Boot启动日志
- 看到"学生管理系统启动成功！"消息
- 应用在8080端口启动

---

## 💡 知识扩展

### Spring Boot自动配置原理

1. **@SpringBootApplication**注解的作用
2. **starter依赖**的设计思想
3. **约定优于配置**的理念

### Maven生命周期

```bash
# 清理
mvn clean

# 编译
mvn compile

# 测试
mvn test

# 打包
mvn package

# 安装到本地仓库
mvn install
```

---

## 🔍 常见问题

### Q1: Maven下载依赖很慢怎么办？
**A**: 配置国内镜像源，在Maven的settings.xml中添加：
```xml
<mirror>
    <id>aliyun</id>
    <mirrorOf>central</mirrorOf>
    <url>https://maven.aliyun.com/repository/central</url>
</mirror>
```

### Q2: JAVA_HOME环境变量如何设置？
**A**: 
- Windows: 系统属性 → 环境变量 → 新建JAVA_HOME
- macOS/Linux: 在~/.bash_profile中添加export JAVA_HOME=...

### Q3: IDE中如何导入Maven项目？
**A**: 
- IntelliJ IDEA: File → Open → 选择pom.xml文件
- Eclipse: File → Import → Existing Maven Projects

---

## 📝 本章小结

✅ **已完成**：
- [x] 开发环境搭建和验证
- [x] 理解Maven项目结构
- [x] 掌握Spring Boot分层架构
- [x] 创建项目基础结构
- [x] 配置核心依赖
- [x] 创建启动类

🎯 **下一章预告**：
在第二章中，我们将设计数据库表结构，创建Student实体类，并实现数据访问层。

---

## 🏠 课后练习

1. 尝试修改启动端口为8081
2. 添加一个简单的Controller返回"Hello World"
3. 研究pom.xml中每个依赖的作用
4. 了解Spring Boot的其他starter依赖