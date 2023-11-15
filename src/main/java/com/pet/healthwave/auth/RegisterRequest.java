package com.pet.healthwave.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Registration details. Don't need to use class, record is better.
 * Every field has at list one annotation to validate data
 * @param firstname
 * @param lastname
 * @param email it would be user's username
 * @param password password to save on db
 * @param passwordConfirm just to match the password
 * @param age
 * @author askerovvvv
 */

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
        Byte age,

        Boolean isDoctor
) {

}
