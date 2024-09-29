package com.emazon.user_v1.infrastructure.exception.response;


import com.emazon.user_v1.util.GlobalConstants;

public enum ExceptionResponse {

    USER_NOT_FOUND(GlobalConstants.USER_NOT_FOUND),
    INVALID_AGE("The user must be an adult"),
    PHONE_NUMBER_EXISTS("The phone number already exists"),
    INVALID_PHONE_NUMBER_PATTERN("Invalid phone number pattern"),
    EMAIL_EXISTS("The email already exists"),
    IDENTIFICATION_EXISTS("The identification already exists"),
    INVALID_EMAIL_PATTERN("Invalid email pattern"),
    EMPTY_USER_ATTRIBUTE("The user contains empty attributes"),
    LOCKED_USER("Blocked for 24 hours due to too many failed login attempts"),
    BAD_CREDENTIALS("The given credentials are not valid. Available attempts: "),
    INVALID_TOKEN("Invalid or expired token"),
    ACCESS_DENIED("Access Denied. You do not have sufficient privileges to access this resource."),
    INVALID_DATA_TYPE("One or more fields contain incorrect data types. Check the field types."),
    ROLE_NOT_FOUND("The role was not found");

    private String message;

    ExceptionResponse(String message) { this.message = message; }

    public String getMessage() { return message; }
}
