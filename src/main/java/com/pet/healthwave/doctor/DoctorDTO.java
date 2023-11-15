package com.pet.healthwave.doctor;

import com.pet.healthwave.user.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.Set;

public record DoctorDTO (
        String firstname,
        String lastname,
        String email,
        Byte age,
        Role role,
        Byte experience,
        Specialty specialty,
        Set<String> qualifications,
        Boolean isAccepted
) {

    public DoctorDTO(String firstname, String lastname, String email, Byte age, Role role, Byte experience, Specialty specialty, Set<String> qualifications, Boolean isAccepted) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.age = age;
        this.role = role;
        this.experience = experience;
        this.specialty = specialty;
        this.qualifications = qualifications;
        this.isAccepted = isAccepted;
    }
}
