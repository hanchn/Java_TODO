# 第七章：测试与调试

> **学习目标**：掌握Spring Boot应用的测试策略、单元测试、集成测试、调试技巧和性能优化方法

---

## 📋 本章概览

### 🎯 核心内容
- **测试基础理论**：测试金字塔、测试分类、测试策略
- **单元测试**：JUnit 5、Mockito、测试驱动开发
- **集成测试**：Spring Boot Test、TestContainers
- **API测试**：MockMvc、WebTestClient、Postman
- **调试技巧**：IDE调试、日志调试、远程调试
- **性能测试**：JMeter、性能监控、性能优化

### 📚 技术栈
- **测试框架**：JUnit 5、Mockito、AssertJ
- **Spring测试**：@SpringBootTest、@WebMvcTest、@DataJpaTest
- **容器化测试**：TestContainers
- **性能测试**：JMeter、Spring Boot Actuator
- **调试工具**：IntelliJ IDEA、Logback

---

## 7.1 测试基础理论

### 📚 测试金字塔

```
        /\     E2E Tests (端到端测试)
       /  \    - 少量，覆盖关键业务流程
      /____\   - 运行缓慢，维护成本高
     /      \
    / Integration \  Integration Tests (集成测试)
   /    Tests     \  - 中等数量，测试组件间交互
  /______________\  - 验证系统集成点
 /                \
/   Unit Tests     \  Unit Tests (单元测试)
\__________________/  - 大量，快速执行
                      - 测试单个组件或方法
```

### 🎯 测试分类

#### 按测试范围分类
```java
/**
 * 单元测试 - 测试单个类或方法
 * 特点：快速、独立、可重复
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    
    @InjectMocks
    private StudentServiceImpl studentService;
    
    @Test
    @DisplayName("根据ID查找学生 - 成功")
    void findById_Success() {
        // Given
        Long studentId = 1L;
        Student student = createTestStudent();
        when(studentRepository.findById(studentId))
            .thenReturn(Optional.of(student));
        
        // When
        StudentDTO result = studentService.findById(studentId);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(studentId);
        assertThat(result.getName()).isEqualTo(student.getName());
    }
}

/**
 * 集成测试 - 测试多个组件协作
 * 特点：真实环境、数据库交互
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class StudentIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    
    @Autowired
    private StudentService studentService;
    
    @Test
    @DisplayName("完整的学生CRUD操作")
    void studentCrudOperations() {
        // 创建学生
        StudentDTO newStudent = createStudentDTO();
        StudentDTO created = studentService.create(newStudent);
        assertThat(created.getId()).isNotNull();
        
        // 查询学生
        StudentDTO found = studentService.findById(created.getId());
        assertThat(found.getName()).isEqualTo(newStudent.getName());
        
        // 更新学生
        found.setName("Updated Name");
        StudentDTO updated = studentService.update(found.getId(), found);
        assertThat(updated.getName()).isEqualTo("Updated Name");
        
        // 删除学生
        studentService.delete(created.getId());
        assertThatThrownBy(() -> studentService.findById(created.getId()))
            .isInstanceOf(ResourceNotFoundException.class);
    }
}
```

#### 按测试目的分类
```java
/**
 * 功能测试 - 验证业务功能
 */
@Test
@DisplayName("学生年龄计算功能测试")
void calculateAge_ShouldReturnCorrectAge() {
    // Given
    LocalDate birthDate = LocalDate.of(2000, 1, 1);
    Student student = Student.builder()
        .birthDate(birthDate)
        .build();
    
    // When
    int age = student.calculateAge();
    
    // Then
    int expectedAge = Period.between(birthDate, LocalDate.now()).getYears();
    assertThat(age).isEqualTo(expectedAge);
}

/**
 * 性能测试 - 验证性能指标
 */
@Test
@DisplayName("批量查询性能测试")
@Timeout(value = 2, unit = TimeUnit.SECONDS)
void batchQuery_ShouldCompleteWithinTimeLimit() {
    // Given
    List<Long> studentIds = IntStream.rangeClosed(1, 1000)
        .mapToLong(i -> (long) i)
        .boxed()
        .collect(Collectors.toList());
    
    // When & Then
    assertDoesNotThrow(() -> {
        List<StudentDTO> students = studentService.findByIds(studentIds);
        assertThat(students).isNotEmpty();
    });
}

/**
 * 安全测试 - 验证安全控制
 */
@Test
@DisplayName("未授权访问应该被拒绝")
void unauthorizedAccess_ShouldBeDenied() {
    // Given
    StudentDTO studentDTO = createStudentDTO();
    
    // When & Then
    assertThatThrownBy(() -> {
        // 模拟未授权用户访问
        SecurityContextHolder.clearContext();
        studentService.create(studentDTO);
    }).isInstanceOf(AccessDeniedException.class);
}
```

---

## 7.2 单元测试实战

### 🎯 实战任务1：完善StudentService单元测试

```java
// src/test/java/com/example/studentmanagement/service/StudentServiceTest.java

package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.exception.BusinessException;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * StudentService 单元测试
 * 
 * 测试策略：
 * 1. 正常流程测试
 * 2. 异常情况测试
 * 3. 边界条件测试
 * 4. 业务规则验证
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService 单元测试")
class StudentServiceTest {
    
    @Mock
    private StudentRepository studentRepository;
    
    @InjectMocks
    private StudentServiceImpl studentService;
    
    private Student testStudent;
    private StudentDTO testStudentDTO;
    
    @BeforeEach
    void setUp() {
        testStudent = createTestStudent();
        testStudentDTO = createTestStudentDTO();
    }
    
    @Nested
    @DisplayName("查询操作测试")
    class FindOperationsTest {
        
        @Test
        @DisplayName("根据ID查找学生 - 成功")
        void findById_WhenStudentExists_ShouldReturnStudentDTO() {
            // Given
            Long studentId = 1L;
            when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(testStudent));
            
            // When
            StudentDTO result = studentService.findById(studentId);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(studentId);
            assertThat(result.getName()).isEqualTo(testStudent.getName());
            assertThat(result.getEmail()).isEqualTo(testStudent.getEmail());
            
            verify(studentRepository).findById(studentId);
        }
        
        @Test
        @DisplayName("根据ID查找学生 - 学生不存在")
        void findById_WhenStudentNotExists_ShouldThrowException() {
            // Given
            Long studentId = 999L;
            when(studentRepository.findById(studentId))
                .thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> studentService.findById(studentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student not found with id: " + studentId);
            
            verify(studentRepository).findById(studentId);
        }
        
        @Test
        @DisplayName("分页查询学生列表")
        void findAll_WithPageable_ShouldReturnPagedResult() {
            // Given
            Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
            List<Student> students = createTestStudents(5);
            Page<Student> studentPage = new PageImpl<>(students, pageable, students.size());
            
            when(studentRepository.findAll(pageable))
                .thenReturn(studentPage);
            
            // When
            Page<StudentDTO> result = studentService.findAll(pageable);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(5);
            assertThat(result.getTotalElements()).isEqualTo(5);
            assertThat(result.getNumber()).isEqualTo(0);
            assertThat(result.getSize()).isEqualTo(10);
            
            verify(studentRepository).findAll(pageable);
        }
        
        @Test
        @DisplayName("根据专业查询学生")
        void findByMajor_ShouldReturnMatchingStudents() {
            // Given
            String major = "Computer Science";
            List<Student> students = createTestStudents(3);
            students.forEach(s -> s.setMajor(major));
            
            when(studentRepository.findByMajorContainingIgnoreCase(major))
                .thenReturn(students);
            
            // When
            List<StudentDTO> result = studentService.findByMajor(major);
            
            // Then
            assertThat(result).hasSize(3);
            assertThat(result).allMatch(dto -> dto.getMajor().equals(major));
            
            verify(studentRepository).findByMajorContainingIgnoreCase(major);
        }
    }
    
    @Nested
    @DisplayName("创建操作测试")
    class CreateOperationsTest {
        
        @Test
        @DisplayName("创建学生 - 成功")
        void create_WithValidData_ShouldReturnCreatedStudent() {
            // Given
            when(studentRepository.existsByEmail(testStudentDTO.getEmail()))
                .thenReturn(false);
            when(studentRepository.save(any(Student.class)))
                .thenReturn(testStudent);
            
            // When
            StudentDTO result = studentService.create(testStudentDTO);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo(testStudentDTO.getName());
            assertThat(result.getEmail()).isEqualTo(testStudentDTO.getEmail());
            
            verify(studentRepository).existsByEmail(testStudentDTO.getEmail());
            verify(studentRepository).save(any(Student.class));
        }
        
        @Test
        @DisplayName("创建学生 - 邮箱已存在")
        void create_WithDuplicateEmail_ShouldThrowException() {
            // Given
            when(studentRepository.existsByEmail(testStudentDTO.getEmail()))
                .thenReturn(true);
            
            // When & Then
            assertThatThrownBy(() -> studentService.create(testStudentDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email already exists");
            
            verify(studentRepository).existsByEmail(testStudentDTO.getEmail());
            verify(studentRepository, never()).save(any(Student.class));
        }
        
        @Test
        @DisplayName("创建学生 - 无效数据")
        void create_WithInvalidData_ShouldThrowException() {
            // Given
            StudentDTO invalidStudent = StudentDTO.builder()
                .name("") // 空名称
                .email("invalid-email") // 无效邮箱
                .build();
            
            // When & Then
            assertThatThrownBy(() -> studentService.create(invalidStudent))
                .isInstanceOf(IllegalArgumentException.class);
            
            verify(studentRepository, never()).save(any(Student.class));
        }
    }
    
    @Nested
    @DisplayName("更新操作测试")
    class UpdateOperationsTest {
        
        @Test
        @DisplayName("更新学生 - 成功")
        void update_WithValidData_ShouldReturnUpdatedStudent() {
            // Given
            Long studentId = 1L;
            StudentDTO updateData = testStudentDTO.toBuilder()
                .name("Updated Name")
                .major("Updated Major")
                .build();
            
            Student existingStudent = testStudent.toBuilder().build();
            Student updatedStudent = existingStudent.toBuilder()
                .name(updateData.getName())
                .major(updateData.getMajor())
                .build();
            
            when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(existingStudent));
            when(studentRepository.save(any(Student.class)))
                .thenReturn(updatedStudent);
            
            // When
            StudentDTO result = studentService.update(studentId, updateData);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo(updateData.getName());
            assertThat(result.getMajor()).isEqualTo(updateData.getMajor());
            
            verify(studentRepository).findById(studentId);
            verify(studentRepository).save(any(Student.class));
        }
        
        @Test
        @DisplayName("更新学生 - 学生不存在")
        void update_WhenStudentNotExists_ShouldThrowException() {
            // Given
            Long studentId = 999L;
            when(studentRepository.findById(studentId))
                .thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> studentService.update(studentId, testStudentDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student not found with id: " + studentId);
            
            verify(studentRepository).findById(studentId);
            verify(studentRepository, never()).save(any(Student.class));
        }
        
        @Test
        @DisplayName("更新学生邮箱 - 邮箱已被其他学生使用")
        void update_WithDuplicateEmail_ShouldThrowException() {
            // Given
            Long studentId = 1L;
            String newEmail = "other@example.com";
            StudentDTO updateData = testStudentDTO.toBuilder()
                .email(newEmail)
                .build();
            
            when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(testStudent));
            when(studentRepository.existsByEmailAndIdNot(newEmail, studentId))
                .thenReturn(true);
            
            // When & Then
            assertThatThrownBy(() -> studentService.update(studentId, updateData))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email already exists");
            
            verify(studentRepository).findById(studentId);
            verify(studentRepository).existsByEmailAndIdNot(newEmail, studentId);
            verify(studentRepository, never()).save(any(Student.class));
        }
    }
    
    @Nested
    @DisplayName("删除操作测试")
    class DeleteOperationsTest {
        
        @Test
        @DisplayName("删除学生 - 成功")
        void delete_WhenStudentExists_ShouldDeleteSuccessfully() {
            // Given
            Long studentId = 1L;
            when(studentRepository.existsById(studentId))
                .thenReturn(true);
            doNothing().when(studentRepository).deleteById(studentId);
            
            // When
            assertDoesNotThrow(() -> studentService.delete(studentId));
            
            // Then
            verify(studentRepository).existsById(studentId);
            verify(studentRepository).deleteById(studentId);
        }
        
        @Test
        @DisplayName("删除学生 - 学生不存在")
        void delete_WhenStudentNotExists_ShouldThrowException() {
            // Given
            Long studentId = 999L;
            when(studentRepository.existsById(studentId))
                .thenReturn(false);
            
            // When & Then
            assertThatThrownBy(() -> studentService.delete(studentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student not found with id: " + studentId);
            
            verify(studentRepository).existsById(studentId);
            verify(studentRepository, never()).deleteById(any());
        }
        
        @Test
        @DisplayName("批量删除学生")
        void deleteAll_WithValidIds_ShouldDeleteAllStudents() {
            // Given
            List<Long> studentIds = Arrays.asList(1L, 2L, 3L);
            when(studentRepository.findAllById(studentIds))
                .thenReturn(createTestStudents(3));
            doNothing().when(studentRepository).deleteAllById(studentIds);
            
            // When
            assertDoesNotThrow(() -> studentService.deleteAll(studentIds));
            
            // Then
            verify(studentRepository).findAllById(studentIds);
            verify(studentRepository).deleteAllById(studentIds);
        }
    }
    
    @Nested
    @DisplayName("统计操作测试")
    class StatisticsOperationsTest {
        
        @Test
        @DisplayName("获取学生统计信息")
        void getStatistics_ShouldReturnCorrectStatistics() {
            // Given
            when(studentRepository.count()).thenReturn(100L);
            when(studentRepository.countDistinctMajor()).thenReturn(10L);
            when(studentRepository.findAverageAge()).thenReturn(22.5);
            when(studentRepository.countByCreatedDateAfter(any(LocalDate.class)))
                .thenReturn(5L);
            
            // When
            var statistics = studentService.getStatistics();
            
            // Then
            assertThat(statistics.getTotalStudents()).isEqualTo(100L);
            assertThat(statistics.getTotalMajors()).isEqualTo(10L);
            assertThat(statistics.getAverageAge()).isEqualTo(22.5);
            assertThat(statistics.getTodayNew()).isEqualTo(5L);
            
            verify(studentRepository).count();
            verify(studentRepository).countDistinctMajor();
            verify(studentRepository).findAverageAge();
            verify(studentRepository).countByCreatedDateAfter(any(LocalDate.class));
        }
        
        @Test
        @DisplayName("获取专业分布统计")
        void getMajorStatistics_ShouldReturnMajorDistribution() {
            // Given
            List<Object[]> majorStats = Arrays.asList(
                new Object[]{"Computer Science", 30L},
                new Object[]{"Mathematics", 20L},
                new Object[]{"Physics", 15L}
            );
            when(studentRepository.findMajorStatistics())
                .thenReturn(majorStats);
            
            // When
            var result = studentService.getMajorStatistics();
            
            // Then
            assertThat(result).hasSize(3);
            assertThat(result.get(0).getMajor()).isEqualTo("Computer Science");
            assertThat(result.get(0).getCount()).isEqualTo(30L);
            
            verify(studentRepository).findMajorStatistics();
        }
    }
    
    @Nested
    @DisplayName("搜索操作测试")
    class SearchOperationsTest {
        
        @Test
        @DisplayName("按关键词搜索学生")
        void search_WithKeyword_ShouldReturnMatchingStudents() {
            // Given
            String keyword = "John";
            Pageable pageable = PageRequest.of(0, 10);
            List<Student> students = createTestStudents(2);
            Page<Student> studentPage = new PageImpl<>(students, pageable, students.size());
            
            when(studentRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                eq(keyword), eq(keyword), eq(pageable)))
                .thenReturn(studentPage);
            
            // When
            Page<StudentDTO> result = studentService.search(keyword, pageable);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            
            verify(studentRepository).findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                keyword, keyword, pageable);
        }
        
        @Test
        @DisplayName("搜索无结果")
        void search_WithNoMatches_ShouldReturnEmptyPage() {
            // Given
            String keyword = "NonExistent";
            Pageable pageable = PageRequest.of(0, 10);
            Page<Student> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
            
            when(studentRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                eq(keyword), eq(keyword), eq(pageable)))
                .thenReturn(emptyPage);
            
            // When
            Page<StudentDTO> result = studentService.search(keyword, pageable);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isEqualTo(0);
        }
    }
    
    // 测试数据创建方法
    private Student createTestStudent() {
        return Student.builder()
            .id(1L)
            .name("John Doe")
            .email("john.doe@example.com")
            .age(20)
            .major("Computer Science")
            .phone("1234567890")
            .address("123 Main St")
            .birthDate(LocalDate.of(2003, 1, 1))
            .enrollmentDate(LocalDate.now())
            .build();
    }
    
    private StudentDTO createTestStudentDTO() {
        return StudentDTO.builder()
            .name("John Doe")
            .email("john.doe@example.com")
            .age(20)
            .major("Computer Science")
            .phone("1234567890")
            .address("123 Main St")
            .birthDate(LocalDate.of(2003, 1, 1))
            .build();
    }
    
    private List<Student> createTestStudents(int count) {
        return IntStream.rangeClosed(1, count)
            .mapToObj(i -> Student.builder()
                .id((long) i)
                .name("Student " + i)
                .email("student" + i + "@example.com")
                .age(20 + i)
                .major("Major " + i)
                .phone("123456789" + i)
                .address("Address " + i)
                .birthDate(LocalDate.of(2003, 1, i))
                .enrollmentDate(LocalDate.now())
                .build())
            .collect(Collectors.toList());
    }
}
```

### 🎯 实战任务2：Repository层测试

```java
// src/test/java/com/example/studentmanagement/repository/StudentRepositoryTest.java

package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Student;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * StudentRepository 数据访问层测试
 * 
 * 使用 @DataJpaTest 注解，只加载JPA相关配置
 * 使用内存数据库H2进行测试
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("StudentRepository 数据访问层测试")
class StudentRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private StudentRepository studentRepository;
    
    private Student testStudent1;
    private Student testStudent2;
    private Student testStudent3;
    
    @BeforeEach
    void setUp() {
        // 创建测试数据
        testStudent1 = Student.builder()
            .name("Alice Johnson")
            .email("alice@example.com")
            .age(20)
            .major("Computer Science")
            .phone("1234567890")
            .address("123 Main St")
            .birthDate(LocalDate.of(2003, 5, 15))
            .enrollmentDate(LocalDate.now())
            .build();
        
        testStudent2 = Student.builder()
            .name("Bob Smith")
            .email("bob@example.com")
            .age(22)
            .major("Mathematics")
            .phone("0987654321")
            .address("456 Oak Ave")
            .birthDate(LocalDate.of(2001, 8, 20))
            .enrollmentDate(LocalDate.now())
            .build();
        
        testStudent3 = Student.builder()
            .name("Charlie Brown")
            .email("charlie@example.com")
            .age(21)
            .major("Computer Science")
            .phone("5555555555")
            .address("789 Pine Rd")
            .birthDate(LocalDate.of(2002, 12, 10))
            .enrollmentDate(LocalDate.now())
            .build();
        
        // 持久化测试数据
        entityManager.persistAndFlush(testStudent1);
        entityManager.persistAndFlush(testStudent2);
        entityManager.persistAndFlush(testStudent3);
    }
    
    @Nested
    @DisplayName("基本CRUD操作测试")
    class BasicCrudTest {
        
        @Test
        @DisplayName("保存学生")
        void save_ShouldPersistStudent() {
            // Given
            Student newStudent = Student.builder()
                .name("David Wilson")
                .email("david@example.com")
                .age(19)
                .major("Physics")
                .phone("1111111111")
                .address("321 Elm St")
                .birthDate(LocalDate.of(2004, 3, 25))
                .enrollmentDate(LocalDate.now())
                .build();
            
            // When
            Student saved = studentRepository.save(newStudent);
            
            // Then
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getName()).isEqualTo("David Wilson");
            assertThat(saved.getEmail()).isEqualTo("david@example.com");
            
            // 验证数据库中确实保存了
            Student found = entityManager.find(Student.class, saved.getId());
            assertThat(found).isNotNull();
            assertThat(found.getName()).isEqualTo("David Wilson");
        }
        
        @Test
        @DisplayName("根据ID查找学生")
        void findById_ShouldReturnStudent() {
            // When
            Optional<Student> found = studentRepository.findById(testStudent1.getId());
            
            // Then
            assertThat(found).isPresent();
            assertThat(found.get().getName()).isEqualTo("Alice Johnson");
            assertThat(found.get().getEmail()).isEqualTo("alice@example.com");
        }
        
        @Test
        @DisplayName("查找所有学生")
        void findAll_ShouldReturnAllStudents() {
            // When
            List<Student> students = studentRepository.findAll();
            
            // Then
            assertThat(students).hasSize(3);
            assertThat(students)
                .extracting(Student::getName)
                .containsExactlyInAnyOrder("Alice Johnson", "Bob Smith", "Charlie Brown");
        }
        
        @Test
        @DisplayName("删除学生")
        void delete_ShouldRemoveStudent() {
            // Given
            Long studentId = testStudent1.getId();
            
            // When
            studentRepository.deleteById(studentId);
            entityManager.flush();
            
            // Then
            Optional<Student> found = studentRepository.findById(studentId);
            assertThat(found).isEmpty();
            
            // 验证其他学生仍然存在
            List<Student> remaining = studentRepository.findAll();
            assertThat(remaining).hasSize(2);
        }
    }
    
    @Nested
    @DisplayName("自定义查询方法测试")
    class CustomQueryTest {
        
        @Test
        @DisplayName("根据邮箱查找学生")
        void findByEmail_ShouldReturnStudent() {
            // When
            Optional<Student> found = studentRepository.findByEmail("alice@example.com");
            
            // Then
            assertThat(found).isPresent();
            assertThat(found.get().getName()).isEqualTo("Alice Johnson");
        }
        
        @Test
        @DisplayName("根据专业查找学生")
        void findByMajorContainingIgnoreCase_ShouldReturnMatchingStudents() {
            // When
            List<Student> csStudents = studentRepository
                .findByMajorContainingIgnoreCase("computer");
            
            // Then
            assertThat(csStudents).hasSize(2);
            assertThat(csStudents)
                .extracting(Student::getName)
                .containsExactlyInAnyOrder("Alice Johnson", "Charlie Brown");
        }
        
        @Test
        @DisplayName("根据年龄范围查找学生")
        void findByAgeBetween_ShouldReturnStudentsInRange() {
            // When
            List<Student> students = studentRepository.findByAgeBetween(20, 21);
            
            // Then
            assertThat(students).hasSize(2);
            assertThat(students)
                .extracting(Student::getAge)
                .containsExactlyInAnyOrder(20, 21);
        }
        
        @Test
        @DisplayName("根据姓名和邮箱搜索学生")
        void findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase_ShouldReturnMatches() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            
            // When
            Page<Student> result = studentRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    "alice", "alice", pageable);
            
            // Then
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getName()).isEqualTo("Alice Johnson");
        }
        
        @Test
        @DisplayName("检查邮箱是否存在")
        void existsByEmail_ShouldReturnTrueForExistingEmail() {
            // When
            boolean exists = studentRepository.existsByEmail("alice@example.com");
            boolean notExists = studentRepository.existsByEmail("nonexistent@example.com");
            
            // Then
            assertThat(exists).isTrue();
            assertThat(notExists).isFalse();
        }
        
        @Test
        @DisplayName("检查邮箱是否被其他学生使用")
        void existsByEmailAndIdNot_ShouldReturnCorrectResult() {
            // When
            boolean existsForOther = studentRepository
                .existsByEmailAndIdNot("alice@example.com", testStudent2.getId());
            boolean notExistsForSame = studentRepository
                .existsByEmailAndIdNot("alice@example.com", testStudent1.getId());
            
            // Then
            assertThat(existsForOther).isTrue();
            assertThat(notExistsForSame).isFalse();
        }
    }
    
    @Nested
    @DisplayName("统计查询测试")
    class StatisticsQueryTest {
        
        @Test
        @DisplayName("统计不同专业数量")
        void countDistinctMajor_ShouldReturnCorrectCount() {
            // When
            Long count = studentRepository.countDistinctMajor();
            
            // Then
            assertThat(count).isEqualTo(2L); // Computer Science 和 Mathematics
        }
        
        @Test
        @DisplayName("计算平均年龄")
        void findAverageAge_ShouldReturnCorrectAverage() {
            // When
            Double averageAge = studentRepository.findAverageAge();
            
            // Then
            assertThat(averageAge).isEqualTo(21.0); // (20 + 22 + 21) / 3
        }
        
        @Test
        @DisplayName("统计今日新增学生数")
        void countByCreatedDateAfter_ShouldReturnTodayCount() {
            // Given
            LocalDate yesterday = LocalDate.now().minusDays(1);
            
            // When
            Long count = studentRepository.countByCreatedDateAfter(yesterday);
            
            // Then
            assertThat(count).isEqualTo(3L); // 今天创建的3个学生
        }
        
        @Test
        @DisplayName("获取专业分布统计")
        void findMajorStatistics_ShouldReturnMajorDistribution() {
            // When
            List<Object[]> statistics = studentRepository.findMajorStatistics();
            
            // Then
            assertThat(statistics).hasSize(2);
            
            // 验证Computer Science专业有2个学生
            Object[] csStats = statistics.stream()
                .filter(stat -> "Computer Science".equals(stat[0]))
                .findFirst()
                .orElse(null);
            assertThat(csStats).isNotNull();
            assertThat(csStats[1]).isEqualTo(2L);
            
            // 验证Mathematics专业有1个学生
            Object[] mathStats = statistics.stream()
                .filter(stat -> "Mathematics".equals(stat[0]))
                .findFirst()
                .orElse(null);
            assertThat(mathStats).isNotNull();
            assertThat(mathStats[1]).isEqualTo(1L);
        }
    }
    
    @Nested
    @DisplayName("分页和排序测试")
    class PagingAndSortingTest {
        
        @Test
        @DisplayName("分页查询")
        void findAll_WithPageable_ShouldReturnPagedResult() {
            // Given
            Pageable pageable = PageRequest.of(0, 2);
            
            // When
            Page<Student> page = studentRepository.findAll(pageable);
            
            // Then
            assertThat(page.getContent()).hasSize(2);
            assertThat(page.getTotalElements()).isEqualTo(3);
            assertThat(page.getTotalPages()).isEqualTo(2);
            assertThat(page.isFirst()).isTrue();
            assertThat(page.hasNext()).isTrue();
        }
        
        @Test
        @DisplayName("按姓名排序查询")
        void findAll_WithSort_ShouldReturnSortedResult() {
            // Given
            Sort sort = Sort.by(Sort.Direction.ASC, "name");
            
            // When
            List<Student> students = studentRepository.findAll(sort);
            
            // Then
            assertThat(students)
                .extracting(Student::getName)
                .containsExactly("Alice Johnson", "Bob Smith", "Charlie Brown");
        }
        
        @Test
        @DisplayName("按年龄降序排序")
        void findAll_WithDescendingAgeSort_ShouldReturnCorrectOrder() {
            // Given
            Sort sort = Sort.by(Sort.Direction.DESC, "age");
            
            // When
            List<Student> students = studentRepository.findAll(sort);
            
            // Then
            assertThat(students)
                .extracting(Student::getAge)
                .containsExactly(22, 21, 20);
        }
    }
    
    @Nested
    @DisplayName("数据完整性测试")
    class DataIntegrityTest {
        
        @Test
        @DisplayName("邮箱唯一性约束测试")
        void save_WithDuplicateEmail_ShouldThrowException() {
            // Given
            Student duplicateEmailStudent = Student.builder()
                .name("Duplicate Email Student")
                .email("alice@example.com") // 重复邮箱
                .age(25)
                .major("Physics")
                .phone("9999999999")
                .address("999 Test St")
                .birthDate(LocalDate.of(1998, 1, 1))
                .enrollmentDate(LocalDate.now())
                .build();
            
            // When & Then
            assertThatThrownBy(() -> {
                studentRepository.save(duplicateEmailStudent);
                entityManager.flush();
            }).isInstanceOf(Exception.class);
        }
        
        @Test
        @DisplayName("必填字段验证")
        void save_WithNullRequiredFields_ShouldThrowException() {
            // Given
            Student invalidStudent = Student.builder()
                .name(null) // 必填字段为null
                .email("test@example.com")
                .build();
            
            // When & Then
            assertThatThrownBy(() -> {
                studentRepository.save(invalidStudent);
                entityManager.flush();
            }).isInstanceOf(Exception.class);
        }
    }
}
```

---

## 7.3 集成测试实战

### 🎯 实战任务3：Web层集成测试

```java
// src/test/java/com/example/studentmanagement/controller/StudentControllerIntegrationTest.java

package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * StudentController 集成测试
 * 
 * 使用 @SpringBootTest 加载完整的应用上下文
 * 使用 @AutoConfigureMockMvc 自动配置MockMvc
 * 使用 @Transactional 确保测试数据隔离
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("StudentController 集成测试")
class StudentControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private StudentRepository studentRepository;
    
    private Student testStudent;
    private StudentDTO testStudentDTO;
    
    @BeforeEach
    void setUp() {
        // 清理数据
        studentRepository.deleteAll();
        
        // 创建测试数据
        testStudent = Student.builder()
            .name("John Doe")
            .email("john.doe@example.com")
            .age(20)
            .major("Computer Science")
            .phone("1234567890")
            .address("123 Main St")
            .birthDate(LocalDate.of(2003, 1, 1))
            .enrollmentDate(LocalDate.now())
            .build();
        
        testStudentDTO = StudentDTO.builder()
            .name("Jane Smith")
            .email("jane.smith@example.com")
            .age(21)
            .major("Mathematics")
            .phone("0987654321")
            .address("456 Oak Ave")
            .birthDate(LocalDate.of(2002, 5, 15))
            .build();
    }
    
    @Nested
    @DisplayName("GET /api/students 测试")
    class GetStudentsTest {
        
        @Test
        @DisplayName("获取学生列表 - 成功")
        void getStudents_ShouldReturnStudentList() throws Exception {
            // Given
            studentRepository.save(testStudent);
            
            // When & Then
            mockMvc.perform(get("/api/students")
                    .param("page", "0")
                    .param("size", "10")
                    .param("sort", "name,asc")
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpect(jsonPath("$.data.content[0].name").value("John Doe"))
                .andExpect(jsonPath("$.data.content[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1));
        }
        
        @Test
        @DisplayName("获取空学生列表")
        void getStudents_WhenNoStudents_ShouldReturnEmptyList() throws Exception {
            // When & Then
            mockMvc.perform(get("/api/students")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content", hasSize(0)))
                .andExpect(jsonPath("$.data.totalElements").value(0));
        }
        
        @Test
        @DisplayName("分页参数验证")
        void getStudents_WithInvalidPageParams_ShouldReturnBadRequest() throws Exception {
            // When & Then
            mockMvc.perform(get("/api/students")
                    .param("page", "-1")
                    .param("size", "0")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        }
    }
    
    @Nested
    @DisplayName("GET /api/students/{id} 测试")
    class GetStudentByIdTest {
        
        @Test
        @DisplayName("根据ID获取学生 - 成功")
        void getStudentById_WhenStudentExists_ShouldReturnStudent() throws Exception {
            // Given
            Student saved = studentRepository.save(testStudent);
            
            // When & Then
            mockMvc.perform(get("/api/students/{id}", saved.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(saved.getId()))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
        }
        
        @Test
        @DisplayName("根据ID获取学生 - 学生不存在")
        void getStudentById_WhenStudentNotExists_ShouldReturnNotFound() throws Exception {
            // When & Then
            mockMvc.perform(get("/api/students/{id}", 999L)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(containsString("Student not found")));
        }
        
        @Test
        @DisplayName("无效ID格式")
        void getStudentById_WithInvalidId_ShouldReturnBadRequest() throws Exception {
            // When & Then
            mockMvc.perform(get("/api/students/{id}", "invalid")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        }
    }
    
    @Nested
    @DisplayName("POST /api/students 测试")
    class CreateStudentTest {
        
        @Test
        @DisplayName("创建学生 - 成功")
        void createStudent_WithValidData_ShouldCreateStudent() throws Exception {
            // Given
            String studentJson = objectMapper.writeValueAsString(testStudentDTO);
            
            // When & Then
            mockMvc.perform(post("/api/students")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(studentJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.name").value("Jane Smith"))
                .andExpect(jsonPath("$.data.email").value("jane.smith@example.com"));
            
            // 验证数据库中确实创建了学生
            Optional<Student> created = studentRepository.findByEmail("jane.smith@example.com");
            assertThat(created).isPresent();
            assertThat(created.get().getName()).isEqualTo("Jane Smith");
        }
        
        @Test
        @DisplayName("创建学生 - 数据验证失败")
        void createStudent_WithInvalidData_ShouldReturnBadRequest() throws Exception {
            // Given
            StudentDTO invalidStudent = StudentDTO.builder()
                .name("") // 空名称
                .email("invalid-email") // 无效邮箱
                .age(-1) // 无效年龄
                .build();
            String studentJson = objectMapper.writeValueAsString(invalidStudent);
            
            // When & Then
            mockMvc.perform(post("/api/students")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
        }
        
        @Test
        @DisplayName("创建学生 - 邮箱已存在")
        void createStudent_WithDuplicateEmail_ShouldReturnConflict() throws Exception {
            // Given
            studentRepository.save(testStudent);
            
            StudentDTO duplicateEmailStudent = testStudentDTO.toBuilder()
                .email("john.doe@example.com") // 使用已存在的邮箱
                .build();
            String studentJson = objectMapper.writeValueAsString(duplicateEmailStudent);
            
            // When & Then
            mockMvc.perform(post("/api/students")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(studentJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(containsString("Email already exists")));
        }
        
        @Test
        @DisplayName("创建学生 - 请求体为空")
        void createStudent_WithEmptyBody_ShouldReturnBadRequest() throws Exception {
            // When & Then
            mockMvc.perform(post("/api/students")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(""))
                .andExpect(status().isBadRequest());
        }
    }
    
    @Nested
    @DisplayName("PUT /api/students/{id} 测试")
    class UpdateStudentTest {
        
        @Test
        @DisplayName("更新学生 - 成功")
        void updateStudent_WithValidData_ShouldUpdateStudent() throws Exception {
            // Given
            Student saved = studentRepository.save(testStudent);
            
            StudentDTO updateData = testStudentDTO.toBuilder()
                .name("Updated Name")
                .major("Updated Major")
                .build();
            String updateJson = objectMapper.writeValueAsString(updateData);
            
            // When & Then
            mockMvc.perform(put("/api/students/{id}", saved.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(saved.getId()))
                .andExpect(jsonPath("$.data.name").value("Updated Name"))
                .andExpect(jsonPath("$.data.major").value("Updated Major"));
            
            // 验证数据库中确实更新了
            Optional<Student> updated = studentRepository.findById(saved.getId());
            assertThat(updated).isPresent();
            assertThat(updated.get().getName()).isEqualTo("Updated Name");
            assertThat(updated.get().getMajor()).isEqualTo("Updated Major");
        }
        
        @Test
        @DisplayName("更新学生 - 学生不存在")
        void updateStudent_WhenStudentNotExists_ShouldReturnNotFound() throws Exception {
            // Given
            String updateJson = objectMapper.writeValueAsString(testStudentDTO);
            
            // When & Then
            mockMvc.perform(put("/api/students/{id}", 999L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(containsString("Student not found")));
        }
        
        @Test
        @DisplayName("更新学生 - 邮箱被其他学生使用")
        void updateStudent_WithEmailUsedByOther_ShouldReturnConflict() throws Exception {
            // Given
            Student student1 = studentRepository.save(testStudent);
            Student student2 = studentRepository.save(testStudent.toBuilder()
                .email("other@example.com")
                .build());
            
            StudentDTO updateData = testStudentDTO.toBuilder()
                .email("other@example.com") // 使用student2的邮箱
                .build();
            String updateJson = objectMapper.writeValueAsString(updateData);
            
            // When & Then
            mockMvc.perform(put("/api/students/{id}", student1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(containsString("Email already exists")));
        }
    }
    
    @Nested
    @DisplayName("DELETE /api/students/{id} 测试")
    class DeleteStudentTest {
        
        @Test
        @DisplayName("删除学生 - 成功")
        void deleteStudent_WhenStudentExists_ShouldDeleteStudent() throws Exception {
            // Given
            Student saved = studentRepository.save(testStudent);
            
            // When & Then
            mockMvc.perform(delete("/api/students/{id}", saved.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
            
            // 验