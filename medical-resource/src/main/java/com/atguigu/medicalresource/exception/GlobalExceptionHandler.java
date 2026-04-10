package com.atguigu.medicalresource.exception;


import com.atguigu.medicalresource.dto.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataAccessException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handle(Exception ex) {
        ex.printStackTrace();
        if (ex instanceof IllegalArgumentException) {
            return ApiResponse.fail(ex.getMessage());
        }
        if (ex instanceof DataAccessException) {
            return ApiResponse.fail("数据库错误：" + ex.getMessage());
        }
        return ApiResponse.fail("服务器内部错误");
    }
}