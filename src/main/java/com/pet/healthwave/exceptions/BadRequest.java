package com.pet.healthwave.exceptions;

public class BadRequest extends RuntimeException{
    public BadRequest(String message) {
        super(message);
    }
}
