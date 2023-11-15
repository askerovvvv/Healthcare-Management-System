package com.pet.healthwave.doctor;


import com.pet.healthwave.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("DOCTOR")
@PrimaryKeyJoinColumn(name = "user_id")
public class Doctor extends User {
    private Byte experience;
    @Enumerated(EnumType.STRING)
    private Specialty specialty;
    private Set<String> qualifications;
    private Boolean isAccepted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(experience, doctor.experience) && specialty == doctor.specialty && Objects.equals(qualifications, doctor.qualifications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), experience, specialty, qualifications);
    }


}
