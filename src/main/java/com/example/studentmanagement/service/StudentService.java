package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 学生服务接口
 * 
 * @author System
 * @version 1.0
 */
public interface StudentService {

    /**
     * 创建学生
     * 
     * @param studentDTO 学生信息
     * @return 创建的学生信息
     */
    StudentDTO createStudent(StudentDTO studentDTO);

    /**
     * 根据ID获取学生信息
     * 
     * @param id 学生ID
     * @return 学生信息
     */
    Optional<StudentDTO> getStudentById(Long id);

    /**
     * 根据学号获取学生信息
     * 
     * @param studentNumber 学号
     * @return 学生信息
     */
    Optional<StudentDTO> getStudentByStudentNumber(String studentNumber);

    /**
     * 获取所有学生信息
     * 
     * @return 学生列表
     */
    List<StudentDTO> getAllStudents();

    /**
     * 分页获取学生信息
     * 
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<StudentDTO> getStudentsWithPagination(Pageable pageable);

    /**
     * 根据条件分页查询学生
     * 
     * @param name 姓名关键字
     * @param major 专业
     * @param gender 性别
     * @param pageable 分页参数
     * @return 分页结果
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
     * @return 是否删除成功
     */
    boolean deleteStudent(Long id);

    /**
     * 批量删除学生
     * 
     * @param ids 学生ID列表
     * @return 删除成功的数量
     */
    int deleteStudentsBatch(List<Long> ids);

    /**
     * 根据姓名模糊查询学生
     * 
     * @param name 姓名关键字
     * @return 学生列表
     */
    List<StudentDTO> searchStudentsByName(String name);

    /**
     * 根据专业查询学生
     * 
     * @param major 专业
     * @return 学生列表
     */
    List<StudentDTO> getStudentsByMajor(String major);

    /**
     * 根据性别查询学生
     * 
     * @param gender 性别
     * @return 学生列表
     */
    List<StudentDTO> getStudentsByGender(String gender);

    /**
     * 根据年龄范围查询学生
     * 
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @return 学生列表
     */
    List<StudentDTO> getStudentsByAgeRange(Integer minAge, Integer maxAge);

    /**
     * 检查学号是否存在
     * 
     * @param studentNumber 学号
     * @return 是否存在
     */
    boolean existsByStudentNumber(String studentNumber);

    /**
     * 获取学生总数
     * 
     * @return 学生总数
     */
    long getTotalStudentCount();

    /**
     * 获取各专业学生统计
     * 
     * @return 专业统计结果
     */
    Map<String, Long> getStudentCountByMajor();

    /**
     * 获取各性别学生统计
     * 
     * @return 性别统计结果
     */
    Map<String, Long> getStudentCountByGender();
}