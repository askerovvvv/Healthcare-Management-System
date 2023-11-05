package com.pet.healthwave.user;

import com.pet.healthwave.auth.AuthValidator;
import com.pet.healthwave.validator.DefaultValidator;
import com.pet.healthwave.validator.DefaultValidatorImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserValidator<T> extends DefaultValidatorImpl<T> {

    private final AuthValidator<T> authValidator;

    public List<String> validateChangePassword(ChangePasswordRequest request) {

        List<String> passwordErrors = authValidator.validatePassword(request.newPassword(), request.newPasswordConfirm());
        return passwordErrors;
    }

}
