package com.emazon.user_v1.domain.exception;

public class BadUserCredentialsException extends RuntimeException {
    public BadUserCredentialsException(String message) { super(message); }
}
