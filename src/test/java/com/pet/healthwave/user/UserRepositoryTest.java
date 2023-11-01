package com.pet.healthwave.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        User user = User.builder()
                .firstname("testName")
                .email("test@example.com")
                .emailVerified(false)
                .build();

        userRepository.save(user);
    }

    @Test
    void testFindByUsername() {
        boolean exists = userRepository.findByUsername("test@example.com").isPresent();

        assertTrue(exists);
    }

    @Test
    void updateEmailVerified() {
        int changes = userRepository.updateEmailVerified("test@example.com");

        assertEquals(1, changes);
    }
}