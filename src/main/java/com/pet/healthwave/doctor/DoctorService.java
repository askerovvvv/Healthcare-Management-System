package com.pet.healthwave.doctor;


import java.security.Principal;
import java.util.List;

public interface DoctorService {
    void AcceptDoctor(Long id, Principal connectedHeadPhysician);
    void    FillInformationForDoctor(FillInformationRequest request, Principal connectedDoctor);
    DoctorDTO getDoctorProfileById(Long id);
    void changeHeadPhysician(Long doctorId, Integer hospitalId);
    List<DoctorDTO> getAllDoctors(Specialty bySpecialty);
}
