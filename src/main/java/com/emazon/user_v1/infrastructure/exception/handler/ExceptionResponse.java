package com.emazon.user_v1.infrastructure.exception.handler;


import com.emazon.user_v1.util.GlobalConstants;

public enum ExceptionResponse {

    USER_NOT_FOUND(GlobalConstants.USER_NOT_FOUND),
    INVALID_AGE("The user must be an adult"),
    PHONE_NUMBER_EXISTS("The phone number already exists"),
    INVALID_PHONE_NUMBER_PATTERN("Invalid phone number pattern"),
    EMAIL_EXISTS("The email already exists"),
    IDENTIFICATION_EXISTS("The identification already exists"),
    INVALID_EMAIL_PATTERN("Invalid email pattern"),
    EMPTY_USER_ATTRIBUTE("The user contains empty attributes");

    private String message;

    ExceptionResponse(String message) { this.message = message; }

    public String getMessage() { return message; }
}
