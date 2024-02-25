package com.pet.healthwave.user;


public record UserDTO(
        String firstname,
        String lastname,
        String email,
        Byte age,
        Role role
) {
}
