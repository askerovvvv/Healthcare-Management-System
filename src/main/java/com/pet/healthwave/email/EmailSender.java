package com.pet.healthwave.email;

public interface EmailSender {
    void send(String to, String content);
}
