package com.example.studentmanagement.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生实体类
 * 
 * @author System
 * @version 1.0
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "学生姓名不能为空")
    @Size(min = 2, max = 20, message = "学生姓名长度必须在2-20个字符之间")
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "^[0-9]{8,12}$", message = "学号必须是8-12位数字")
    @Column(name = "student_number", nullable = false, unique = true, length = 12)
    private String studentNumber;

    @NotNull(message = "年龄不能为空")
    @Min(value = 16, message = "年龄不能小于16岁")
    @Max(value = 30, message = "年龄不能大于30岁")
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotNull(message = "性别不能为空")
    @Pattern(regexp = "^(男|女)$", message = "性别只能是男或女")
    @Column(name = "gender", nullable = false, length = 2)
    private String gender;

    @NotBlank(message = "专业不能为空")
    @Size(min = 2, max = 50, message = "专业名称长度必须在2-50个字符之间")
    @Column(name = "major", nullable = false, length = 50)
    private String major;

    @Email(message = "邮箱格式不正确")
    @Column(name = "email", length = 100)
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    // 构造函数
    public Student() {
    }

    public Student(String name, String studentNumber, Integer age, String gender, String major) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.age = age;
        this.gender = gender;
        this.major = major;
        this.enrollmentDate = LocalDate.now();
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }

    // JPA生命周期回调
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
        if (enrollmentDate == null) {
            enrollmentDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
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
        return "Student{" +
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