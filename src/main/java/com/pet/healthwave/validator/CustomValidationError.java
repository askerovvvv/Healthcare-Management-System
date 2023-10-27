package com.pet.healthwave.validator;

/**
 * If validation has error, program will save information in this fields
 * @param field field that was wrong
 * @param message description of error
 */
public record CustomValidationError (
        String field,
        String message
) {
}
