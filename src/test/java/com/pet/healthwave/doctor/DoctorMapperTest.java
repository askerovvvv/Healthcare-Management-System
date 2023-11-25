package com.pet.healthwave.doctor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoctorMapperTest {

    @Test
    void testDoctorToDTO() {
        Doctor doctor = new Doctor();
        doctor.setFirstname("firstname");
        doctor.setLastname("lastname");

//        DoctorDTO doctorDTO = DoctorMapper.INSTANCE.map(doctor);

//        assertEquals(doctor.getLastname(), doctorDTO.lastname());
//        assertEquals(doctor.getFirstname(), doctorDTO.firstname());
    }
}