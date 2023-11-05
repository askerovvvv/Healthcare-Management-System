package com.pet.healthwave.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record AuthenticationRequest (
        @Email(message = "Пожалуйста введите верный формат почты.")
        String email,
        @NotEmpty(message = "Пароль не может быть пустым.")
        String password
) {

}
