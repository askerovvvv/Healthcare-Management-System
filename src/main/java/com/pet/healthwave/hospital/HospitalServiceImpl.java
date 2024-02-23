package com.pet.healthwave.hospital;

import com.pet.healthwave.doctor.*;
import com.pet.healthwave.email.EmailSender;
import com.pet.healthwave.exceptions.BadRequest;
import com.pet.healthwave.exceptions.DefaultValidationException;
import com.pet.healthwave.exceptions.ObjectNotFoundException;
import com.pet.healthwave.user.Role;
import com.pet.healthwave.validator.CustomValidationError;
import com.pet.healthwave.validator.DefaultValidator;
import com.pet.healthwave.validator.ValidationMessages;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService{

    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;
    private final EmailSender emailSender;
    private final Logger logger = LoggerFactory.getLogger("Hospital");

    @Autowired
    @Qualifier("defaultValidatorImpl")
    private DefaultValidator<HospitalRequest> hospitalValidator;

    @Override
    @Transactional
    public void addHospital(HospitalRequest request) {
        List<CustomValidationError> fieldsErrors = hospitalValidator.validate(request);
        if (!fieldsErrors.isEmpty()) {
            logger.error("Ошибка при валидации полей: " + fieldsErrors);
            throw new DefaultValidationException(ValidationMessages.VALIDATION_ERROR_MESSAGE, fieldsErrors);
        }

        Optional<Doctor> doctor = doctorRepository.findByUsername(request.headPhysician());
        if (doctor.isEmpty()) {
            logger.error("Доктор для роли главвра не найден в базе: " + request.headPhysician());
            throw new ObjectNotFoundException(DoctorMessages.DOCTOR_NOT_FOUND);
        }

        doctor.get().setRole(Role.HEAD_PHYSICIAN);
        doctorRepository.save(doctor.get());
//        int doctorIsPresent = doctorRepository.updateDoctorRole(request.headPhysician());
//
//        if (doctorIsPresent == 0) {
//            throw new BadRequest(DoctorMessages.DOCTOR_ROLE_NOT_UPDATED);
//        }

        Hospital hospital = Hospital.builder()
                .hospitalName(request.hospitalName())
                .address(request.address())
                .headPhysician(request.headPhysician())
                .build();
        hospitalRepository.save(hospital);
        emailSender.send(request.headPhysician(), DoctorMessages.NEW_HEAD_PHYSICIAN + hospital.getHospitalName());
        logger.info("Новая больница создана: " + hospital.getHospitalName());
    }

    @Override
    public List<HospitalDto> getAllHospitals(Specialty specialty) {
        Specification<Hospital> specification = Specification.where(null);
        // toDO: validate data
        if (specialty != null) {
            specification = HospitalSpecification.hasDoctorInSpecialty(specialty);
        }

        return hospitalRepository.findAll(specification).stream()
                .map(HospitalMapper.INSTANCE::hospitalToDto)
                .collect(Collectors.toList());
    }
}
