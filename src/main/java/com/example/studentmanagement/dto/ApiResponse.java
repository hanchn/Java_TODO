package com.example.studentmanagement.dto;

/**
 * 统一API响应结果类
 * 
 * @author System
 * @version 1.0
 */
public class ApiResponse<T> {

    private int code;
    private String message;
    private T data;
    private long timestamp;

    // 构造函数
    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResponse(int code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public ApiResponse(int code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    // 成功响应的静态方法
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "操作成功");
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "操作成功", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    // 失败响应的静态方法
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message);
    }

    // 参数错误响应
    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(400, message);
    }

    // 未找到资源响应
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(404, message);
    }

    // 冲突响应（如数据已存在）
    public static <T> ApiResponse<T> conflict(String message) {
        return new ApiResponse<>(409, message);
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
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