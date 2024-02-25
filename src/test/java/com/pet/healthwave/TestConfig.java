package com.pet.healthwave;

import com.pet.healthwave.auditing.ApplicationAuditAware;
import com.pet.healthwave.auth.AuthService;
import com.pet.healthwave.auth.AuthenticationRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

@TestConfiguration
public class TestConfig {

    @Bean
    public AuditorAware<Long> auditorAware() {
        return new ApplicationAuditAware();
    }

}