package com.pet.healthwave.hospital;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    // TODO: add city or region
    private String address;
    @OneToMany(mappedBy = "hospital")
    @JsonIgnore
    private Set<Doctor> doctors;

    public String getHospitalName() {
        return hospitalName;
    }

    public String getHeadPhysician() {
        return headPhysician;
    }

    public String getAddress() {
        return address;
    }

    public Set<Doctor> getDoctors() {
        return doctors;
    }
}
