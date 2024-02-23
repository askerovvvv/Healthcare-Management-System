package com.pet.healthwave.doctor;

import com.pet.healthwave.hospital.Hospital;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class DoctorSpecification {

    public DoctorSpecification() {
    }

    public static Specification<Doctor> hasSpecialty(Specialty specialty) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("specialty"), specialty);
    }



}
