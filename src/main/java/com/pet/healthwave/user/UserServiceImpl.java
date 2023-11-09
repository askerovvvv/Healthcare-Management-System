package com.pet.healthwave.user;

import com.pet.healthwave.auth.AuthMessages;
import com.pet.healthwave.auth.AuthValidator;
import com.pet.healthwave.exceptions.AuthException;
import com.pet.healthwave.exceptions.DefaultValidationException;
import com.pet.healthwave.validator.CustomValidationError;
import com.pet.healthwave.validator.ValidationMessages;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final Logger logger = LoggerFactory.getLogger("UserServiceImpl");
    private final UserRepository userRepository;
    private final AuthValidator<ChangePasswordRequest> changePasswordValidator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        List<CustomValidationError> fieldsErrors = changePasswordValidator.validate(request);
        if (!fieldsErrors.isEmpty()) {
            logger.error("У пользователя: " + connectedUser.getName() + " ошибка при валидации полей: " + fieldsErrors);
            throw new DefaultValidationException(ValidationMessages.VALIDATION_ERROR_MESSAGE, fieldsErrors);
        }

        List<String> passwordErrors = changePasswordValidator.validatePassword(request.newPassword(), request.newPasswordConfirm());
        if (!passwordErrors.isEmpty()) {
            logger.error("У пользователя: " + connectedUser.getName() + " ошибка при валидации пароля: " + passwordErrors);
            throw new AuthException(AuthMessages.PASSWORD_REQUIREMENTS_ERROR_MESSAGE);
        }

        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            logger.error("Пользователь: " + connectedUser.getName() + " ввел неверный текущий пароль.");
            throw new AuthException(UserMessages.WRONG_CURRENT_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        logger.info("Пользователь: " + connectedUser.getName() +" успешно сменил пароль");
    }
}
