package com.pet.healthwave.user;

import com.pet.healthwave.auth.AuthMessages;
import com.pet.healthwave.auth.AuthValidator;
import com.pet.healthwave.exceptions.AuthException;
import com.pet.healthwave.exceptions.DefaultValidationException;
import com.pet.healthwave.validator.CustomValidationError;
import com.pet.healthwave.validator.ValidationMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserValidator<ChangePasswordRequest> changePasswordValidator;

    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        List<CustomValidationError> fieldsErrors = changePasswordValidator.validate(request);
        if (!fieldsErrors.isEmpty()) {
            throw new DefaultValidationException(ValidationMessages.VALIDATION_ERROR_MESSAGE, fieldsErrors);
        }

        List<String> passwordErrors = changePasswordValidator.validateChangePassword(request);
        if (!passwordErrors.isEmpty()) {
            throw new AuthException(AuthMessages.PASSWORD_REQUIREMENTS_ERROR_MESSAGE);
        }

        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setPassword(request.newPassword());
        userRepository.save(user);
    }
}
