package com.pet.healthwave.user;

import jakarta.validation.constraints.NotEmpty;

public record ChangePasswordRequest (
        @NotEmpty
        String currentPassword,
        @NotEmpty
        String newPassword,
        @NotEmpty
        String newPasswordConfirm
){
}
