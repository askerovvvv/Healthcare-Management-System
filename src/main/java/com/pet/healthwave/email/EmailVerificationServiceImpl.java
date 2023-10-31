package com.pet.healthwave.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService{

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.sender.email}")
    private final String senderEmail;


    @Override
    public void send(String to, String from) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
//            helper.setText();
            helper.setSubject(EmailSenderMessages.CONFIRM_ACCOUNT);
            helper.setFrom(senderEmail);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
