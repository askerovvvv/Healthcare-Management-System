package com.pet.healthwave.doctor;

import com.pet.healthwave.user.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper
public interface DoctorMapper {
    Logger logger = LoggerFactory.getLogger("DoctorServiceImpl");
    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    @InheritInverseConfiguration
    DoctorDTO doctorToDTO(Doctor doctor);

    default DoctorDTO map(Doctor doctor) {
        logger.info("Mapping Doctor to DoctorDTO: " + doctor);
        DoctorDTO result = doctorToDTO(doctor);
        logger.info("result: " + result);
        return result;
    }

}
