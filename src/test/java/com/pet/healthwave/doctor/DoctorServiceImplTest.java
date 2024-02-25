package com.pet.healthwave.doctor;

import com.pet.healthwave.email.EmailSender;
import com.pet.healthwave.hospital.HospitalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class DoctorServiceImplTest {
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private HospitalRepository hospitalRepository;
    @Mock
    private EmailSender emailSender;
    @Mock
    private DoctorServiceImpl doctorService;

    @BeforeEach
    public void setUp() {
        doctorService = new DoctorServiceImpl(
                doctorRepository,
                hospitalRepository,
                emailSender
        );
    }

    @Test
    void testAcceptDoctor() {

    }

    @Test
    void fillInformationForDoctor() {
    }

    @Test
    void getDoctorProfileById() {
    }

    @Test
    void changeHeadPhysician() {
    }

    @Test
    void getAllDoctors() {
    }
}