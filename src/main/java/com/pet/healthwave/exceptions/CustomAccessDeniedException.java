package com.pet.healthwave.exceptions;

public class CustomAccessDeniedException extends RuntimeException{
    public CustomAccessDeniedException(String message) {
        super(message);
    }
}
