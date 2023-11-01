package com.pet.healthwave.auth;

import com.pet.healthwave.email.EmailVerificationService;
import com.pet.healthwave.email.EmailVerificationToken;
import com.pet.healthwave.user.Role;
import com.pet.healthwave.user.User;
import com.pet.healthwave.user.UserRepository;
import com.pet.healthwave.validator.CustomValidationError;
import com.pet.healthwave.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthValidator<RegisterRequest> registerValidator;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailVerificationService emailVerificationService;
    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest requestData;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        requestData = RegisterRequest.builder()
                .firstname("testName")
                .email("test@.example.com")
                .password("password")
                .build();
    }

    @Test
    void testRegister_Success() {
        User user = User.builder()
                .firstname("testName")
                .email("test@.example.com")
                .password("hashedPassword")
                .role(Role.PATIENT)
                .emailVerified(false)
                .build();

        when(registerValidator.validate(requestData)).thenReturn(Collections.emptyList());
        when(registerValidator.validatePassword(anyString(), anyString())).thenReturn(Collections.emptyList());
        when(registerValidator.checkIfUserExists(anyString())).thenReturn(Boolean.TRUE);
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        emailVerificationService.saveVerificationToken(any(EmailVerificationToken.class));
        doNothing().when(emailVerificationService).send(anyString(), anyString());

        String actualResponse = authService.registerService(requestData);
        String expectedResponse = "На вашу почту отправлено сообщение, перейдите и активируйте аккаунт.";

        assertEquals(expectedResponse, actualResponse);
        verify(userRepository, times(1)).save(user);
        verify(emailVerificationService, times(1))
                .saveVerificationToken(any(EmailVerificationToken.class));
        verify(emailVerificationService, times(1)).send(anyString(), anyString());
    }

    @Test
    void testRegister_WithEmptyField_ShouldThrowValidationException() {
        List<CustomValidationError> validationErrors = Collections.singletonList(
                new CustomValidationError("passwordConfirm", "Не должен быть пустым")
        );

        when(registerValidator.validate((requestData))).thenReturn(validationErrors);

        assertThrows(ValidationException.class, () -> authService.registerService(requestData));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_WithIncorrectPasswords_ShouldThrowValidationException() {
        List<String> passwordErrors = new LinkedList<>();
        passwordErrors.add("just to test");
        passwordErrors.add("just to test2");

        when(registerValidator.validate(requestData)).thenReturn(Collections.emptyList());
        when(registerValidator.validatePassword(requestData.getPassword(), requestData.getPasswordConfirm())).thenReturn(passwordErrors);

        assertThrows(ValidationException.class, () -> authService.registerService(requestData));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_UserAlreadyExists_ShouldThrowValidationException() {

        when(registerValidator.validate(requestData)).thenReturn(Collections.emptyList());
        when(registerValidator.validatePassword(requestData.getPassword(), requestData.getPasswordConfirm())).thenReturn(Collections.emptyList());

        assertThrows(AuthException.class, () -> authService.registerService(requestData));
        verify(userRepository, never()).save(any());
    }
}