package com.pet.healthwave.email;

public interface EmailVerificationService {
    void send(String to, String from);
}
