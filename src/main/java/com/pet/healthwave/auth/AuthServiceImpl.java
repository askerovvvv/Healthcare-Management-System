package com.pet.healthwave.auth;

import com.pet.healthwave.config.JwtService;
import com.pet.healthwave.email.EmailVerificationService;
import com.pet.healthwave.email.EmailVerificationToken;
import com.pet.healthwave.exceptions.*;
import com.pet.healthwave.user.Role;
import com.pet.healthwave.user.User;
import com.pet.healthwave.user.UserRepository;
import com.pet.healthwave.validator.CustomValidationError;
import com.pet.healthwave.validator.ValidationMessages;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Main auth business logic is here. It implements interface and has @Service annotation for spring framework,
 * @RequiredArgsConstructor annotation to inject dependencies.
 *
 * @author askerovvvv
 */

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final AuthValidator<RegisterRequest> registerValidator;
    private final AuthValidator<AuthenticationRequest> authValidator;

    @Override
    @Transactional
    public String registerService(RegisterRequest request) {
        List<CustomValidationError> fieldsErrors = registerValidator.validate(request);
        if (!fieldsErrors.isEmpty()) {
            logger.error("У пользователя " + request.email() + " ошибка при валидации полей " + fieldsErrors);
            throw new DefaultValidationException(ValidationMessages.VALIDATION_ERROR_MESSAGE, fieldsErrors);
        }

        List<String> passwordErrors = registerValidator.validatePassword(request.password(), request.passwordConfirm());
        if (!passwordErrors.isEmpty()) {
            logger.error("У пользователя " + request.email() + " ошибка при валидации пароля " + passwordErrors);
            throw new AuthException(AuthMessages.PASSWORD_REQUIREMENTS_ERROR_MESSAGE);
        }

        if (registerValidator.checkIfUserExists(request.email())) {
            logger.error("Аккаунт " + request.email() + " уже зарегистрирован");
            throw new AuthException(AuthMessages.USER_ALREADY_EXISTS_MESSAGE);
        }

        User user = createUserFromRequest(request);
        userRepository.save(user);
        logger.info("Новый пользователь " + user.getEmail());

        EmailVerificationToken verificationToken = createToken(user);
        emailVerificationService.saveVerificationToken(verificationToken);

        String link = "http://localhost:8080/api/v1/auth/confirm?token=" + verificationToken.getToken();

        emailVerificationService.send(request.email(), buildEmail(request.firstname(), link));
        logger.info("Ссылка для подтверждении аккаунта отправлена для " + user.getEmail());

        return AuthMessages.USER_REGISTERED;
    }

    @Override
    @Transactional
    public String confirmAccount(String token) {
        EmailVerificationToken verificationToken = emailVerificationService.getToken(token)
                .orElseThrow(() -> new ObjectNotFoundException(AuthMessages.TOKEN_NOT_FOUND_MESSAGE));

        if (verificationToken.getConfirmedAt() != null) {
            logger.error("По данному токену аккаунт уже активирован " + token);
            throw new AccountAlreadyVerifiedException(AuthMessages.ACCOUNT_ALREADY_VERIFIED_MESSAGE);
        }

        LocalDateTime expiresAt = verificationToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            logger.error("Время токена истекло " + token);
            throw new TokenExpiredException(AuthMessages.VERIFY_TOKEN_EXPIRED_MESSAGE);
        }

        emailVerificationService.setConfirmedAt(token);
        userRepository.updateEmailVerified(verificationToken.getUser().getEmail());
        logger.info("Аккаунт подтвержден по токену " + token);

        return AuthMessages.USER_ACTIVATE_ACCOUNT;
    }

    @Override
    public AuthenticationResponse authenticateService(AuthenticationRequest request) {
        List<CustomValidationError> fieldsErrors = authValidator.validate(request);
        if (!fieldsErrors.isEmpty()) {
            logger.error("У пользователя " + request.email() + " ошибка при валидации полей " + fieldsErrors);
            throw new DefaultValidationException(ValidationMessages.VALIDATION_ERROR_MESSAGE, fieldsErrors);
        }

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (BadCredentialsException exception) {
            logger.error("У пользователя " + request.email() + " неверные данные для входа");
            throw new AuthException(AuthMessages.BADCREDENTIAL_MESSAGE);
        }

        Optional<User> user = userRepository.findByUsername(request.email());
        String jwtToken = jwtService.generateToken(user.get());
        logger.info("Новый вход в аккаунт " + user.get().getEmail());

        return new AuthenticationResponse(jwtToken);
    }

    private User createUserFromRequest(RegisterRequest request) {
        return User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .age(request.age())
                .password(passwordEncoder.encode(request.password()))
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

    /**
     * Server sends to user's email html template with some data, this method is just html template.
     *
     * @param name user's name
     * @param link to confirm account
     * @return html template
     */
    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Подтвердите ваш адрес электронной почты</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Привет " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Благодарим за регистрацию. Нажмите на ссылку ниже чтобы активировать аккаунт: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Подтвердить аккаунт</a> </p></blockquote>\n Срок действия ссылки истекает через 15 минут. <p>Всего хорошего</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}


