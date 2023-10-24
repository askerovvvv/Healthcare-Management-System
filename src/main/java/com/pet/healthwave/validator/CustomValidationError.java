package com.pet.healthwave.validator;

public record CustomValidationError (
        String field,
        String message
) {
}
