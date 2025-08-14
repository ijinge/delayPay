package com.ijinge.delaypay.aop;

import com.ijinge.delaypay.entity.ApiResponse;
import com.ijinge.delaypay.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 捕获所有未处理的异常
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e, HttpServletRequest request) {
        log.error("请求异常，URL: {}, 错误信息: {}", request.getRequestURI(), e.getMessage(), e);
        return ApiResponse.error(500, "服务器内部错误，请稍后重试");
    }

    // 捕获自定义业务异常
    @ExceptionHandler(BizException.class)
    public ApiResponse<?> handleBizException(BizException e) {
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

}
