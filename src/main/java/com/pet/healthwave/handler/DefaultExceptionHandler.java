package com.pet.healthwave.handler;

import com.pet.healthwave.exceptions.AuthException;
import com.pet.healthwave.exceptions.DefaultValidationException;
import com.pet.healthwave.exceptions.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(DefaultValidationException.class)
    public ResponseEntity<?> handleException(DefaultValidationException exception, HttpServletRequest request) {
        Map<String, Object> apiError = new HashMap<>();
        apiError.put("path", request.getRequestURI());
        apiError.put("message", exception.getMessage());
        apiError.put("localDateTime", LocalDateTime.now());
        apiError.put("status", HttpStatus.BAD_REQUEST.value());
        apiError.put("errors", exception.getErrors());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiError> handleException(AuthException exception, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ApiError> handleException(ObjectNotFoundException exception, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

}
