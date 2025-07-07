package com.example.studentmanagement.exception;

import com.example.studentmanagement.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 
 * @author System
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理请求体参数校验异常
     * 
     * @param ex 异常
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest("参数校验失败").getData() != null ? 
                      ApiResponse.badRequest("参数校验失败") : 
                      new ApiResponse<>(400, "参数校验失败", errors));
    }

    /**
     * 处理表单参数校验异常
     * 
     * @param ex 异常
     * @return 错误响应
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleBindException(BindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(400, "参数校验失败", errors));
    }

    /**
     * 处理路径参数校验异常
     * 
     * @param ex 异常
     * @return 错误响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest("参数校验失败: " + message));
    }

    /**
     * 处理参数类型转换异常
     * 
     * @param ex 异常
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<String>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        String message = String.format("参数 '%s' 的值 '%s' 类型不正确，期望类型: %s", 
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest(message));
    }

    /**
     * 处理运行时异常
     * 
     * @param ex 异常
     * @return 错误响应
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        // 根据异常消息判断错误类型
        String message = ex.getMessage();
        if (message != null) {
            if (message.contains("不存在") || message.contains("未找到")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.notFound(message));
            } else if (message.contains("已存在") || message.contains("重复")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.conflict(message));
            }
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("操作失败: " + message));
    }

    /**
     * 处理非法参数异常
     * 
     * @param ex 异常
     * @return 错误响应
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest("参数错误: " + ex.getMessage()));
    }

    /**
     * 处理其他未捕获的异常
     * 
     * @param ex 异常
     * @return 错误响应
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        // 记录异常日志（在实际项目中应该使用日志框架）
        System.err.println("未处理的异常: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        ex.printStackTrace();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("系统内部错误，请联系管理员"));
    }
}