package com.pet.healthwave.hospital;

import com.pet.healthwave.doctor.Doctor;
import com.pet.healthwave.doctor.Specialty;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class HospitalSpecification {


    public static Specification<Hospital> hasDoctorInSpecialty(Specialty specialty) {
        return (root, query, criteriaBuilder) -> {
            Join<Doctor, Hospital> hospitalDoctors = root.join("doctors");
            return criteriaBuilder.equal(hospitalDoctors.get("specialty"), specialty);
        };
    }
}
