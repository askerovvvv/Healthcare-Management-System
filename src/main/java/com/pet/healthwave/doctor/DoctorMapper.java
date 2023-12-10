package com.pet.healthwave.doctor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    @Mapping(target = "hospitalName", source = "hospital.hospitalName")
    DoctorDTO doctorToDto(Doctor doctor);
}
