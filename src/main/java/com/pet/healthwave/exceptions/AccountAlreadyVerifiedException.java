package com.pet.healthwave.exceptions;

public class AccountAlreadyVerifiedException extends RuntimeException{
    public AccountAlreadyVerifiedException(String message) {
        super(message);
    }
}
