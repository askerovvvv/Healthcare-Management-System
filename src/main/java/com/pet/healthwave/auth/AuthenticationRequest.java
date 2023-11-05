package com.pet.healthwave.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest {
    @Email(message = "Пожалуйста введите верный формат почты.")
    private String email;
    @NotEmpty(message = "Пароль не может быть пустым.")
    private String password;
}
