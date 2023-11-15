package com.pet.healthwave.user;

import com.pet.healthwave.doctor.Specialty;

import java.util.Set;

public record UserDTO(
        String firstname,
        String lastname,
        String email,
        Byte age,
        Role role
) {
}
