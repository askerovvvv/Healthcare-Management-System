package com.pet.healthwave;

import com.pet.healthwave.auth.AuthService;
import com.pet.healthwave.auth.AuthenticationRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class HealthwaveApplication {
	public static void main(String[] args) {
		SpringApplication.run(HealthwaveApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthService authService) {
		return args -> {
			var token = new AuthenticationRequest("headPhis@bikedid.com", "223abcd!");
			System.out.println("Token: " + authService.authenticateService(token));
		};
	}

}
