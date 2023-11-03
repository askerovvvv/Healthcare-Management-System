package com.pet.healthwave.validator;

import jakarta.validation.*;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * This class validates fields when user sends request. In necessary request classes(where defined fields for request)
 * there are some annotations like @NotNull and this validator checks if everything is good.
 * It has generic<T> to validate multiple objects.
 *
 * @author askerovvvv
 */


@RequiredArgsConstructor
public class DefaultValidatorImpl<T> implements DefaultValidator<T> {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    /**
     * this method validates and if something is wrong saves it in to set.
     *
     * @param objectToValidate object of class that need to be validated
     * @return List of validation errors(field: message)
     */
    @Override
    public List<CustomValidationError> validate(T objectToValidate) {
        Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);

        return violations.stream()
                .map(violation -> new CustomValidationError(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());
    }
}
