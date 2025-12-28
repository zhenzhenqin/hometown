package com.mjc.exception;

public class AccountDisabledException extends RuntimeException {

    public AccountDisabledException() {
    }

    public AccountDisabledException(String message) {
        super(message);
    }
}
