package com.pet.healthwave.hospital;

import com.pet.healthwave.doctor.Specialty;

import java.util.List;

public interface HospitalService {

    void addHospital(HospitalRequest request);
    List<HospitalDto> getAllHospitals(Specialty specialty);

}
