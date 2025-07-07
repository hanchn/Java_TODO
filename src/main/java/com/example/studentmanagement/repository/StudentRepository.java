package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 学生数据访问层接口
 * 
 * @author System
 * @version 1.0
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * 根据学号查找学生
     * 
     * @param studentNumber 学号
     * @return 学生信息
     */
    Optional<Student> findByStudentNumber(String studentNumber);

    /**
     * 检查学号是否存在
     * 
     * @param studentNumber 学号
     * @return 是否存在
     */
    boolean existsByStudentNumber(String studentNumber);

    /**
     * 根据姓名模糊查询学生
     * 
     * @param name 姓名关键字
     * @return 学生列表
     */
    List<Student> findByNameContainingIgnoreCase(String name);

    /**
     * 根据专业查找学生
     * 
     * @param major 专业
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
     * 根据多个条件分页查询学生
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
     * 统计各专业学生数量
     * 
     * @return 专业统计结果
     */
    @Query("SELECT s.major, COUNT(s) FROM Student s GROUP BY s.major")
    List<Object[]> countStudentsByMajor();

    /**
     * 统计各性别学生数量
     * 
     * @return 性别统计结果
     */
    @Query("SELECT s.gender, COUNT(s) FROM Student s GROUP BY s.gender")
    List<Object[]> countStudentsByGender();
}