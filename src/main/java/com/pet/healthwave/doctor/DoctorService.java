package com.pet.healthwave.doctor;


import java.security.Principal;

public interface DoctorService {
    void AcceptDoctor(Long id);

    DoctorDTO FillInformationForDoctor(FillInformationRequest request, Principal connectedDoctor);

    DoctorDTO getDoctorProfileById(Long id);
}
