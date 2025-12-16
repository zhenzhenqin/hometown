package com.mjc.exception;

public class PhoneExistException extends RuntimeException {
    public PhoneExistException(){}

    public PhoneExistException(String msg) {
        super(msg);
    }
}
