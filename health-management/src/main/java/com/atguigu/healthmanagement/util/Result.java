package com.atguigu.healthmanagement.util;

import lombok.Data;

import java.io.Serializable;

//@Data
//public class Result<T> implements Serializable {
//
//    private Integer code;    // 状态码
//    private String message;  // 消息
//    private T data;         // 数据
//
//    // 私有构造方法
//    private Result(Integer code, String message, T data) {
//        this.code = code;
//        this.message = message;
//        this.data = data;
//    }
//
//    // 成功响应（无数据）
//    public static <T> Result<T> success() {
//        return new Result<>(200, "成功", null);
//    }
//
//    // 成功响应（有数据）
//    public static <T> Result<T> success(T data) {
//        return new Result<>(200, "成功", data);
//    }
//
//    // 成功响应（自定义消息）
//    public static <T> Result<T> success(T data, String message) {
//        return new Result<>(200, message, data);
//    }
//
//    // 错误响应
//    public static <T> Result<T> error(String message) {
//        return new Result<>(500, message, null);
//    }
//
//    // 错误响应（自定义状态码）
//    public static <T> Result<T> error(Integer code, String message) {
//        return new Result<>(code, message, null);
//    }
//
//    // 未授权响应
//    public static <T> Result<T> unauthorized(String message) {
//        return new Result<>(401, message, null);
//    }
//
//    // 禁止访问响应
//    public static <T> Result<T> forbidden(String message) {
//        return new Result<>(403, message, null);
//    }
//
//    // 资源不存在响应
//    public static <T> Result<T> notFound(String message) {
//        return new Result<>(404, message, null);
//    }
//}


@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private T data;

    // 成功响应
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
//        result.setCode(200);
//        result.setMessage("成功");
//        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(T data, String message) {
        Result<T> result = new Result<>();
//        result.setCode(200);
//        result.setMessage(message);
//        result.setData(data);
        return result;
    }

    // 错误响应
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
//        result.setCode(500);
//        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
//        result.setCode(code);
//        result.setMessage(message);
        return result;
    }

    // 未找到响应
    public static <T> Result<T> notFound(String message) {
        Result<T> result = new Result<>();
//        result.setCode(404);
//        result.setMessage(message);
        return result;
    }

    // 业务错误
    public static <T> Result<T> businessError(String message) {
        Result<T> result = new Result<>();
//        result.setCode(400);
//        result.setMessage(message);
        return result;
    }

    // 未授权
    public static <T> Result<T> unauthorized(String message) {
        Result<T> result = new Result<>();
//        result.setCode(401);
//        result.setMessage(message);
        return result;
    }
}