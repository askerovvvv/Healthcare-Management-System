package com.pet.healthwave.validator;

/**
 * If validation has error, program will save information in these fields.
 * Don't need to use class, record is better.
 *
 * @param field field that was wrong
 * @param message description of error
 *
 * @author askerovvvv
 */
public record CustomValidationError (
        String field,
        String message
) {
}
