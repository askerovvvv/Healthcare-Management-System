package com.pet.healthwave.exceptions;

import com.pet.healthwave.validator.CustomValidationError;
import lombok.Getter;

import java.util.List;

@Getter
public class DefaultValidationException extends RuntimeException {

    private final List<CustomValidationError> errors;

    public DefaultValidationException(String message, List<CustomValidationError> errors) {
        super(message);
        this.errors = errors;
    }
}
