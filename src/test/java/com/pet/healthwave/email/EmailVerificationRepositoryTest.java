package com.pet.healthwave.email;

import com.pet.healthwave.TestConfig;
import com.pet.healthwave.auth.AuthService;
import com.pet.healthwave.user.User;
import com.pet.healthwave.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
class EmailVerificationRepositoryTest {
    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @MockBean // TODO: после (CommandLineRunner) окончания проекта удалить
    @Deprecated
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    private final String testToken = "testToken";

    @BeforeEach
    public void setUp() {
        User user = createUser();
        EmailVerificationToken emailVerificationToken = createEmailVerificationToken(user);

        userRepository.save(user);
        emailVerificationRepository.save(emailVerificationToken);

    }

    @Test
    @Transactional
    void testFindByToken() {
        boolean exists = emailVerificationRepository.findByToken(testToken).isPresent();

        assertTrue(exists);
    }

    @Test
    @Transactional
    void updateConfirmedAt() {
        int changes = emailVerificationRepository.updateConfirmedAt(LocalDateTime.now(), testToken);

        EmailVerificationToken verificationToken = emailVerificationRepository.findByToken(testToken).get();
        assertEquals(1, changes);

    }

    private EmailVerificationToken createEmailVerificationToken(User user) {
        return EmailVerificationToken.builder()
                .token(testToken)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();
    }

    private User createUser() {
        return User.builder()
                .email("test@example.com")
                .emailVerified(false)
                .build();
    }
}