package com.pet.healthwave.email;

import java.util.Optional;

public interface EmailVerificationService {
    void send(String to, String email);
    void saveVerificationToken(EmailVerificationToken token);
    Optional<EmailVerificationToken> getToken(String token);

    int setConfirmedAt(String token);
}
