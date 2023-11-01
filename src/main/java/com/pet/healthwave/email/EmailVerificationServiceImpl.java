package com.pet.healthwave.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService{

    private final JavaMailSender javaMailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    @Value("${spring.mail.sender.email}")
    private String senderEmail;

    @Override
    @Async
    public void send(String to, String from) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
//            helper.setText();
            helper.setSubject(EmailSenderMessages.CONFIRM_ACCOUNT);
            helper.setFrom(senderEmail);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSenderException(EmailSenderMessages.MESSAGE_SEND_ERROR);
        }
    }

    @Override
    public void saveVerificationToken(EmailVerificationToken token) {
        emailVerificationRepository.save(token);
    }

    @Override
    public Optional<EmailVerificationToken> getToken(String token) {
        return emailVerificationRepository.findByToken(token);
    }

    @Override
    public int setConfirmedAt(String token) {
        return emailVerificationRepository.updateConfirmedAt(LocalDateTime.now(), token);
    }
}
