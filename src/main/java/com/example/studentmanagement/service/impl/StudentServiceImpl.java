package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.service.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 学生服务实现类
 * 
 * @author System
 * @version 1.0
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        // 检查学号是否已存在
        if (studentRepository.existsByStudentNumber(studentDTO.getStudentNumber())) {
            throw new RuntimeException("学号已存在: " + studentDTO.getStudentNumber());
        }

        Student student = convertToEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);
        return convertToDTO(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentDTO> getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentDTO> getStudentByStudentNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentDTO> getStudentsWithPagination(Pageable pageable) {
        return studentRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentDTO> searchStudents(String name, String major, String gender, Pageable pageable) {
        return studentRepository.findStudentsWithFilters(name, major, gender, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("学生不存在，ID: " + id));

        // 如果学号发生变化，检查新学号是否已存在
        if (!existingStudent.getStudentNumber().equals(studentDTO.getStudentNumber())) {
            if (studentRepository.existsByStudentNumber(studentDTO.getStudentNumber())) {
                throw new RuntimeException("学号已存在: " + studentDTO.getStudentNumber());
            }
        }

        // 更新字段（保留ID和时间戳）
        existingStudent.setName(studentDTO.getName());
        existingStudent.setStudentNumber(studentDTO.getStudentNumber());
        existingStudent.setAge(studentDTO.getAge());
        existingStudent.setGender(studentDTO.getGender());
        existingStudent.setMajor(studentDTO.getMajor());
        existingStudent.setEmail(studentDTO.getEmail());
        existingStudent.setPhone(studentDTO.getPhone());
        if (studentDTO.getEnrollmentDate() != null) {
            existingStudent.setEnrollmentDate(studentDTO.getEnrollmentDate());
        }

        Student updatedStudent = studentRepository.save(existingStudent);
        return convertToDTO(updatedStudent);
    }

    @Override
    public boolean deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("学生不存在，ID: " + id);
        }
        studentRepository.deleteById(id);
        return true;
    }

    @Override
    public int deleteStudentsBatch(List<Long> ids) {
        List<Student> studentsToDelete = studentRepository.findAllById(ids);
        studentRepository.deleteAll(studentsToDelete);
        return studentsToDelete.size();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> searchStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsByMajor(String major) {
        return studentRepository.findByMajor(major).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsByGender(String gender) {
        return studentRepository.findByGender(gender).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsByAgeRange(Integer minAge, Integer maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByStudentNumber(String studentNumber) {
        return studentRepository.existsByStudentNumber(studentNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalStudentCount() {
        return studentRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getStudentCountByMajor() {
        List<Object[]> results = studentRepository.countStudentsByMajor();
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> (Long) result[1]
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getStudentCountByGender() {
        List<Object[]> results = studentRepository.countStudentsByGender();
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> (Long) result[1]
                ));
    }

    /**
     * 将实体转换为DTO
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
     * 将DTO转换为实体
     * 
     * @param studentDTO 学生DTO
     * @return 学生实体
     */
    private Student convertToEntity(StudentDTO studentDTO) {
        Student student = new Student();
        BeanUtils.copyProperties(studentDTO, student);
        return student;
    }
}