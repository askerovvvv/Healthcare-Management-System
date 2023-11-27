package com.pet.healthwave.doctor;

import com.pet.healthwave.user.User;
import com.pet.healthwave.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Doctor d " +
            "SET d.isAccepted = TRUE " +
            "WHERE d.id = ?1")
    int updateIsAccepted(Long id);


    @Query("SELECT u FROM Doctor u WHERE u.email = ?1")
    Optional<Doctor> findByUsername(String email);

//    @Transactional
//    @Modifying
//    @Query("UPDATE Doctor d " +
//            "SET d.role = HEAD_PHYSICIAN " +
//            "WHERE d.email = ?1" )
//    int updateDoctorRole(String email);

}
