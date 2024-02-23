package com.pet.healthwave.hospital;

import com.pet.healthwave.doctor.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface HospitalMapper {
    HospitalMapper INSTANCE = Mappers.getMapper(HospitalMapper.class);

    @Mapping(target = "headPhysician", source = "hospital.headPhysician")
    @Mapping(target = "doctors", source = "doctors" ,qualifiedByName = "name")
    HospitalDto hospitalToDto(Hospital hospital);

    @Named("name")
    default List<String> name(Set<Doctor> doctors) {
        return doctors.stream()
                .map(doctor -> doctor.getFirstname() + " " + doctor.getLastname())
                .collect(Collectors.toList());
//        return doctors
//                .stream()
//                .map(User::getFirstname)
//                .collect(Collectors.toSet());
    }
}
