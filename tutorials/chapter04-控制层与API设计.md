# 第四章：控制层与API设计

## 🎯 学习目标
- 掌握RESTful API设计原则和最佳实践
- 学习Spring MVC控制器的开发
- 实现完整的CRUD API接口
- 掌握全局异常处理机制

## ⏱️ 预计用时：45分钟

---

## 4.1 RESTful API设计原则

### 📚 理论知识

#### 什么是REST？
**REST（Representational State Transfer）**是一种软件架构风格，用于设计网络应用程序的接口。

#### REST核心原则
1. **资源（Resource）**：一切皆资源，每个资源都有唯一的URI
2. **表现层（Representation）**：资源的具体表现形式（JSON、XML等）
3. **状态转移（State Transfer）**：通过HTTP方法实现资源状态的转移
4. **无状态（Stateless）**：每个请求都包含处理该请求所需的所有信息

#### HTTP方法与CRUD操作映射

| HTTP方法 | CRUD操作 | 描述 | 示例 |
|----------|----------|------|------|
| GET | Read | 获取资源 | GET /api/students |
| POST | Create | 创建资源 | POST /api/students |
| PUT | Update | 更新资源（完整更新） | PUT /api/students/1 |
| PATCH | Update | 更新资源（部分更新） | PATCH /api/students/1 |
| DELETE | Delete | 删除资源 | DELETE /api/students/1 |

#### RESTful URL设计规范

```
# 资源集合
GET    /api/students          # 获取学生列表
POST   /api/students          # 创建新学生

# 单个资源
GET    /api/students/{id}     # 获取指定学生
PUT    /api/students/{id}     # 更新指定学生
DELETE /api/students/{id}     # 删除指定学生

# 资源搜索和过滤
GET    /api/students?name=张三&major=计算机
GET    /api/students?page=0&size=10&sort=name,asc

# 统计和聚合
GET    /api/students/statistics
GET    /api/students/count
```

#### HTTP状态码规范

| 状态码 | 含义 | 使用场景 |
|--------|------|----------|
| 200 | OK | 请求成功 |
| 201 | Created | 资源创建成功 |
| 204 | No Content | 删除成功，无返回内容 |
| 400 | Bad Request | 请求参数错误 |
| 401 | Unauthorized | 未授权 |
| 403 | Forbidden | 禁止访问 |
| 404 | Not Found | 资源不存在 |
| 409 | Conflict | 资源冲突 |
| 500 | Internal Server Error | 服务器内部错误 |

---

## 4.2 控制器开发

### 📚 Spring MVC注解详解

| 注解 | 作用 | 示例 |
|------|------|------|
| @RestController | 标识REST控制器 | @RestController |
| @RequestMapping | 映射请求路径 | @RequestMapping("/api/students") |
| @GetMapping | 映射GET请求 | @GetMapping("/{id}") |
| @PostMapping | 映射POST请求 | @PostMapping |
| @PutMapping | 映射PUT请求 | @PutMapping("/{id}") |
| @DeleteMapping | 映射DELETE请求 | @DeleteMapping("/{id}") |
| @PathVariable | 获取路径变量 | @PathVariable Long id |
| @RequestParam | 获取请求参数 | @RequestParam String name |
| @RequestBody | 获取请求体 | @RequestBody StudentDTO dto |
| @Valid | 启用数据验证 | @Valid @RequestBody StudentDTO dto |

### 🎯 实战任务1：创建StudentController类

```java
// src/main/java/com/example/studentmanagement/controller/StudentController.java
package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.ApiResponse;
import com.example.studentmanagement.dto.PageResponse;
import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * 学生管理控制器
 * 
 * 提供学生相关的RESTful API接口
 */
@RestController
@RequestMapping("/api/students")
@Validated
@CrossOrigin(origins = "*") // 允许跨域请求
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 创建学生
     * 
     * @param studentDTO 学生信息
     * @return 创建结果
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StudentDTO>> createStudent(
            @Valid @RequestBody StudentDTO studentDTO) {
        
        logger.info("创建学生请求: {}", studentDTO.getName());
        
        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        
        ApiResponse<StudentDTO> response = ApiResponse.success(
            createdStudent, 
            "学生创建成功"
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 根据ID获取学生信息
     * 
     * @param id 学生ID
     * @return 学生信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDTO>> getStudentById(
            @PathVariable @Min(value = 1, message = "学生ID必须大于0") Long id) {
        
        logger.debug("获取学生信息，ID: {}", id);
        
        StudentDTO student = studentService.getStudentById(id);
        
        ApiResponse<StudentDTO> response = ApiResponse.success(
            student, 
            "获取学生信息成功"
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 根据学号获取学生信息
     * 
     * @param studentNumber 学号
     * @return 学生信息
     */
    @GetMapping("/number/{studentNumber}")
    public ResponseEntity<ApiResponse<StudentDTO>> getStudentByNumber(
            @PathVariable String studentNumber) {
        
        logger.debug("根据学号获取学生信息: {}", studentNumber);
        
        StudentDTO student = studentService.getStudentByNumber(studentNumber);
        
        ApiResponse<StudentDTO> response = ApiResponse.success(
            student, 
            "获取学生信息成功"
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 获取学生列表（分页）
     * 
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @param sort 排序字段
     * @param direction 排序方向
     * @return 分页结果
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StudentDTO>>> getAllStudents(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "页码不能小于0") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "每页大小不能小于1") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        
        logger.debug("获取学生列表 - 页码: {}, 大小: {}, 排序: {} {}", page, size, sort, direction);
        
        // 创建排序对象
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        Sort sortObj = Sort.by(sortDirection, sort);
        
        // 创建分页对象
        Pageable pageable = PageRequest.of(page, size, sortObj);
        
        // 获取分页数据
        Page<StudentDTO> studentPage = studentService.getAllStudents(pageable);
        PageResponse<StudentDTO> pageResponse = PageResponse.of(studentPage);
        
        ApiResponse<PageResponse<StudentDTO>> response = ApiResponse.success(
            pageResponse, 
            "获取学生列表成功"
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 搜索学生
     * 
     * @param name 姓名关键字
     * @param major 专业
     * @param gender 性别
     * @param page 页码
     * @param size 每页大小
     * @param sort 排序字段
     * @param direction 排序方向
     * @return 搜索结果
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<StudentDTO>>> searchStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) String gender,
            @RequestParam(defaultValue = "0") @Min(value = 0) int page,
            @RequestParam(defaultValue = "10") @Min(value = 1) int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        
        logger.debug("搜索学生 - 姓名: {}, 专业: {}, 性别: {}", name, major, gender);
        
        // 创建排序和分页对象
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        Sort sortObj = Sort.by(sortDirection, sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        
        // 执行搜索
        Page<StudentDTO> studentPage = studentService.searchStudents(name, major, gender, pageable);
        PageResponse<StudentDTO> pageResponse = PageResponse.of(studentPage);
        
        ApiResponse<PageResponse<StudentDTO>> response = ApiResponse.success(
            pageResponse, 
            "搜索学生成功"
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 更新学生信息
     * 
     * @param id 学生ID
     * @param studentDTO 更新的学生信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDTO>> updateStudent(
            @PathVariable @Min(value = 1, message = "学生ID必须大于0") Long id,
            @Valid @RequestBody StudentDTO studentDTO) {
        
        logger.info("更新学生信息，ID: {}", id);
        
        StudentDTO updatedStudent = studentService.updateStudent(id, studentDTO);
        
        ApiResponse<StudentDTO> response = ApiResponse.success(
            updatedStudent, 
            "学生信息更新成功"
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 删除学生
     * 
     * @param id 学生ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(
            @PathVariable @Min(value = 1, message = "学生ID必须大于0") Long id) {
        
        logger.info("删除学生，ID: {}", id);
        
        studentService.deleteStudent(id);
        
        ApiResponse<Void> response = ApiResponse.success("学生删除成功");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 批量删除学生
     * 
     * @param ids 学生ID列表
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<ApiResponse<Void>> deleteStudents(
            @RequestBody @NotEmpty(message = "学生ID列表不能为空") List<Long> ids) {
        
        logger.info("批量删除学生，数量: {}", ids.size());
        
        studentService.deleteStudents(ids);
        
        ApiResponse<Void> response = ApiResponse.success(
            String.format("成功删除%d个学生", ids.size())
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 检查学号是否存在
     * 
     * @param studentNumber 学号
     * @return 检查结果
     */
    @GetMapping("/exists/{studentNumber}")
    public ResponseEntity<ApiResponse<Boolean>> checkStudentNumberExists(
            @PathVariable String studentNumber) {
        
        logger.debug("检查学号是否存在: {}", studentNumber);
        
        boolean exists = studentService.existsByStudentNumber(studentNumber);
        
        ApiResponse<Boolean> response = ApiResponse.success(
            exists, 
            exists ? "学号已存在" : "学号可用"
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 获取学生统计信息
     * 
     * @return 统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStudentStatistics() {
        
        logger.debug("获取学生统计信息");
        
        Map<String, Object> statistics = studentService.getStudentStatistics();
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(
            statistics, 
            "获取统计信息成功"
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 获取专业分布统计
     * 
     * @return 专业统计
     */
    @GetMapping("/statistics/major")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMajorStatistics() {
        
        logger.debug("获取专业分布统计");
        
        List<Map<String, Object>> majorStats = studentService.getMajorStatistics();
        
        ApiResponse<List<Map<String, Object>>> response = ApiResponse.success(
            majorStats, 
            "获取专业统计成功"
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 获取年龄分布统计
     * 
     * @return 年龄分布统计
     */
    @GetMapping("/statistics/age")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAgeDistribution() {
        
        logger.debug("获取年龄分布统计");
        
        List<Map<String, Object>> ageStats = studentService.getAgeDistribution();
        
        ApiResponse<List<Map<String, Object>>> response = ApiResponse.success(
            ageStats, 
            "获取年龄统计成功"
        );
        
        return ResponseEntity.ok(response);
    }
}
```

### 💡 控制器设计要点

1. **统一响应格式**：所有接口都返回ApiResponse格式
2. **参数验证**：使用@Valid和验证注解
3. **日志记录**：记录关键操作和调试信息
4. **异常处理**：让全局异常处理器处理异常
5. **HTTP状态码**：根据操作结果返回合适的状态码

---

## 4.3 全局异常处理

### 📚 理论知识

#### 为什么需要全局异常处理？
1. **统一处理**：避免在每个Controller中重复处理异常
2. **一致性**：保证所有异常响应格式一致
3. **可维护性**：集中管理异常处理逻辑
4. **用户体验**：提供友好的错误信息

#### Spring异常处理机制
- **@ControllerAdvice**：全局异常处理器
- **@ExceptionHandler**：处理特定异常
- **@ResponseStatus**：指定HTTP状态码

### 🎯 实战任务2：创建GlobalExceptionHandler

```java
// src/main/java/com/example/studentmanagement/exception/GlobalExceptionHandler.java
package com.example.studentmanagement.exception;

import com.example.studentmanagement.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理器
 * 
 * 统一处理应用中的各种异常，返回标准的错误响应格式
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理参数验证异常（@RequestBody验证失败）
     * 
     * @param ex 方法参数验证异常
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            MethodArgumentNotValidException ex) {
        
        logger.warn("参数验证失败: {}", ex.getMessage());
        
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        
        String errorMessage = "参数验证失败: " + String.join(", ", errors);
        ApiResponse<Object> response = ApiResponse.validationError(errorMessage);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 处理绑定异常（@RequestParam验证失败）
     * 
     * @param ex 绑定异常
     * @return 错误响应
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Object>> handleBindException(BindException ex) {
        
        logger.warn("参数绑定失败: {}", ex.getMessage());
        
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        
        String errorMessage = "参数绑定失败: " + String.join(", ", errors);
        ApiResponse<Object> response = ApiResponse.validationError(errorMessage);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 处理约束违反异常（@PathVariable、@RequestParam验证失败）
     * 
     * @param ex 约束违反异常
     * @return 错误响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        
        logger.warn("约束验证失败: {}", ex.getMessage());
        
        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
        }
        
        String errorMessage = "参数验证失败: " + String.join(", ", errors);
        ApiResponse<Object> response = ApiResponse.validationError(errorMessage);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 处理参数类型不匹配异常
     * 
     * @param ex 参数类型不匹配异常
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        
        logger.warn("参数类型不匹配: {}", ex.getMessage());
        
        String errorMessage = String.format(
            "参数 '%s' 的值 '%s' 类型不正确，期望类型: %s",
            ex.getName(),
            ex.getValue(),
            ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "未知"
        );
        
        ApiResponse<Object> response = ApiResponse.validationError(errorMessage);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 处理HTTP消息不可读异常（JSON格式错误）
     * 
     * @param ex HTTP消息不可读异常
     * @return 错误响应
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        
        logger.warn("HTTP消息不可读: {}", ex.getMessage());
        
        String errorMessage = "请求体格式错误，请检查JSON格式是否正确";
        ApiResponse<Object> response = ApiResponse.validationError(errorMessage);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 处理404异常（接口不存在）
     * 
     * @param ex 404异常
     * @return 错误响应
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoHandlerFoundException(
            NoHandlerFoundException ex) {
        
        logger.warn("接口不存在: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        
        String errorMessage = String.format(
            "接口不存在: %s %s",
            ex.getHttpMethod(),
            ex.getRequestURL()
        );
        
        ApiResponse<Object> response = ApiResponse.notFound(errorMessage);
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 处理业务异常（IllegalArgumentException）
     * 
     * @param ex 非法参数异常
     * @return 错误响应
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        
        logger.warn("业务异常: {}", ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.validationError(ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 处理运行时异常
     * 
     * @param ex 运行时异常
     * @return 错误响应
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        
        logger.error("运行时异常: ", ex);
        
        ApiResponse<Object> response = ApiResponse.error("系统繁忙，请稍后重试");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 处理所有其他异常
     * 
     * @param ex 异常
     * @return 错误响应
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        
        logger.error("未知异常: ", ex);
        
        ApiResponse<Object> response = ApiResponse.error("系统内部错误");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
```

### 🎯 实战任务3：创建自定义业务异常

```java
// src/main/java/com/example/studentmanagement/exception/BusinessException.java
package com.example.studentmanagement.exception;

/**
 * 业务异常类
 * 
 * 用于表示业务逻辑相关的异常
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 400;
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
```

```java
// src/main/java/com/example/studentmanagement/exception/ResourceNotFoundException.java
package com.example.studentmanagement.exception;

/**
 * 资源不存在异常
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String message) {
        super(404, message);
    }

    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(404, String.format("%s不存在，ID: %s", resourceName, resourceId));
    }
}
```

在GlobalExceptionHandler中添加自定义异常处理：

```java
/**
 * 处理业务异常
 * 
 * @param ex 业务异常
 * @return 错误响应
 */
@ExceptionHandler(BusinessException.class)
public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex) {
    
    logger.warn("业务异常: {}", ex.getMessage());
    
    ApiResponse<Object> response = ApiResponse.error(ex.getCode(), ex.getMessage());
    
    HttpStatus status = ex.getCode() == 404 ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
    
    return ResponseEntity.status(status).body(response);
}

/**
 * 处理资源不存在异常
 * 
 * @param ex 资源不存在异常
 * @return 错误响应
 */
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
        ResourceNotFoundException ex) {
    
    logger.warn("资源不存在: {}", ex.getMessage());
    
    ApiResponse<Object> response = ApiResponse.notFound(ex.getMessage());
    
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
}
```

---

## ✅ 功能验证

### 🎯 实战任务4：创建Controller测试类

```java
// src/test/java/com/example/studentmanagement/controller/StudentControllerTest.java
package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 学生控制器测试类
 */
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentDTO testStudentDTO;

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
        testStudentDTO.setId(1L);
    }

    @Test
    void testCreateStudent() throws Exception {
        // 模拟Service行为
        when(studentService.createStudent(any(StudentDTO.class))).thenReturn(testStudentDTO);

        // 执行POST请求
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStudentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("学生创建成功"))
                .andExpect(jsonPath("$.data.name").value("张三"))
                .andExpect(jsonPath("$.data.studentNumber").value("20210001"));
    }

    @Test
    void testCreateStudentWithInvalidData() throws Exception {
        // 创建无效数据（姓名为空）
        StudentDTO invalidStudent = new StudentDTO();
        invalidStudent.setStudentNumber("20210001");
        invalidStudent.setAge(20);

        // 执行POST请求
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidStudent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value(containsString("参数验证失败")));
    }

    @Test
    void testGetStudentById() throws Exception {
        // 模拟Service行为
        when(studentService.getStudentById(1L)).thenReturn(testStudentDTO);

        // 执行GET请求
        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("张三"))
                .andExpect(jsonPath("$.data.studentNumber").value("20210001"));
    }

    @Test
    void testGetAllStudents() throws Exception {
        // 准备分页数据
        Page<StudentDTO> studentPage = new PageImpl<>(
            Arrays.asList(testStudentDTO),
            PageRequest.of(0, 10),
            1
        );

        // 模拟Service行为
        when(studentService.getAllStudents(any())).thenReturn(studentPage);

        // 执行GET请求
        mockMvc.perform(get("/api/students")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].name").value("张三"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        // 模拟Service行为
        when(studentService.updateStudent(eq(1L), any(StudentDTO.class))).thenReturn(testStudentDTO);

        // 执行PUT请求
        mockMvc.perform(put("/api/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStudentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("学生信息更新成功"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        // 执行DELETE请求
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("学生删除成功"));
    }
}
```

---

## 💡 知识扩展

### API文档生成

可以使用Swagger/OpenAPI生成API文档：

```xml
<!-- 在pom.xml中添加依赖 -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.6.14</version>
</dependency>
```

### 接口版本控制

```java
// 通过URL路径版本控制
@RequestMapping("/api/v1/students")

// 通过请求头版本控制
@RequestMapping(value = "/api/students", headers = "API-Version=1")

// 通过参数版本控制
@RequestMapping(value = "/api/students", params = "version=1")
```

### 接口限流和缓存

```java
// 使用Redis实现接口限流
@RateLimiter(key = "student_api", rate = 100, duration = 60)

// 使用缓存提高查询性能
@Cacheable(value = "students", key = "#id")
public StudentDTO getStudentById(Long id) {
    // 查询逻辑
}
```

---

## 📝 本章小结

✅ **已完成**：
- [x] 学习RESTful API设计原则
- [x] 创建完整的StudentController
- [x] 实现CRUD操作的API接口
- [x] 实现搜索和统计接口
- [x] 创建全局异常处理器
- [x] 定义自定义业务异常
- [x] 编写Controller测试用例

🎯 **下一章预告**：
在第五章中，我们将配置应用程序的各种参数，创建数据库初始化脚本，完善系统配置。

---

## 🏠 课后练习

1. 为API添加接口文档注解（Swagger）
2. 实现API版本控制机制
3. 添加接口访问日志记录
4. 实现API接口的权限控制