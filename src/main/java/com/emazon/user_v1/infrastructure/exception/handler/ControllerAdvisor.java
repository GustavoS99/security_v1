package com.emazon.user_v1.infrastructure.exception.handler;

import com.emazon.user_v1.domain.exception.*;
import com.emazon.user_v1.infrastructure.exception.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvisor {

    private static final String MESSAGE = "message";

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException httpMessageNotReadableException
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_DATA_TYPE.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAgeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidAgeException(
            InvalidAgeException invalidAgeException
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_AGE.getMessage()));
    }

    @ExceptionHandler(PhoneNumberExistsException.class)
    public ResponseEntity<Map<String, String>> handlePhoneNumberExistsException(
            PhoneNumberExistsException phoneNumberExistsException
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PHONE_NUMBER_EXISTS.getMessage()));
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailExistsException(
            EmailExistsException emailExistsException
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.EMAIL_EXISTS.getMessage()));
    }

    @ExceptionHandler(IdentificationExistsException.class)
    public ResponseEntity<Map<String, String>> handleIdentificationExistsException(
            IdentificationExistsException identificationExistsException
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.IDENTIFICATION_EXISTS.getMessage()));
    }

    @ExceptionHandler(EmptyUserAttributeException.class)
    public ResponseEntity<Map<String, String>> handleEmptyUserAttributeException(
            EmptyUserAttributeException emptyUserAttributeException
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.EMPTY_USER_ATTRIBUTE.getMessage()));
    }



    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(
            UserNotFoundException userNotFoundException
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.USER_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(UserLockedException.class)
    public ResponseEntity<Map<String, String>> handleUserLockedException(
            UserLockedException userLockedException
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.LOCKED_USER.getMessage()));
    }

    @ExceptionHandler(BadUserCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadUserCredentialsException(
            BadUserCredentialsException badUserCredentialsException
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap(
                        MESSAGE,
                        ExceptionResponse.BAD_CREDENTIALS.getMessage().concat(badUserCredentialsException.getMessage()))
                );
    }

    @ExceptionHandler(SystemRoleNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSystemRoleNotFoundException(
            SystemRoleNotFoundException systemRoleNotFoundException
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.ROLE_NOT_FOUND.getMessage()));
    }
}
