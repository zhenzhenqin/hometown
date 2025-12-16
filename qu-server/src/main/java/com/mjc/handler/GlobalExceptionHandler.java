package com.mjc.handler;

import com.mjc.Result.Result;
import com.mjc.exception.AccountNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获自定义的账号不存在异常
    @ExceptionHandler(AccountNotFoundException.class)
    public Result<?> handleAccountNotFound(AccountNotFoundException e) {

        return Result.error(e.getMessage());
    }

    // 捕获其他所有异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleGlobalException(Exception e) {
        // 打印异常日志，方便排查
        e.printStackTrace();
        // 返回通用错误提示
        return Result.error("服务器内部错误，请联系管理员");
    }
}
