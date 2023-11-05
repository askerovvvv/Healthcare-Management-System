package com.pet.healthwave.user;

import com.pet.healthwave.exceptions.AuthException;
import com.pet.healthwave.exceptions.DefaultValidationException;
import com.pet.healthwave.validator.CustomValidationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserValidator<ChangePasswordRequest> changePasswordValidator;
    @Mock
    private UserServiceImpl userService;
    private ChangePasswordRequest changePasswordRequest;
    private UsernamePasswordAuthenticationToken authToken;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        changePasswordRequest = new ChangePasswordRequest(
                "old password",
                "new password",
                "new password"
        );
        authToken = mock(UsernamePasswordAuthenticationToken.class);

        userService = new UserServiceImpl(userRepository, changePasswordValidator, passwordEncoder);
    }

    @Test
    void testChangePassword_Success() {

        User user = User.builder()
                .password("old password")
                .build();

        when(changePasswordValidator.validate(changePasswordRequest)).thenReturn(Collections.emptyList());
        when(changePasswordValidator.validateChangePassword(changePasswordRequest)).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(changePasswordRequest.newPassword())).thenReturn("new password");
        when(userRepository.save(user)).thenReturn(user);
        when(authToken.getPrincipal()).thenReturn(user);

        userService.changePassword(
                changePasswordRequest,
                authToken
        );

        verify(userRepository).save(user);
        assertEquals("new password", user.getPassword());
    }

    @Test
    void testChangePassword_ValidationException() {
        List<CustomValidationError> validationErrors = Collections.singletonList(
                new CustomValidationError("newPassword", "Новый пароль не должен быть пустым!")
        );
        when(changePasswordValidator.validate(changePasswordRequest)).thenReturn(validationErrors);

        assertThrows(DefaultValidationException.class,
                () -> userService.changePassword(changePasswordRequest, authToken));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testChangePassword_IncorrectPassword_ValidationException() {
        List<String> passwordErrors = new ArrayList<>();
        passwordErrors.add("just to test");

        when(changePasswordValidator.validate(changePasswordRequest)).thenReturn(Collections.emptyList());
        when(changePasswordValidator.validateChangePassword(changePasswordRequest)).thenReturn(passwordErrors);

        assertThrows(AuthException.class,
                () -> userService.changePassword(changePasswordRequest, authToken));
        verify(userRepository, never()).save(any());
    }

}