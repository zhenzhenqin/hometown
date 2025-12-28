package com.mjc.handler;

import com.mjc.Result.Result;
import com.mjc.exception.*;
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

    //注册账号已存在拦截
    @ExceptionHandler(AccountExistException.class)
    public Result<?> handleAccountExistException(AccountExistException e){
        return Result.error(e.getMessage());
    }

    //捕获验证码错误
    @ExceptionHandler(CaptchaException.class)
    public Result<?> handleCaptchaException(CaptchaException e){
        return Result.error(e.getMessage());
    }

    // 捕获手机号已存在异常
    @ExceptionHandler(PhoneExistException.class)
    public Result<?> handlePhoneExistException(PhoneExistException e){
        return Result.error(e.getMessage());
    }

    // 捕获邮箱已存在异常
    @ExceptionHandler(EmailExistException.class)
    public Result<?> handleEmailExistException(EmailExistException e) {
        return Result.error(e.getMessage());
    }

    // 捕获ip被禁用异常
    @ExceptionHandler(AccountDisabledException.class)
    public Result<?> handleAccountDisabledException(AccountDisabledException e){
        return Result.error(e.getMessage());
    }

    // 捕获数据库唯一约束异常
    @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
    public Result<?> handleDuplicateKeyException(org.springframework.dao.DuplicateKeyException e) {
        // 检查异常消息以确定具体约束
        String message = e.getMessage();
        if (message.contains("admin_email_unique")) {
            return Result.error("邮箱已被使用，请更换邮箱地址");
        } else if (message.contains("admin_username_unique")) {
            return Result.error("用户名已被使用，请更换用户名");
        } else if (message.contains("phone")) {
            return Result.error("手机号已被使用，请更换手机号");
        }
        return Result.error("数据重复，请检查输入信息");
    }

    // 捕获 SQL 约束异常
    @ExceptionHandler(java.sql.SQLIntegrityConstraintViolationException.class)
    public Result<?> handleSQLIntegrityConstraintViolationException(java.sql.SQLIntegrityConstraintViolationException e) {
        String message = e.getMessage();
        if (message.contains("admin_email_unique")) {
            return Result.error("邮箱已被使用，请更换邮箱地址");
        } else if (message.contains("admin_username_unique")) {
            return Result.error("用户名已被使用，请更换用户名");
        } else if (message.contains("phone")) {
            return Result.error("手机号已被使用，请更换手机号");
        }
        return Result.error("数据约束冲突，请检查输入信息");
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
