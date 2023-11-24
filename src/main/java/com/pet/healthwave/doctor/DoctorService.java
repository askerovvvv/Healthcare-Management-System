package com.pet.healthwave.doctor;


import java.security.Principal;

public interface DoctorService {
    void AcceptDoctor(Long id, Principal connectedHeadPhysician);

    void    FillInformationForDoctor(FillInformationRequest request, Principal connectedDoctor);

    void getDoctorProfileById(Long id);
}
