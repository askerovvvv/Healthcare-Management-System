package com.pet.healthwave.doctor;


import com.pet.healthwave.hospital.Hospital;
import com.pet.healthwave.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("DOCTOR")
@PrimaryKeyJoinColumn(name = "user_id")
@EntityListeners(AuditingEntityListener.class)
public class Doctor extends User {
    private Byte experience;
    @Enumerated(EnumType.STRING)
    private Specialty specialty;
    private Set<String> qualifications;
    private Boolean isAccepted;
    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModified;

    @LastModifiedBy
    @Column(insertable = false)
    private Long lastModifiedBy;

    public Hospital getHospital() {
        return hospital;
    }

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

    @Override
    public String toString() {
        return "Doctor{" +
                "experience=" + experience +
                ", specialty=" + specialty +
                ", qualifications=" + qualifications +
                ", isAccepted=" + isAccepted +
                ", hospital=" + (hospital != null ? hospital.getHospitalName() : null) + // или любое другое свойство, которое вы хотите включить
                ", lastModified=" + lastModified +
                ", lastModifiedBy=" + lastModifiedBy +
                "} " + super.toString();
    }
}
