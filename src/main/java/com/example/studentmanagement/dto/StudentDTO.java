package com.example.studentmanagement.dto;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生数据传输对象
 * 
 * @author System
 * @version 1.0
 */
public class StudentDTO {

    private Long id;

    @NotBlank(message = "学生姓名不能为空")
    @Size(min = 2, max = 20, message = "学生姓名长度必须在2-20个字符之间")
    private String name;

    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "^[0-9]{8,12}$", message = "学号必须是8-12位数字")
    private String studentNumber;

    @NotNull(message = "年龄不能为空")
    @Min(value = 16, message = "年龄不能小于16岁")
    @Max(value = 30, message = "年龄不能大于30岁")
    private Integer age;

    @NotNull(message = "性别不能为空")
    @Pattern(regexp = "^(男|女)$", message = "性别只能是男或女")
    private String gender;

    @NotBlank(message = "专业不能为空")
    @Size(min = 2, max = 50, message = "专业名称长度必须在2-50个字符之间")
    private String major;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private LocalDate enrollmentDate;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    // 构造函数
    public StudentDTO() {
    }

    public StudentDTO(String name, String studentNumber, Integer age, String gender, String major) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.age = age;
        this.gender = gender;
        this.major = major;
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", studentNumber='" + studentNumber + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", major='" + major + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                '}';
    }
}