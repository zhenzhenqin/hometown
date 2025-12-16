package com.mjc.handler;

import com.mjc.Result.Result;
import com.mjc.exception.AccountLockedException;
import com.mjc.exception.AccountNotFoundException;
import com.mjc.exception.PasswordErrorException;
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

    //捕获密码错误异常
    @ExceptionHandler(PasswordErrorException.class)
    public Result<?> handlePasswordErrorException(PasswordErrorException e){
        return Result.error(e.getMessage());
    }

    //捕获账号被锁定无法登录的异常
    @ExceptionHandler(AccountLockedException.class)
    public Result<?> handleAccountLockedException(AccountLockedException e){
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
