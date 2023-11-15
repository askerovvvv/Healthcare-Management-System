package com.pet.healthwave.doctor;

import com.pet.healthwave.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Doctor d " +
            "SET d.isAccepted = TRUE " +
            "WHERE d.id = ?1")
    int updateIsAccepted(Long id);

}
