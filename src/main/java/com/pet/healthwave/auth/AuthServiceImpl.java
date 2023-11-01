package com.pet.healthwave.auth;

import com.pet.healthwave.email.EmailVerificationService;
import com.pet.healthwave.email.EmailVerificationToken;
import com.pet.healthwave.user.Role;
import com.pet.healthwave.user.User;
import com.pet.healthwave.user.UserRepository;
import com.pet.healthwave.validator.CustomValidationError;
import com.pet.healthwave.validator.ValidationException;
import com.pet.healthwave.validator.ValidationMessages;
import jakarta.transaction.Transactional;
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
    private final EmailVerificationService emailVerificationService;

    @Override
    public String registerService(RegisterRequest request) {
        List<CustomValidationError> fieldsErrors = registerValidator.validate(request);
        if (!fieldsErrors.isEmpty()) {
            throw new ValidationException(ValidationMessages.VALIDATION_ERROR_MESSAGE);
        }

        List<String> passwordErrors = registerValidator.validatePassword(request.getPassword(), request.getPasswordConfirm());
        if (!passwordErrors.isEmpty()) {
            throw new ValidationException(ValidationMessages.PASSWORD_REQUIREMENTS_ERROR_MESSAGE);
        }

        if (!registerValidator.checkIfUserExists(request.getEmail())) {
            throw new AuthException(AuthMessages.USER_ALREADY_EXISTS_MESSAGE);
        }

        User user = createUserFromRequest(request);
        userRepository.save(user);

        EmailVerificationToken verificationToken = createToken(user);
        emailVerificationService.saveVerificationToken(verificationToken);

        String link = "http://localhost:8080/api/v1/auth/confirm?token=" + verificationToken;

        emailVerificationService.send(request.getEmail(), buildEmail(request.getFirstname(), link));

        return AuthMessages.USER_REGISTERED;
    }

    @Override
    @Transactional
    public void activateAccount(String token) {
        EmailVerificationToken verificationToken = emailVerificationService.getToken(token)
                .orElseThrow(() -> new NullPointerException("As"));

        if (verificationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("as");
        }

        LocalDateTime expiresAt = verificationToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("as");
        }

        emailVerificationService.setConfirmedAt(token);
        userRepository.updateEmailVerified(verificationToken.getUser().getEmail());
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


