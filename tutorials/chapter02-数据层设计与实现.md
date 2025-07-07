# 第二章：数据层设计与实现

## 🎯 学习目标
- 设计学生信息数据库表结构
- 掌握JPA实体类的创建和注解使用
- 理解Spring Data JPA的Repository模式
- 实现基础的数据访问功能

## ⏱️ 预计用时：45分钟

---

## 2.1 数据库设计

### 📚 理论知识

#### 数据库设计原则
1. **规范化**：减少数据冗余
2. **完整性**：确保数据准确性
3. **一致性**：保持数据逻辑一致
4. **可扩展性**：便于后续功能扩展

#### 学生信息表设计

| 字段名 | 数据类型 | 长度 | 约束 | 说明 |
|--------|----------|------|------|------|
| id | BIGINT | - | PRIMARY KEY, AUTO_INCREMENT | 主键ID |
| name | VARCHAR | 50 | NOT NULL | 学生姓名 |
| student_number | VARCHAR | 20 | NOT NULL, UNIQUE | 学号 |
| age | INTEGER | - | NOT NULL, CHECK(age > 0) | 年龄 |
| gender | VARCHAR | 10 | NOT NULL | 性别 |
| major | VARCHAR | 100 | NOT NULL | 专业 |
| email | VARCHAR | 100 | UNIQUE | 邮箱 |
| phone | VARCHAR | 20 | - | 电话号码 |
| enrollment_date | DATE | - | NOT NULL | 入学日期 |
| created_time | TIMESTAMP | - | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_time | TIMESTAMP | - | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 💡 设计考虑

1. **主键选择**：使用自增长的BIGINT类型，避免业务字段作为主键
2. **唯一约束**：学号和邮箱需要保证唯一性
3. **时间戳**：记录数据的创建和更新时间，便于审计
4. **数据验证**：在数据库层面添加基础约束

---

## 2.2 实体类开发

### 📚 JPA注解详解

| 注解 | 作用 | 示例 |
|------|------|------|
| @Entity | 标识JPA实体类 | @Entity |
| @Table | 指定数据库表名 | @Table(name = "students") |
| @Id | 标识主键字段 | @Id |
| @GeneratedValue | 主键生成策略 | @GeneratedValue(strategy = GenerationType.IDENTITY) |
| @Column | 指定列属性 | @Column(name = "student_number", unique = true) |
| @NotNull | 非空验证 | @NotNull(message = "姓名不能为空") |
| @Size | 长度验证 | @Size(max = 50, message = "姓名长度不能超过50个字符") |
| @Email | 邮箱格式验证 | @Email(message = "邮箱格式不正确") |

### 🎯 实战任务1：创建Student实体类

```java
// src/main/java/com/example/studentmanagement/entity/Student.java
package com.example.studentmanagement.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 学生实体类
 * 
 * 使用JPA注解映射到数据库表
 * 使用Bean Validation注解进行数据验证
 */
@Entity
@Table(name = "students")
public class Student {

    /**
     * 主键ID，自动生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 学生姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * 学号，唯一标识
     */
    @NotBlank(message = "学号不能为空")
    @Size(max = 20, message = "学号长度不能超过20个字符")
    @Column(name = "student_number", nullable = false, unique = true, length = 20)
    private String studentNumber;

    /**
     * 年龄
     */
    @NotNull(message = "年龄不能为空")
    @Min(value = 1, message = "年龄必须大于0")
    @Max(value = 150, message = "年龄不能超过150")
    @Column(name = "age", nullable = false)
    private Integer age;

    /**
     * 性别
     */
    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "^(男|女)$", message = "性别只能是男或女")
    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    /**
     * 专业
     */
    @NotBlank(message = "专业不能为空")
    @Size(max = 100, message = "专业名称长度不能超过100个字符")
    @Column(name = "major", nullable = false, length = 100)
    private String major;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    @Column(name = "email", unique = true, length = 100)
    private String email;

    /**
     * 电话号码
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * 入学日期
     */
    @NotNull(message = "入学日期不能为空")
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    /**
     * 创建时间
     */
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time", nullable = false)
    private LocalDateTime updatedTime;

    /**
     * JPA生命周期回调方法
     * 在持久化之前自动设置创建时间和更新时间
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdTime = now;
        this.updatedTime = now;
    }

    /**
     * JPA生命周期回调方法
     * 在更新之前自动设置更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedTime = LocalDateTime.now();
    }

    // 默认构造函数（JPA要求）
    public Student() {}

    // 带参数的构造函数
    public Student(String name, String studentNumber, Integer age, String gender, 
                   String major, String email, String phone, LocalDate enrollmentDate) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.age = age;
        this.gender = gender;
        this.major = major;
        this.email = email;
        this.phone = phone;
        this.enrollmentDate = enrollmentDate;
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

    // equals和hashCode方法（基于业务主键：学号）
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(studentNumber, student.studentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentNumber);
    }

    // toString方法
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
```

### 💡 代码解析

1. **实体映射**：@Entity和@Table注解将Java类映射到数据库表
2. **主键策略**：使用IDENTITY策略，依赖数据库自动生成主键
3. **数据验证**：使用Bean Validation注解进行数据校验
4. **生命周期回调**：@PrePersist和@PreUpdate自动管理时间戳
5. **业务方法**：equals和hashCode基于业务主键（学号）实现

---

## 2.3 数据访问层开发

### 📚 Spring Data JPA Repository模式

#### Repository接口层次结构
```
Repository<T, ID>
    ↓
CrudRepository<T, ID>
    ↓
PagingAndSortingRepository<T, ID>
    ↓
JpaRepository<T, ID>
```

#### 方法命名规则

| 关键字 | 示例 | JPQL片段 |
|--------|------|----------|
| findBy | findByName | where x.name = ?1 |
| findByAnd | findByNameAndAge | where x.name = ?1 and x.age = ?2 |
| findByOr | findByNameOrAge | where x.name = ?1 or x.age = ?2 |
| findByOrderBy | findByAgeOrderByName | where x.age = ?1 order by x.name |
| findByContaining | findByNameContaining | where x.name like %?1% |
| findByIgnoreCase | findByNameIgnoreCase | where UPPER(x.name) = UPPER(?1) |

### 🎯 实战任务2：创建StudentRepository接口

```java
// src/main/java/com/example/studentmanagement/repository/StudentRepository.java
package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 学生数据访问接口
 * 
 * 继承JpaRepository获得基础CRUD功能
 * 定义自定义查询方法
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * 根据学号查找学生
     * 
     * @param studentNumber 学号
     * @return 学生信息（可能为空）
     */
    Optional<Student> findByStudentNumber(String studentNumber);

    /**
     * 根据姓名模糊查询学生（忽略大小写）
     * 
     * @param name 姓名关键字
     * @return 学生列表
     */
    List<Student> findByNameContainingIgnoreCase(String name);

    /**
     * 根据专业查找学生
     * 
     * @param major 专业名称
     * @return 学生列表
     */
    List<Student> findByMajor(String major);

    /**
     * 根据性别查找学生
     * 
     * @param gender 性别
     * @return 学生列表
     */
    List<Student> findByGender(String gender);

    /**
     * 根据年龄范围查找学生
     * 
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @return 学生列表
     */
    List<Student> findByAgeBetween(Integer minAge, Integer maxAge);

    /**
     * 根据入学日期范围查找学生
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 学生列表
     */
    List<Student> findByEnrollmentDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 根据专业和性别查找学生（分页）
     * 
     * @param major 专业
     * @param gender 性别
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<Student> findByMajorAndGender(String major, String gender, Pageable pageable);

    /**
     * 检查学号是否已存在
     * 
     * @param studentNumber 学号
     * @return 是否存在
     */
    boolean existsByStudentNumber(String studentNumber);

    /**
     * 检查邮箱是否已存在（排除指定ID）
     * 
     * @param email 邮箱
     * @param id 要排除的学生ID
     * @return 是否存在
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * 统计指定专业的学生数量
     * 
     * @param major 专业
     * @return 学生数量
     */
    long countByMajor(String major);

    /**
     * 统计指定性别的学生数量
     * 
     * @param gender 性别
     * @return 学生数量
     */
    long countByGender(String gender);

    /**
     * 使用自定义JPQL查询：根据多个条件搜索学生
     * 
     * @param name 姓名关键字
     * @param major 专业
     * @param gender 性别
     * @param pageable 分页参数
     * @return 分页结果
     */
    @Query("SELECT s FROM Student s WHERE " +
           "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:major IS NULL OR s.major = :major) AND " +
           "(:gender IS NULL OR s.gender = :gender)")
    Page<Student> findStudentsWithFilters(@Param("name") String name,
                                         @Param("major") String major,
                                         @Param("gender") String gender,
                                         Pageable pageable);

    /**
     * 使用原生SQL查询：获取专业统计信息
     * 
     * @return 专业统计列表（专业名称，学生数量）
     */
    @Query(value = "SELECT major, COUNT(*) as count FROM students GROUP BY major ORDER BY count DESC", 
           nativeQuery = true)
    List<Object[]> getMajorStatistics();

    /**
     * 使用原生SQL查询：获取年龄分布统计
     * 
     * @return 年龄分布统计
     */
    @Query(value = "SELECT " +
           "CASE " +
           "  WHEN age < 20 THEN '20岁以下' " +
           "  WHEN age BETWEEN 20 AND 22 THEN '20-22岁' " +
           "  WHEN age BETWEEN 23 AND 25 THEN '23-25岁' " +
           "  ELSE '25岁以上' " +
           "END as age_group, " +
           "COUNT(*) as count " +
           "FROM students " +
           "GROUP BY age_group " +
           "ORDER BY age_group", 
           nativeQuery = true)
    List<Object[]> getAgeDistribution();
}
```

### 💡 Repository设计要点

1. **方法命名**：遵循Spring Data JPA命名规范
2. **返回类型**：合理选择Optional、List、Page等返回类型
3. **自定义查询**：使用@Query注解编写复杂查询
4. **参数绑定**：使用@Param注解绑定查询参数
5. **性能考虑**：合理使用分页和索引

---

## ✅ 功能验证

### 🎯 实战任务3：创建简单的测试类

```java
// src/test/java/com/example/studentmanagement/repository/StudentRepositoryTest.java
package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 学生Repository测试类
 */
@DataJpaTest
@SpringJUnitConfig
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testSaveAndFindStudent() {
        // 创建测试学生
        Student student = new Student(
            "张三",
            "20210001",
            20,
            "男",
            "计算机科学与技术",
            "zhangsan@example.com",
            "13800138001",
            LocalDate.of(2021, 9, 1)
        );

        // 保存学生
        Student savedStudent = studentRepository.save(student);
        assertNotNull(savedStudent.getId());
        assertNotNull(savedStudent.getCreatedTime());
        assertNotNull(savedStudent.getUpdatedTime());

        // 根据ID查找
        Optional<Student> foundStudent = studentRepository.findById(savedStudent.getId());
        assertTrue(foundStudent.isPresent());
        assertEquals("张三", foundStudent.get().getName());

        // 根据学号查找
        Optional<Student> studentByNumber = studentRepository.findByStudentNumber("20210001");
        assertTrue(studentByNumber.isPresent());
        assertEquals("张三", studentByNumber.get().getName());
    }

    @Test
    void testExistsByStudentNumber() {
        // 创建测试学生
        Student student = new Student(
            "李四",
            "20210002",
            21,
            "女",
            "软件工程",
            "lisi@example.com",
            "13800138002",
            LocalDate.of(2021, 9, 1)
        );
        studentRepository.save(student);

        // 测试学号是否存在
        assertTrue(studentRepository.existsByStudentNumber("20210002"));
        assertFalse(studentRepository.existsByStudentNumber("20210999"));
    }
}
```

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=StudentRepositoryTest
```

---

## 💡 知识扩展

### JPA vs MyBatis

| 特性 | JPA | MyBatis |
|------|-----|----------|
| 学习曲线 | 较陡峭 | 相对平缓 |
| SQL控制 | 自动生成 | 手动编写 |
| 对象关系映射 | 强 | 弱 |
| 性能优化 | 自动优化 | 手动优化 |
| 适用场景 | 快速开发 | 复杂查询 |

### 数据库索引建议

```sql
-- 为常用查询字段创建索引
CREATE INDEX idx_student_number ON students(student_number);
CREATE INDEX idx_major ON students(major);
CREATE INDEX idx_enrollment_date ON students(enrollment_date);
CREATE INDEX idx_name ON students(name);
```

---

## 🔍 常见问题

### Q1: 为什么要使用Optional？
**A**: Optional可以避免空指针异常，明确表示方法可能返回空值，提高代码安全性。

### Q2: @PrePersist和@PreUpdate什么时候执行？
**A**: 
- @PrePersist：在实体首次保存到数据库之前执行
- @PreUpdate：在实体更新到数据库之前执行

### Q3: 如何处理大数据量查询？
**A**: 
- 使用分页查询（Pageable）
- 添加适当的数据库索引
- 考虑使用@Query优化查询
- 避免一次性加载所有数据

---

## 📝 本章小结

✅ **已完成**：
- [x] 设计学生信息数据库表结构
- [x] 创建Student实体类并配置JPA注解
- [x] 实现数据验证注解
- [x] 创建StudentRepository接口
- [x] 定义各种查询方法
- [x] 编写基础测试用例

🎯 **下一章预告**：
在第三章中，我们将创建DTO类、统一响应格式，并实现业务逻辑层的Service接口和实现类。

---

## 🏠 课后练习

1. 为Student实体添加一个"班级"字段
2. 在Repository中添加根据班级查询的方法
3. 编写更多的测试用例验证查询功能
4. 研究JPA的其他注解如@OneToMany、@ManyToOne等