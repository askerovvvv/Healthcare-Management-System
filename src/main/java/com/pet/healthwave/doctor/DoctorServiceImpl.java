package com.pet.healthwave.doctor;

import com.pet.healthwave.email.EmailSender;
import com.pet.healthwave.exceptions.CustomAccessDeniedException;
import com.pet.healthwave.exceptions.DefaultValidationException;
import com.pet.healthwave.exceptions.DoctorDidNotAcceptedException;
import com.pet.healthwave.exceptions.ObjectNotFoundException;
import com.pet.healthwave.hospital.Hospital;
import com.pet.healthwave.hospital.HospitalMessages;
import com.pet.healthwave.hospital.HospitalRepository;
import com.pet.healthwave.validator.CustomValidationError;
import com.pet.healthwave.validator.DefaultValidator;
import com.pet.healthwave.validator.ValidationMessages;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService{

    private final DoctorRepository doctorRepository;
    private final HospitalRepository hospitalRepository;
    private final DefaultValidator<FillInformationRequest> doctorValidator;

    private final Logger logger = LoggerFactory.getLogger("DoctorServiceImpl");
    private final EmailSender emailSender;

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             HospitalRepository hospitalRepository,
                             @Qualifier("defaultValidatorImpl") DefaultValidator<FillInformationRequest> doctorValidator,
                             EmailSender emailSender) {
        this.doctorRepository = doctorRepository;
        this.hospitalRepository = hospitalRepository;
        this.doctorValidator = doctorValidator;
        this.emailSender = emailSender;
    }

    @Override
    public void AcceptDoctor(Long id, Principal connectedHeadPhysician) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isEmpty()) {
            logger.error("При подтверждении доктор по id " + id + ": не найден!");
            throw new ObjectNotFoundException(DoctorMessages.DOCTOR_NOT_FOUND);
        }

        Doctor headPhysician = (Doctor) ((UsernamePasswordAuthenticationToken) connectedHeadPhysician).getPrincipal();
        String hospitalHeadPhysician = doctor.get().getHospital().getHeadPhysician();

        if (!hospitalHeadPhysician.equals(headPhysician.getEmail())) {
            logger.error(connectedHeadPhysician + " не может принять: " + doctor + " поскольку он не главврач этой больницы ");
            throw new CustomAccessDeniedException(DoctorMessages.DOCTOR_CAN_NOT_ACCEPT);
        }

        int updated = doctorRepository.updateIsAccepted(id);
        if (updated == 0) {
            logger.error("Доктор по айди " + id + " не подтвержден");
            throw new DoctorDidNotAcceptedException(DoctorMessages.DOCTOR_DID_NOT_ACCEPTED);
        }

        emailSender.send(doctorRepository.findById(id).get().getEmail(), DoctorMessages.DOCTOR_IS_ACCEPTED);
        logger.info("Доктор по айди " + id + " подтвержден");
    }

    @Override
    public void FillInformationForDoctor(FillInformationRequest request ,Principal connectedDoctor) {
        List<CustomValidationError> fieldsErrors = doctorValidator.validate(request);
        if (!fieldsErrors.isEmpty()) {
            logger.error("При заполнении информации о докторе: " + connectedDoctor + " ошибка при валидации: " + fieldsErrors);
            throw new DefaultValidationException(ValidationMessages.VALIDATION_ERROR_MESSAGE, fieldsErrors);
        }

        Optional<Hospital> hospital = hospitalRepository.findById(request.hospitalId());
        if (hospital.isEmpty()) {
            throw new ObjectNotFoundException(HospitalMessages.HOSPITAL_NOT_FOUND);
        }

        Doctor doctor = (Doctor) ((UsernamePasswordAuthenticationToken) connectedDoctor).getPrincipal();
        doctor.setExperience(request.experience());
        doctor.setQualifications(request.qualifications());
        doctor.setSpecialty(request.specialty());
        doctor.setHospital(hospital.get());
        doctorRepository.save(doctor);

        emailSender.send(hospital.get().getHeadPhysician(), DoctorMessages.NEW_DOCTOR_IN_HOSPITAL);
        logger.info("Добавлена дополнительная информация для доктора " + doctor);
    }

    @Override
    public void getDoctorProfileById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(DoctorMessages.DOCTOR_NOT_FOUND));

//        return DoctorMapper.INSTANCE.doctorToDTO(doctor);
    }


}
