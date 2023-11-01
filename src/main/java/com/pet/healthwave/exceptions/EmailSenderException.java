package com.pet.healthwave.exceptions;

public class EmailSenderException extends RuntimeException{
    public EmailSenderException(String message) {
        super(message);
    }
}
