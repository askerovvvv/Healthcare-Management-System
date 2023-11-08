package com.pet.healthwave.user;

import jakarta.validation.constraints.NotEmpty;

/**
 * To change password user need to fill in these fields. Don't need to use class, record is better.
 * Every field has annotation to validate data
 *
 * @param currentPassword password in db
 * @param newPassword password that should be saved in db
 * @param newPasswordConfirm  just to match the newPassword
 */

public record ChangePasswordRequest (
        @NotEmpty(message = "Поле текущий пароль не должен быть пустым!")
        String currentPassword,
        @NotEmpty(message = "Новый пароль не должен быть пустым!")
        String newPassword,
        @NotEmpty(message = "Подтверждение пароля не должен быть пустым!")
        String newPasswordConfirm
){
}
