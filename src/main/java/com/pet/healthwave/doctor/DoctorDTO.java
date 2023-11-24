package com.pet.healthwave.doctor;

import com.pet.healthwave.hospital.Hospital;
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
        Boolean isAccepted,
        String hospitalName
) {

}
