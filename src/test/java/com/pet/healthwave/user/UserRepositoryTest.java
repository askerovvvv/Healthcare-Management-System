package com.pet.healthwave.user;

import com.pet.healthwave.TestConfig;
import com.pet.healthwave.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @MockBean
    @Deprecated
    private AuthService authService;

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