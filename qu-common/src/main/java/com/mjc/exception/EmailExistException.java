package com.mjc.exception;

// 创建 EmailExistException 类
public class EmailExistException extends RuntimeException {
    public EmailExistException(String message) {
        super(message);
    }
}
