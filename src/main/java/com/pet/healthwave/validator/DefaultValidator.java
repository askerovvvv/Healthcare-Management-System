package com.pet.healthwave.validator;

import java.util.List;

public interface DefaultValidator<T> {

    List<CustomValidationError> validate(T objectToValidate);
}
