package com.example.studentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 学生管理系统主启动类
 * 
 * @author System
 * @version 1.0
 */
@SpringBootApplication
public class StudentManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApplication.class, args);
        System.out.println("学生管理系统启动成功！");
        System.out.println("访问地址: http://localhost:8080");
    }
}