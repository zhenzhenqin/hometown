package com.mjc.exception;

public class CaptchaException extends RuntimeException {
    public CaptchaException(){}

    public CaptchaException(String message) {
        super(message);
    }
}
