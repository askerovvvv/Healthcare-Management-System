//package com.pet.healthwave.doctor;
//
//import com.pet.healthwave.user.User;
//import org.mapstruct.InheritInverseConfiguration;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.SubclassMapping;
//import org.mapstruct.factory.Mappers;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@Mapper
//public interface DoctorMapper {
//    Logger logger = LoggerFactory.getLogger("DoctorServiceImpl");
//    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);
//
////    @InheritInverseConfiguration
////    @Mapping(target = "firstname", source = "firstname")
////    @Mapping(target = "hospitalName", source = "hospital.hospitalName")
//    void doctorToDTO(Doctor doctor);
//
//    default void map(Doctor doctor) {
//        logger.info("Mapping Doctor to DoctorDTO: " + doctor);
////        void result = doctorToDTO(doctor);
////        logger.info("result: " + result);
////        return result;
//    }
//
//}
