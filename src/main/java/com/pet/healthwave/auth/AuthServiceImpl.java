package com.pet.healthwave.auth;

import com.pet.healthwave.email.EmailVerificationToken;
import com.pet.healthwave.user.Role;
import com.pet.healthwave.user.User;
import com.pet.healthwave.user.UserRepository;
import com.pet.healthwave.validator.CustomValidationError;
import com.pet.healthwave.validator.ValidationException;
import com.pet.healthwave.validator.ValidationMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final AuthValidator<RegisterRequest> registerValidator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String registerService(RegisterRequest request) {
        List<CustomValidationError> fieldsErrors = registerValidator.validate(request);
        if (!fieldsErrors.isEmpty()) {
            throw new ValidationException(ValidationMessages.VALIDATION_ERROR_MESSAGE);
        }

        validatePasswordAndEmail(
                request.getPassword(),
                request.getPasswordConfirm(),
                request.getEmail()
        );

        User user = createUserFromRequest(request);
        userRepository.save(user);

        EmailVerificationToken verificationToken = createToken(user);



    }

    @Override
    public AuthenticationResponse authenticateService(AuthenticationRequest request) {
        return null;
    }

    private User createUserFromRequest(RegisterRequest request) {
        return User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .age(request.getAge())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PATIENT)
                .emailVerified(false)
                .build();
    }

    private EmailVerificationToken createToken(User user) {
        String token = UUID.randomUUID().toString();

        return EmailVerificationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
    }

    private void validatePasswordAndEmail(String password, String passwordConfirm, String email) {
        List<String> passwordErrors = registerValidator.validatePassword(password, passwordConfirm);

        if (!passwordErrors.isEmpty()) {
            throw new ValidationException(ValidationMessages.PASSWORD_REQUIREMENTS_ERROR_MESSAGE);
        }

        if (!registerValidator.checkIfUserExists(email)) {
            throw new AuthException(AuthMessages.USER_ALREADY_EXISTS_MESSAGE);
        }
    }
}
