package com.pet.healthwave.email;

import com.pet.healthwave.exceptions.EmailSenderException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Email business logic is here. It implements interface and has @Service annotation for spring framework,
 * Has @RequiredArgsConstructor annotation to inject dependencies.
 * @author askerovvvv
 */

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService{

    private final Logger logger = LoggerFactory.getLogger("EmailVerificationServiceImpl");
    private final JavaMailSender javaMailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    @Override
    public void saveVerificationToken(EmailVerificationToken token) {
        emailVerificationRepository.save(token);
        logger.info("Создан новый токен: " + token);
    }

    @Override
    public Optional<EmailVerificationToken> getToken(String token) {
        return emailVerificationRepository.findByToken(token);
    }

    @Override
    public int setConfirmedAt(String token) {
        logger.info("По токену: " + token + " аккаунт подтвержден");
        return emailVerificationRepository.updateConfirmedAt(LocalDateTime.now(), token);
    }
}
