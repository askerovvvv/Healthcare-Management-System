package com.pet.healthwave.validator;

import jakarta.validation.*;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultValidatorImpl<T> implements DefaultValidator<T> {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Override
    public List<CustomValidationError> validate(T objectToValidate) {
        Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);

        return violations.stream()
                .map(violation -> new CustomValidationError(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());
    }
}
