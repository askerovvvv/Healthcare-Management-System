package com.pet.healthwave.auth;

import com.pet.healthwave.config.JwtService;
import com.pet.healthwave.email.EmailSender;
import com.pet.healthwave.email.EmailSenderImpl;
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
    @Mock
    private AuthServiceImpl mockAuthService;
    @Mock
    private EmailSenderImpl emailSender;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        registerRequest = new RegisterRequest(
                "testName",
                "lastname",
                "test@example.com",
                "password",
                "password",
                (byte) 44,
                false);

        authenticationRequest = new AuthenticationRequest("test@example.com", "password");
        mockAuthService = new AuthServiceImpl(
                userRepository,
                emailVerificationService,
                emailSender,
                passwordEncoder,
                jwtService,
                authManager,
                registerValidator,
                authValidator
        );
    }

    @Test
    void testRegister_Success() {
        User mockUser = User.builder()
                .firstname("testName")
                .lastname("lastname")
                .email("test@example.com")
                .password("hashedPassword")
                .age((byte) 44)
                .emailVerified(false)
                .role(Role.USER)
                .build();

        when(registerValidator.validate(registerRequest)).thenReturn(Collections.emptyList());
        when(registerValidator.validatePassword(anyString(), anyString())).thenReturn(Collections.emptyList());
        when(registerValidator.checkIfUserExists(anyString())).thenReturn(Boolean.FALSE);
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        emailVerificationService.saveVerificationToken(any(EmailVerificationToken.class));
        doNothing().when(emailSender).send(anyString(), anyString());

        String expectedResponse = mockAuthService.registerService(registerRequest);
        String actualResponse = "На вашу почту отправлено сообщение, перейдите и активируйте аккаунт.";

        assertEquals(expectedResponse, actualResponse);
        verify(userRepository, times(1)).save(mockUser);
        verify(emailVerificationService, times(1))
                .saveVerificationToken(any(EmailVerificationToken.class));
        verify(emailSender, times(1)).send(anyString(), anyString());
    }

    @Test
    void testRegister_EmptyField_ValidationException() {
        List<CustomValidationError> validationErrors = Collections.singletonList(
                new CustomValidationError("passwordConfirm", "Не должен быть пустым")
        );

        when(registerValidator.validate((registerRequest))).thenReturn(validationErrors);

        assertThrows(DefaultValidationException.class, () -> mockAuthService.registerService(registerRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_IncorrectPassword_AuthException() {
        List<String> passwordErrors = new LinkedList<>();
        passwordErrors.add("just to test");
        passwordErrors.add("just to test2");

        when(registerValidator.validate(registerRequest)).thenReturn(Collections.emptyList());
        when(registerValidator.validatePassword(registerRequest.password(), registerRequest.passwordConfirm())).thenReturn(passwordErrors);

        assertThrows(AuthException.class, () -> mockAuthService.registerService(registerRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_UserAlreadyExists_AuthException() {

        when(registerValidator.validate(registerRequest)).thenReturn(Collections.emptyList());
        when(registerValidator.validatePassword(registerRequest.password(),
                registerRequest.passwordConfirm())).thenReturn(Collections.emptyList());
        when(registerValidator.checkIfUserExists(registerRequest.email())).thenReturn(Boolean.TRUE);

        assertThrows(AuthException.class, () -> mockAuthService.registerService(registerRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testAuthenticate_Success() {
        User mockUser = User.builder()
                .email(authenticationRequest.email())
                .emailVerified(false)
                .build();

        when(authValidator.validate(authenticationRequest)).thenReturn(Collections.emptyList());
        when(authManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByUsername(authenticationRequest.email())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(mockUser)).thenReturn("jwt token");

        AuthenticationResponse expectedResponse = mockAuthService.authenticateService(authenticationRequest);

        assertEquals(expectedResponse.token(), "jwt token");
    }

    @Test
    void testAuthenticate_BadCredentials_AuthException() {

        when(authValidator.validate(authenticationRequest)).thenReturn(Collections.emptyList());
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(AuthException.class, () -> mockAuthService.authenticateService(authenticationRequest));
    }
}