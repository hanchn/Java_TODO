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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 学生服务测试类
 * 
 * @author System
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student testStudent;
    private StudentDTO testStudentDTO;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("张三");
        testStudent.setStudentNumber("20210001");
        testStudent.setAge(20);
        testStudent.setGender("男");
        testStudent.setMajor("计算机科学与技术");
        testStudent.setEmail("zhangsan@example.com");
        testStudent.setPhone("13800138001");
        testStudent.setEnrollmentDate(LocalDate.now());
        testStudent.setCreatedTime(LocalDateTime.now());
        testStudent.setUpdatedTime(LocalDateTime.now());

        testStudentDTO = new StudentDTO();
        testStudentDTO.setName("张三");
        testStudentDTO.setStudentNumber("20210001");
        testStudentDTO.setAge(20);
        testStudentDTO.setGender("男");
        testStudentDTO.setMajor("计算机科学与技术");
        testStudentDTO.setEmail("zhangsan@example.com");
        testStudentDTO.setPhone("13800138001");
    }

    @Test
    void testCreateStudent_Success() {
        // 准备
        when(studentRepository.existsByStudentNumber(anyString())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // 执行
        StudentDTO result = studentService.createStudent(testStudentDTO);

        // 验证
        assertNotNull(result);
        assertEquals(testStudentDTO.getName(), result.getName());
        assertEquals(testStudentDTO.getStudentNumber(), result.getStudentNumber());
        verify(studentRepository).existsByStudentNumber(testStudentDTO.getStudentNumber());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void testCreateStudent_StudentNumberExists() {
        // 准备
        when(studentRepository.existsByStudentNumber(anyString())).thenReturn(true);

        // 执行和验证
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.createStudent(testStudentDTO);
        });
        
        assertTrue(exception.getMessage().contains("学号已存在"));
        verify(studentRepository).existsByStudentNumber(testStudentDTO.getStudentNumber());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testGetStudentById_Found() {
        // 准备
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // 执行
        Optional<StudentDTO> result = studentService.getStudentById(1L);

        // 验证
        assertTrue(result.isPresent());
        assertEquals(testStudent.getName(), result.get().getName());
        assertEquals(testStudent.getStudentNumber(), result.get().getStudentNumber());
        verify(studentRepository).findById(1L);
    }

    @Test
    void testGetStudentById_NotFound() {
        // 准备
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行
        Optional<StudentDTO> result = studentService.getStudentById(1L);

        // 验证
        assertFalse(result.isPresent());
        verify(studentRepository).findById(1L);
    }

    @Test
    void testGetAllStudents() {
        // 准备
        List<Student> students = Arrays.asList(testStudent);
        when(studentRepository.findAll()).thenReturn(students);

        // 执行
        List<StudentDTO> result = studentService.getAllStudents();

        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testStudent.getName(), result.get(0).getName());
        verify(studentRepository).findAll();
    }

    @Test
    void testGetStudentsWithPagination() {
        // 准备
        Pageable pageable = PageRequest.of(0, 10);
        Page<Student> studentPage = new PageImpl<>(Arrays.asList(testStudent));
        when(studentRepository.findAll(pageable)).thenReturn(studentPage);

        // 执行
        Page<StudentDTO> result = studentService.getStudentsWithPagination(pageable);

        // 验证
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testStudent.getName(), result.getContent().get(0).getName());
        verify(studentRepository).findAll(pageable);
    }

    @Test
    void testUpdateStudent_Success() {
        // 准备
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        testStudentDTO.setName("李四");
        testStudentDTO.setAge(21);

        // 执行
        StudentDTO result = studentService.updateStudent(1L, testStudentDTO);

        // 验证
        assertNotNull(result);
        verify(studentRepository).findById(1L);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void testUpdateStudent_NotFound() {
        // 准备
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行和验证
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.updateStudent(1L, testStudentDTO);
        });
        
        assertTrue(exception.getMessage().contains("学生不存在"));
        verify(studentRepository).findById(1L);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testDeleteStudent_Success() {
        // 准备
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);

        // 执行
        boolean result = studentService.deleteStudent(1L);

        // 验证
        assertTrue(result);
        verify(studentRepository).existsById(1L);
        verify(studentRepository).deleteById(1L);
    }

    @Test
    void testDeleteStudent_NotFound() {
        // 准备
        when(studentRepository.existsById(1L)).thenReturn(false);

        // 执行和验证
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.deleteStudent(1L);
        });
        
        assertTrue(exception.getMessage().contains("学生不存在"));
        verify(studentRepository).existsById(1L);
        verify(studentRepository, never()).deleteById(1L);
    }

    @Test
    void testSearchStudentsByName() {
        // 准备
        List<Student> students = Arrays.asList(testStudent);
        when(studentRepository.findByNameContainingIgnoreCase("张")).thenReturn(students);

        // 执行
        List<StudentDTO> result = studentService.searchStudentsByName("张");

        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testStudent.getName(), result.get(0).getName());
        verify(studentRepository).findByNameContainingIgnoreCase("张");
    }

    @Test
    void testGetStudentsByMajor() {
        // 准备
        List<Student> students = Arrays.asList(testStudent);
        when(studentRepository.findByMajor("计算机科学与技术")).thenReturn(students);

        // 执行
        List<StudentDTO> result = studentService.getStudentsByMajor("计算机科学与技术");

        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testStudent.getMajor(), result.get(0).getMajor());
        verify(studentRepository).findByMajor("计算机科学与技术");
    }

    @Test
    void testExistsByStudentNumber() {
        // 准备
        when(studentRepository.existsByStudentNumber("20210001")).thenReturn(true);

        // 执行
        boolean result = studentService.existsByStudentNumber("20210001");

        // 验证
        assertTrue(result);
        verify(studentRepository).existsByStudentNumber("20210001");
    }

    @Test
    void testGetTotalStudentCount() {
        // 准备
        when(studentRepository.count()).thenReturn(10L);

        // 执行
        long result = studentService.getTotalStudentCount();

        // 验证
        assertEquals(10L, result);
        verify(studentRepository).count();
    }
}