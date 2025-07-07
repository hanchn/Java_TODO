# 第三章：业务逻辑层开发

## 🎯 学习目标
- 理解DTO设计模式及其作用
- 掌握统一响应格式的设计
- 实现完整的业务逻辑层
- 学习Service接口和实现类的最佳实践

## ⏱️ 预计用时：60分钟

---

## 3.1 DTO设计模式

### 📚 理论知识

#### 什么是DTO？
**DTO（Data Transfer Object）**是数据传输对象，用于在不同层之间传输数据。

#### DTO的作用
1. **数据封装**：隐藏内部实现细节
2. **减少网络传输**：只传输必要的数据
3. **版本控制**：API版本变更时保持兼容性
4. **数据验证**：在传输层进行数据校验
5. **安全性**：避免直接暴露实体类

#### Entity vs DTO

| 特性 | Entity | DTO |
|------|--------|-----|
| 用途 | 数据库映射 | 数据传输 |
| 注解 | JPA注解 | 验证注解 |
| 字段 | 包含所有数据库字段 | 只包含需要传输的字段 |
| 生命周期 | 由JPA管理 | 请求响应周期 |

### 🎯 实战任务1：创建StudentDTO类

```java
// src/main/java/com/example/studentmanagement/dto/StudentDTO.java
package com.example.studentmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生数据传输对象
 * 
 * 用于前后端数据传输，包含数据验证注解
 */
public class StudentDTO {

    /**
     * 学生ID（更新时需要）
     */
    private Long id;

    /**
     * 学生姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String name;

    /**
     * 学号
     */
    @NotBlank(message = "学号不能为空")
    @Size(max = 20, message = "学号长度不能超过20个字符")
    @Pattern(regexp = "^[0-9]{8}$", message = "学号必须是8位数字")
    private String studentNumber;

    /**
     * 年龄
     */
    @NotNull(message = "年龄不能为空")
    @Min(value = 16, message = "年龄不能小于16岁")
    @Max(value = 35, message = "年龄不能超过35岁")
    private Integer age;

    /**
     * 性别
     */
    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "^(男|女)$", message = "性别只能是男或女")
    private String gender;

    /**
     * 专业
     */
    @NotBlank(message = "专业不能为空")
    @Size(max = 100, message = "专业名称长度不能超过100个字符")
    private String major;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /**
     * 电话号码
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    /**
     * 入学日期
     */
    @NotNull(message = "入学日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate enrollmentDate;

    /**
     * 创建时间（只读）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    /**
     * 更新时间（只读）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    // 默认构造函数
    public StudentDTO() {}

    // 带参数的构造函数
    public StudentDTO(String name, String studentNumber, Integer age, String gender, 
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
                '}';
    }
}
```

---

## 3.2 统一响应格式设计

### 📚 理论知识

#### 为什么需要统一响应格式？
1. **一致性**：所有API返回格式统一
2. **可维护性**：便于前端统一处理
3. **扩展性**：便于添加通用字段
4. **错误处理**：统一的错误信息格式

#### 响应格式设计原则
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": "2023-12-01T10:30:00"
}
```

### 🎯 实战任务2：创建ApiResponse通用响应类

```java
// src/main/java/com/example/studentmanagement/dto/ApiResponse.java
package com.example.studentmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * 统一API响应格式
 * 
 * @param <T> 响应数据类型
 */
public class ApiResponse<T> {

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    // 私有构造函数
    private ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    private ApiResponse(int code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应（带数据）
     * 
     * @param data 响应数据
     * @param message 成功消息
     * @param <T> 数据类型
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(200, message, data);
    }

    /**
     * 成功响应（带数据，默认消息）
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "操作成功");
    }

    /**
     * 成功响应（无数据）
     * 
     * @param message 成功消息
     * @param <T> 数据类型
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(200, message, null);
    }

    /**
     * 成功响应（无数据，默认消息）
     * 
     * @param <T> 数据类型
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> success() {
        return success("操作成功");
    }

    /**
     * 失败响应
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    /**
     * 失败响应（默认错误码500）
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> error(String message) {
        return error(500, message);
    }

    /**
     * 参数验证失败响应
     * 
     * @param message 验证失败消息
     * @param <T> 数据类型
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> validationError(String message) {
        return error(400, message);
    }

    /**
     * 资源不存在响应
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return error(404, message);
    }

    /**
     * 判断响应是否成功
     * 
     * @return 是否成功
     */
    public boolean isSuccess() {
        return this.code == 200;
    }

    // Getter和Setter方法
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}
```

### 🎯 实战任务3：创建分页响应类

```java
// src/main/java/com/example/studentmanagement/dto/PageResponse.java
package com.example.studentmanagement.dto;

import org.springframework.data.domain.Page;
import java.util.List;

/**
 * 分页响应数据
 * 
 * @param <T> 数据类型
 */
public class PageResponse<T> {

    /**
     * 数据列表
     */
    private List<T> content;

    /**
     * 当前页码（从0开始）
     */
    private int page;

    /**
     * 每页大小
     */
    private int size;

    /**
     * 总元素数
     */
    private long totalElements;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 是否为第一页
     */
    private boolean first;

    /**
     * 是否为最后一页
     */
    private boolean last;

    /**
     * 是否有下一页
     */
    private boolean hasNext;

    /**
     * 是否有上一页
     */
    private boolean hasPrevious;

    // 默认构造函数
    public PageResponse() {}

    /**
     * 从Spring Data的Page对象创建PageResponse
     * 
     * @param page Spring Data Page对象
     */
    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
    }

    /**
     * 创建分页响应的静态方法
     * 
     * @param page Spring Data Page对象
     * @param <T> 数据类型
     * @return PageResponse对象
     */
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(page);
    }

    // Getter和Setter方法
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}
```

---

## 3.3 业务服务层开发

### 📚 理论知识

#### Service层的职责
1. **业务逻辑处理**：实现具体的业务规则
2. **事务管理**：控制数据库事务
3. **数据转换**：Entity与DTO之间的转换
4. **异常处理**：处理业务异常
5. **调用协调**：协调多个Repository的调用

#### Service设计原则
1. **单一职责**：每个Service只负责一个业务领域
2. **接口隔离**：定义清晰的Service接口
3. **依赖注入**：通过构造函数注入依赖
4. **事务边界**：合理定义事务范围

### 🎯 实战任务4：创建StudentService接口

```java
// src/main/java/com/example/studentmanagement/service/StudentService.java
package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 学生业务服务接口
 */
public interface StudentService {

    /**
     * 创建学生
     * 
     * @param studentDTO 学生信息
     * @return 创建后的学生信息
     */
    StudentDTO createStudent(StudentDTO studentDTO);

    /**
     * 根据ID获取学生信息
     * 
     * @param id 学生ID
     * @return 学生信息
     */
    StudentDTO getStudentById(Long id);

    /**
     * 根据学号获取学生信息
     * 
     * @param studentNumber 学号
     * @return 学生信息
     */
    StudentDTO getStudentByNumber(String studentNumber);

    /**
     * 获取所有学生（分页）
     * 
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<StudentDTO> getAllStudents(Pageable pageable);

    /**
     * 根据条件搜索学生
     * 
     * @param name 姓名关键字
     * @param major 专业
     * @param gender 性别
     * @param pageable 分页参数
     * @return 搜索结果
     */
    Page<StudentDTO> searchStudents(String name, String major, String gender, Pageable pageable);

    /**
     * 更新学生信息
     * 
     * @param id 学生ID
     * @param studentDTO 更新的学生信息
     * @return 更新后的学生信息
     */
    StudentDTO updateStudent(Long id, StudentDTO studentDTO);

    /**
     * 删除学生
     * 
     * @param id 学生ID
     */
    void deleteStudent(Long id);

    /**
     * 批量删除学生
     * 
     * @param ids 学生ID列表
     */
    void deleteStudents(List<Long> ids);

    /**
     * 检查学号是否存在
     * 
     * @param studentNumber 学号
     * @return 是否存在
     */
    boolean existsByStudentNumber(String studentNumber);

    /**
     * 检查学号是否存在（排除指定ID）
     * 
     * @param studentNumber 学号
     * @param excludeId 要排除的学生ID
     * @return 是否存在
     */
    boolean existsByStudentNumber(String studentNumber, Long excludeId);

    /**
     * 获取学生统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getStudentStatistics();

    /**
     * 获取专业分布统计
     * 
     * @return 专业统计列表
     */
    List<Map<String, Object>> getMajorStatistics();

    /**
     * 获取年龄分布统计
     * 
     * @return 年龄分布统计
     */
    List<Map<String, Object>> getAgeDistribution();
}
```

### 🎯 实战任务5：创建StudentServiceImpl实现类

```java
// src/main/java/com/example/studentmanagement/service/impl/StudentServiceImpl.java
package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 学生业务服务实现类
 */
@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    public StudentDTO createStudent(StudentDTO studentDTO) {
        logger.info("创建学生: {}", studentDTO.getName());
        
        // 检查学号是否已存在
        if (studentRepository.existsByStudentNumber(studentDTO.getStudentNumber())) {
            throw new IllegalArgumentException("学号已存在: " + studentDTO.getStudentNumber());
        }
        
        // 检查邮箱是否已存在
        if (StringUtils.hasText(studentDTO.getEmail()) && 
            studentRepository.findByEmail(studentDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("邮箱已存在: " + studentDTO.getEmail());
        }
        
        // DTO转Entity
        Student student = convertToEntity(studentDTO);
        
        // 保存学生
        Student savedStudent = studentRepository.save(student);
        
        logger.info("学生创建成功，ID: {}", savedStudent.getId());
        return convertToDTO(savedStudent);
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        logger.debug("根据ID获取学生: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("学生不存在，ID: " + id));
        
        return convertToDTO(student);
    }

    @Override
    public StudentDTO getStudentByNumber(String studentNumber) {
        logger.debug("根据学号获取学生: {}", studentNumber);
        
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new IllegalArgumentException("学生不存在，学号: " + studentNumber));
        
        return convertToDTO(student);
    }

    @Override
    public Page<StudentDTO> getAllStudents(Pageable pageable) {
        logger.debug("获取学生列表，页码: {}, 大小: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage.map(this::convertToDTO);
    }

    @Override
    public Page<StudentDTO> searchStudents(String name, String major, String gender, Pageable pageable) {
        logger.debug("搜索学生 - 姓名: {}, 专业: {}, 性别: {}", name, major, gender);
        
        Page<Student> studentPage = studentRepository.findStudentsWithFilters(name, major, gender, pageable);
        return studentPage.map(this::convertToDTO);
    }

    @Override
    @Transactional
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        logger.info("更新学生信息，ID: {}", id);
        
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("学生不存在，ID: " + id));
        
        // 检查学号是否被其他学生使用
        if (!existingStudent.getStudentNumber().equals(studentDTO.getStudentNumber()) &&
            studentRepository.existsByStudentNumber(studentDTO.getStudentNumber())) {
            throw new IllegalArgumentException("学号已存在: " + studentDTO.getStudentNumber());
        }
        
        // 检查邮箱是否被其他学生使用
        if (StringUtils.hasText(studentDTO.getEmail()) &&
            studentRepository.existsByEmailAndIdNot(studentDTO.getEmail(), id)) {
            throw new IllegalArgumentException("邮箱已存在: " + studentDTO.getEmail());
        }
        
        // 更新字段（保留创建时间）
        BeanUtils.copyProperties(studentDTO, existingStudent, "id", "createdTime", "updatedTime");
        
        Student updatedStudent = studentRepository.save(existingStudent);
        
        logger.info("学生信息更新成功，ID: {}", id);
        return convertToDTO(updatedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        logger.info("删除学生，ID: {}", id);
        
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("学生不存在，ID: " + id);
        }
        
        studentRepository.deleteById(id);
        logger.info("学生删除成功，ID: {}", id);
    }

    @Override
    @Transactional
    public void deleteStudents(List<Long> ids) {
        logger.info("批量删除学生，数量: {}", ids.size());
        
        List<Student> studentsToDelete = studentRepository.findAllById(ids);
        if (studentsToDelete.size() != ids.size()) {
            throw new IllegalArgumentException("部分学生不存在");
        }
        
        studentRepository.deleteAllById(ids);
        logger.info("批量删除学生成功，数量: {}", ids.size());
    }

    @Override
    public boolean existsByStudentNumber(String studentNumber) {
        return studentRepository.existsByStudentNumber(studentNumber);
    }

    @Override
    public boolean existsByStudentNumber(String studentNumber, Long excludeId) {
        return studentRepository.existsByStudentNumberAndIdNot(studentNumber, excludeId);
    }

    @Override
    public Map<String, Object> getStudentStatistics() {
        logger.debug("获取学生统计信息");
        
        Map<String, Object> statistics = new HashMap<>();
        
        // 总学生数
        long totalStudents = studentRepository.count();
        statistics.put("totalStudents", totalStudents);
        
        // 男女比例
        long maleCount = studentRepository.countByGender("男");
        long femaleCount = studentRepository.countByGender("女");
        statistics.put("maleCount", maleCount);
        statistics.put("femaleCount", femaleCount);
        
        // 专业数量
        List<Object[]> majorStats = studentRepository.getMajorStatistics();
        long majorCount = majorStats.size();
        statistics.put("majorCount", majorCount);
        
        return statistics;
    }

    @Override
    public List<Map<String, Object>> getMajorStatistics() {
        logger.debug("获取专业统计信息");
        
        List<Object[]> results = studentRepository.getMajorStatistics();
        return results.stream()
                .map(result -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("major", result[0]);
                    stat.put("count", result[1]);
                    return stat;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getAgeDistribution() {
        logger.debug("获取年龄分布统计");
        
        List<Object[]> results = studentRepository.getAgeDistribution();
        return results.stream()
                .map(result -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("ageGroup", result[0]);
                    stat.put("count", result[1]);
                    return stat;
                })
                .collect(Collectors.toList());
    }

    /**
     * Entity转DTO
     * 
     * @param student 学生实体
     * @return 学生DTO
     */
    private StudentDTO convertToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        BeanUtils.copyProperties(student, dto);
        return dto;
    }

    /**
     * DTO转Entity
     * 
     * @param studentDTO 学生DTO
     * @return 学生实体
     */
    private Student convertToEntity(StudentDTO studentDTO) {
        Student student = new Student();
        BeanUtils.copyProperties(studentDTO, student, "id", "createdTime", "updatedTime");
        return student;
    }
}
```

---

## ✅ 功能验证

### 🎯 实战任务6：创建Service测试类

```java
// src/test/java/com/example/studentmanagement/service/StudentServiceTest.java
package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 学生服务测试类
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private StudentDTO testStudentDTO;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudentDTO = new StudentDTO(
            "张三",
            "20210001",
            20,
            "男",
            "计算机科学与技术",
            "zhangsan@example.com",
            "13800138001",
            LocalDate.of(2021, 9, 1)
        );

        testStudent = new Student(
            "张三",
            "20210001",
            20,
            "男",
            "计算机科学与技术",
            "zhangsan@example.com",
            "13800138001",
            LocalDate.of(2021, 9, 1)
        );
        testStudent.setId(1L);
    }

    @Test
    void testCreateStudent() {
        // 模拟Repository行为
        when(studentRepository.existsByStudentNumber("20210001")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // 执行测试
        StudentDTO result = studentService.createStudent(testStudentDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals("张三", result.getName());
        assertEquals("20210001", result.getStudentNumber());
        
        // 验证Repository调用
        verify(studentRepository).existsByStudentNumber("20210001");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void testCreateStudentWithDuplicateNumber() {
        // 模拟学号已存在
        when(studentRepository.existsByStudentNumber("20210001")).thenReturn(true);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> studentService.createStudent(testStudentDTO)
        );
        
        assertEquals("学号已存在: 20210001", exception.getMessage());
    }

    @Test
    void testGetStudentById() {
        // 模拟Repository行为
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // 执行测试
        StudentDTO result = studentService.getStudentById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("张三", result.getName());
        assertEquals("20210001", result.getStudentNumber());
    }

    @Test
    void testGetStudentByIdNotFound() {
        // 模拟学生不存在
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> studentService.getStudentById(999L)
        );
        
        assertEquals("学生不存在，ID: 999", exception.getMessage());
    }

    @Test
    void testGetAllStudents() {
        // 准备测试数据
        Pageable pageable = PageRequest.of(0, 10);
        Page<Student> studentPage = new PageImpl<>(Arrays.asList(testStudent));
        
        // 模拟Repository行为
        when(studentRepository.findAll(pageable)).thenReturn(studentPage);

        // 执行测试
        Page<StudentDTO> result = studentService.getAllStudents(pageable);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("张三", result.getContent().get(0).getName());
    }

    @Test
    void testDeleteStudent() {
        // 模拟Repository行为
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);

        // 执行测试
        assertDoesNotThrow(() -> studentService.deleteStudent(1L));

        // 验证Repository调用
        verify(studentRepository).existsById(1L);
        verify(studentRepository).deleteById(1L);
    }
}
```

---

## 💡 知识扩展

### 事务管理最佳实践

1. **只读事务**：查询方法使用`@Transactional(readOnly = true)`
2. **事务边界**：在Service层定义事务边界
3. **异常回滚**：默认只有RuntimeException会回滚
4. **事务传播**：理解不同的传播行为

### 数据转换工具

除了手动转换，还可以使用：
- **MapStruct**：编译时生成转换代码
- **ModelMapper**：运行时反射转换
- **BeanUtils**：Spring提供的简单转换

---

## 📝 本章小结

✅ **已完成**：
- [x] 创建StudentDTO数据传输对象
- [x] 设计统一的API响应格式
- [x] 实现分页响应类
- [x] 定义StudentService接口
- [x] 实现StudentServiceImpl业务逻辑
- [x] 编写Service层测试用例

🎯 **下一章预告**：
在第四章中，我们将创建RESTful API控制器，实现HTTP接口，并添加全局异常处理。

---

## 🏠 课后练习

1. 为StudentService添加批量导入功能
2. 实现学生信息的软删除（逻辑删除）
3. 添加学生信息变更历史记录功能
4. 优化数据转换性能，考虑使用MapStruct