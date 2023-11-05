package com.pet.healthwave.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotEmpty(message = "Имя не может быть пустым.")
        @Size(max = 20, message = "Слишком длинное имя.")
        String firstname,

        @NotEmpty(message = "Фамилия не может быть пустым.")
        @Size(max = 20, message = "Слишком длинная фамилия.")
        String lastname,

        @Email(message = "Пожалуйста введите верный формат почты.")
        String email,

        @NotEmpty(message = "Пароль не может быть пустым.")
        String password,

        @NotEmpty(message = "Подтверждение пароля не может быть пустым.")
        String passwordConfirm,

        @NotNull(message = "Возраст не может быть пустым.")
        Byte age
) {

}
