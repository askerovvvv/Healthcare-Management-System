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

@Service
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSender{

    private final JavaMailSender javaMailSender;
    private final Logger logger = LoggerFactory.getLogger("EmailVerificationServiceImpl");

    @Value("${spring.mail.sender.email}")
    private String senderEmail;

    @Override
    @Async
    public void send(String to, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setText(content, true);
            helper.setTo(to);
            helper.setSubject(EmailSenderMessages.CONFIRM_ACCOUNT);
            helper.setFrom(senderEmail);
            javaMailSender.send(message);
            logger.info("Ссылка для подтверждении аккаунта отправлена для: " + to);

        } catch (MessagingException e) {
            logger.error("Ошибка при отправке на почту: " + to);
            throw new EmailSenderException(EmailSenderMessages.MESSAGE_SEND_ERROR);
        }
    }

}
