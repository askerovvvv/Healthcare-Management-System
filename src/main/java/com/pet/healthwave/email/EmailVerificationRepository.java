package com.pet.healthwave.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerificationRepository, Long> {
    @Query("SELECT e FROM EmailVerificationToken e WHERE e.token = ?1")
    Optional<EmailVerificationToken> findByToken(@Param("email") String token);

}
