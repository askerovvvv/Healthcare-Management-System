package com.pet.healthwave.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByUsername(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.emailVerified = TRUE " +
            "WHERE u.email = ?1")
    int updateEmailVerified(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.role = ?2 " +
            "WHERE u.id = ?1")
    int setAdminRole(Long id, Role role);
}
