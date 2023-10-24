package com.pet.healthwave.auth;

import com.pet.healthwave.user.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotEmpty(message = "Имя не может быть пустым.")
    @Size(max = 20, message = "Слишком длинное имя.")
    private String firstname;

    @NotEmpty(message = "Фамилия не может быть пустым.")
    @Size(max = 20, message = "Слишком длинная фамилия.")
    private String lastname;

    @Email(message = "Пожалуйста введите верный формат почты.")
    private String email;

    @NotEmpty(message = "Пароль не может быть пустым.")
    private String password;

    @NotEmpty(message = "Подтверждение пароля не может быть пустым.")
    private String passwordConfirm;

    @NotNull(message = "Возраст не может быть пустым.")
    private Byte age;
}
