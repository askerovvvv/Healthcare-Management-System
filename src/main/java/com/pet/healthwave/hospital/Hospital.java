package com.pet.healthwave.hospital;

import com.pet.healthwave.doctor.Doctor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "hospital")
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String headPhysician;
    private String hospitalName;
    private String address;
    @OneToMany(mappedBy = "hospital")
    private Set<Doctor> doctors;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hospital hospital = (Hospital) o;
        return Objects.equals(id, hospital.id) && Objects.equals(headPhysician, hospital.headPhysician) && Objects.equals(hospitalName, hospital.hospitalName) && Objects.equals(address, hospital.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, headPhysician, hospitalName, address);
    }

}
