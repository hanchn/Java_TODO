package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.ApiResponse;
import com.example.studentmanagement.dto.StudentDTO;
import com.example.studentmanagement.service.StudentService;
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
import java.util.Optional;

/**
 * 学生管理控制器
 * 
 * @author System
 * @version 1.0
 */
@RestController
@RequestMapping("/api/students")
@Validated
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 创建学生
     * 
     * @param studentDTO 学生信息
     * @return 创建结果
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StudentDTO>> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        try {
            StudentDTO createdStudent = studentService.createStudent(studentDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("学生创建成功", createdStudent));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.conflict(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("创建学生失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID获取学生信息
     * 
     * @param id 学生ID
     * @return 学生信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDTO>> getStudentById(@PathVariable @Min(1) Long id) {
        try {
            Optional<StudentDTO> student = studentService.getStudentById(id);
            if (student.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("获取学生信息成功", student.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound("学生不存在，ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("获取学生信息失败: " + e.getMessage()));
        }
    }

    /**
     * 根据学号获取学生信息
     * 
     * @param studentNumber 学号
     * @return 学生信息
     */
    @GetMapping("/number/{studentNumber}")
    public ResponseEntity<ApiResponse<StudentDTO>> getStudentByStudentNumber(@PathVariable String studentNumber) {
        try {
            Optional<StudentDTO> student = studentService.getStudentByStudentNumber(studentNumber);
            if (student.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("获取学生信息成功", student.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound("学生不存在，学号: " + studentNumber));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("获取学生信息失败: " + e.getMessage()));
        }
    }

    /**
     * 获取所有学生信息
     * 
     * @return 学生列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getAllStudents() {
        try {
            List<StudentDTO> students = studentService.getAllStudents();
            return ResponseEntity.ok(ApiResponse.success("获取学生列表成功", students));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("获取学生列表失败: " + e.getMessage()));
        }
    }

    /**
     * 分页获取学生信息
     * 
     * @param page    页码（从0开始）
     * @param size    每页大小
     * @param sortBy  排序字段
     * @param sortDir 排序方向
     * @return 分页结果
     */
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<StudentDTO>>> getStudentsWithPagination(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<StudentDTO> students = studentService.getStudentsWithPagination(pageable);
            return ResponseEntity.ok(ApiResponse.success("获取学生分页数据成功", students));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("获取学生分页数据失败: " + e.getMessage()));
        }
    }

    /**
     * 根据条件搜索学生
     * 
     * @param name    姓名关键字
     * @param major   专业
     * @param gender  性别
     * @param page    页码
     * @param size    每页大小
     * @param sortBy  排序字段
     * @param sortDir 排序方向
     * @return 搜索结果
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<StudentDTO>>> searchStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) String gender,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<StudentDTO> students = studentService.searchStudents(name, major, gender, pageable);
            return ResponseEntity.ok(ApiResponse.success("搜索学生成功", students));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("搜索学生失败: " + e.getMessage()));
        }
    }

    /**
     * 更新学生信息
     * 
     * @param id         学生ID
     * @param studentDTO 更新的学生信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDTO>> updateStudent(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody StudentDTO studentDTO) {
        try {
            StudentDTO updatedStudent = studentService.updateStudent(id, studentDTO);
            return ResponseEntity.ok(ApiResponse.success("学生信息更新成功", updatedStudent));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("不存在")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound(e.getMessage()));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.conflict(e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("更新学生信息失败: " + e.getMessage()));
        }
    }

    /**
     * 删除学生
     * 
     * @param id 学生ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteStudent(@PathVariable @Min(1) Long id) {
        try {
            boolean deleted = studentService.deleteStudent(id);
            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success("学生删除成功"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound("学生不存在，ID: " + id));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("删除学生失败: " + e.getMessage()));
        }
    }

    /**
     * 批量删除学生
     * 
     * @param ids 学生ID列表
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<ApiResponse<String>> deleteStudentsBatch(@RequestBody @NotEmpty List<Long> ids) {
        try {
            int deletedCount = studentService.deleteStudentsBatch(ids);
            return ResponseEntity.ok(ApiResponse.success("批量删除成功，共删除 " + deletedCount + " 个学生"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("批量删除学生失败: " + e.getMessage()));
        }
    }

    /**
     * 根据年龄范围查询学生
     * 
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @return 学生列表
     */
    @GetMapping("/age-range")
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getStudentsByAgeRange(
            @RequestParam @Min(16) Integer minAge,
            @RequestParam @Min(16) Integer maxAge) {
        try {
            if (minAge > maxAge) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("最小年龄不能大于最大年龄"));
            }
            List<StudentDTO> students = studentService.getStudentsByAgeRange(minAge, maxAge);
            return ResponseEntity.ok(ApiResponse.success("根据年龄范围查询成功", students));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("根据年龄范围查询失败: " + e.getMessage()));
        }
    }

    /**
     * 检查学号是否存在
     * 
     * @param studentNumber 学号
     * @return 检查结果
     */
    @GetMapping("/exists/{studentNumber}")
    public ResponseEntity<ApiResponse<Boolean>> checkStudentNumberExists(@PathVariable String studentNumber) {
        try {
            boolean exists = studentService.existsByStudentNumber(studentNumber);
            return ResponseEntity.ok(ApiResponse.success("检查学号完成", exists));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("检查学号失败: " + e.getMessage()));
        }
    }

    /**
     * 获取学生统计信息
     * 
     * @return 统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStudentStatistics() {
        try {
            Map<String, Object> statistics = Map.of(
                    "totalCount", studentService.getTotalStudentCount(),
                    "countByMajor", studentService.getStudentCountByMajor(),
                    "countByGender", studentService.getStudentCountByGender());
            return ResponseEntity.ok(ApiResponse.success("获取统计信息成功", statistics));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("获取统计信息失败: " + e.getMessage()));
        }
    }
}