package com.pet.healthwave.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * To authenticate user need to fill in these fields. Don't need to use class, record is better.
 * Every field has annotation to validate data
 *
 * @param email --> username
 * @param password
 *
 * @author askerovvvv
 */

public record AuthenticationRequest (
        @Email(message = "Пожалуйста введите верный формат почты.")
        String email,
        @NotEmpty(message = "Пароль не может быть пустым.")
        String password
) {

}
