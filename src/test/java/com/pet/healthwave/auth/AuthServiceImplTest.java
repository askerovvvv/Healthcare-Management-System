package com.pet.healthwave.auth;

import com.pet.healthwave.config.JwtService;
import com.pet.healthwave.email.EmailVerificationService;
import com.pet.healthwave.email.EmailVerificationToken;
import com.pet.healthwave.exceptions.AuthException;
import com.pet.healthwave.user.Role;
import com.pet.healthwave.user.User;
import com.pet.healthwave.user.UserRepository;
import com.pet.healthwave.validator.CustomValidationError;
import com.pet.healthwave.exceptions.DefaultValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailVerificationService emailVerificationService;
    @Mock
    private AuthenticationManager authManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthValidator<RegisterRequest> registerValidator;
    @Mock
    private AuthValidator<AuthenticationRequest> authValidator;
    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest mockRegisterRequest;
    private AuthenticationRequest mockAuthenticationRequest;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockRegisterRequest = RegisterRequest.builder()
                .firstname("testName")
                .email("test@.example.com")
                .password("password")
                .build();

        mockAuthenticationRequest = AuthenticationRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();
    }

    @Test
    void testRegister_Success() {
        User mockUser = User.builder()
                .firstname("testName")
                .email("test@.example.com")
                .password("hashedPassword")
                .role(Role.PATIENT)
                .emailVerified(false)
                .build();

        when(registerValidator.validate(mockRegisterRequest)).thenReturn(Collections.emptyList());
        when(registerValidator.validatePassword(anyString(), anyString())).thenReturn(Collections.emptyList());
        when(registerValidator.checkIfUserExists(anyString())).thenReturn(Boolean.TRUE);
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        emailVerificationService.saveVerificationToken(any(EmailVerificationToken.class));
        doNothing().when(emailVerificationService).send(anyString(), anyString());

        String expectedResponse = authService.registerService(mockRegisterRequest);
        String actualResponse = "На вашу почту отправлено сообщение, перейдите и активируйте аккаунт.";

        assertEquals(expectedResponse, actualResponse);
        verify(userRepository, times(1)).save(mockUser);
        verify(emailVerificationService, times(1))
                .saveVerificationToken(any(EmailVerificationToken.class));
        verify(emailVerificationService, times(1)).send(anyString(), anyString());
    }

    @Test
    void testRegister_WithEmptyField_ShouldThrowsValidationException() {
        List<CustomValidationError> validationErrors = Collections.singletonList(
                new CustomValidationError("passwordConfirm", "Не должен быть пустым")
        );

        when(registerValidator.validate((mockRegisterRequest))).thenReturn(validationErrors);

        assertThrows(DefaultValidationException.class, () -> authService.registerService(mockRegisterRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_WithIncorrectPasswords_ShouldThrowsValidationException() {
        List<String> passwordErrors = new LinkedList<>();
        passwordErrors.add("just to test");
        passwordErrors.add("just to test2");

        when(registerValidator.validate(mockRegisterRequest)).thenReturn(Collections.emptyList());
        when(registerValidator.validatePassword(mockRegisterRequest.getPassword(), mockRegisterRequest.getPasswordConfirm())).thenReturn(passwordErrors);

        assertThrows(DefaultValidationException.class, () -> authService.registerService(mockRegisterRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_UserAlreadyExists_ShouldThrowsValidationException() {

        when(registerValidator.validate(mockRegisterRequest)).thenReturn(Collections.emptyList());
        when(registerValidator.validatePassword(mockRegisterRequest.getPassword(), mockRegisterRequest.getPasswordConfirm())).thenReturn(Collections.emptyList());

        assertThrows(AuthException.class, () -> authService.registerService(mockRegisterRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testAuthenticate_Success() {
        User mockUser = User.builder()
                .email(mockAuthenticationRequest.getEmail())
                .emailVerified(false)
                .build();

        when(authValidator.validate(mockAuthenticationRequest)).thenReturn(Collections.emptyList());
        when(authManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByUsername(mockAuthenticationRequest.getEmail())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(mockUser)).thenReturn("jwt token");

        AuthenticationResponse expectedResponse = authService.authenticateService(mockAuthenticationRequest);

        assertEquals(expectedResponse.getToken(), "jwt token");
    }

    @Test
    void testAuthenticate_BadCredentials_ShouldThrowsAuthException() {

        when(authValidator.validate(mockAuthenticationRequest)).thenReturn(Collections.emptyList());
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(AuthException.class, () -> authService.authenticateService(mockAuthenticationRequest));
    }
}