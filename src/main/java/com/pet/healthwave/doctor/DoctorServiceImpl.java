package com.pet.healthwave.doctor;

import com.pet.healthwave.exceptions.DoctorDidNotAcceptedException;
import com.pet.healthwave.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService{

    private final DoctorRepository doctorRepository;
    private final Logger logger = LoggerFactory.getLogger("DoctorServiceImpl");
    @Override
    public void AcceptDoctor(Long id) {
        int updated = doctorRepository.updateIsAccepted(id);

        if (updated == 0) {
            logger.error("Доктор по айди " + id + " не подтвержден");
            throw new DoctorDidNotAcceptedException(DoctorMessages.DOCTOR_DID_NOT_ACCEPTED);
        }
        logger.info("Доктор по айди " + id + " подтвержден");
    }

    @Override
    public DoctorDTO FillInformationForDoctor(FillInformationRequest request ,Principal connectedDoctor) {
        Doctor doctor = (Doctor) ((UsernamePasswordAuthenticationToken) connectedDoctor).getPrincipal();
        doctor.setExperience(request.experience());
        doctor.setQualifications(request.qualifications());
        doctor.setSpecialty(request.specialty());
        doctorRepository.save(doctor);
        logger.info("Добавлена дополнительная информация для доктора " + doctor);

        return DoctorMapper.INSTANCE.doctorToDTO(doctor);
    }

    @Override
    public DoctorDTO getDoctorProfileById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(DoctorMessages.DOCTOR_NOT_FOUND));

        return DoctorMapper.INSTANCE.doctorToDTO(doctor);
    }


}
