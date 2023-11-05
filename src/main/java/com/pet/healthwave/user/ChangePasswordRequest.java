package com.pet.healthwave.user;

import jakarta.validation.constraints.NotEmpty;

public record ChangePasswordRequest (
        @NotEmpty(message = "Поле текущий пароль не должен быть пустым!")
        String currentPassword,
        @NotEmpty(message = "Новый пароль не должен быть пустым!")
        String newPassword,
        @NotEmpty(message = "Подтверждение пароля не должен быть пустым!")
        String newPasswordConfirm
){
}
